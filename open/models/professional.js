module.exports = function(mongodb, app, professionalCollection) {
	var PAGSEGURO_TOKEN = 'B21F539778C54CAB8E1A0355C9C12AE1';
	var PAGSEGURO_SANDBOX_TOKEN = 'FD218C1927A34DE891BEB11C465BE775';
	var PAGSEGURO_EMAIL = 'dcasa.projeto1@gmail.com';
	var PAGSEGURO_DEBUG = true; // habilita o sandbox 
	var PAGSEGURO_ITEMS = [
	{
		name: 'gold',
		id: '0001',
		description: 'Plano ouro',
		price: '59.90'
	},
	{
		name: 'silver',
		id: '0002',
		description: 'Plano prata',
		price: '39.90'
	},
	{
		name: 'bronze',
		id: '0003',
		description: 'Plano bronze',
		price: '29.90'
	}
	];
	
	var AM = require('./account-manager');		
	var path = require('path');
	var fs = require ( 'fs'); 
	var cookieParser = require('cookie-parser');
	app.use(cookieParser());
	var multer = require('multer');
	var http = require('https');
	var parseString = require('xml2js').parseString;
	
	var storage = multer.diskStorage({
	destination: function (req, file, cb) {
		cb(null, 'public/uploads/');
	},
	filename: function (req, file, cb) {
		var ext = file.originalname.substr(file.originalname.lastIndexOf('.') + 1);
		cb(null, file.fieldname + '-' + Date.now() + '.' + ext);
	}
	});
	var upload = multer({ storage: storage });
	
	app.get('/',function(req,res){		
		if(req.cookies != undefined){
			if (req.cookies.email == undefined || req.cookies.password == undefined){
				res.render('index');
			}else{
				// attempt automatic login //
				AM.autoLogin(req.cookies.email, req.cookies.password, function(o){
					if (o != null){
						req.session.email = o;
						res.redirect('profile');
					}	else{
						res.render('index');
					}
				});
			}
		}else{
			res.render('index');
		}
	});

	app.get('/plans',function(req,res){
        res.render('plans');
    });
	app.get('/free-registration',function(req,res){
        res.render('free-registration');
    });

    app.get('/register-gold',function(req,res){
        res.render('register-gold');
    });

	app.get('/profile',function(req,res){
		if (req.cookies.email == undefined || req.cookies.password == undefined){
			res.redirect('/');
		}	else{
			// attempt automatic login //
			AM.autoLogin(req.cookies.email, req.cookies.password, function(o){
				if (o != null){
					req.session.email = o;
					res.render('profile');
				}	else{
					res.redirect('/');
				}
			});
		}
		
	});	
	
	app.post('/logout', function(req, res){
		res.clearCookie('email');
		res.clearCookie('password');
		req.session.destroy(function(e){
			res.json({"ok": 1, "msg": "Deslogado com sucesso!"}); 
			console.log("profissional deslogado...");
			res.render('index');
		});
	})
	
	app.get('/getEmailProfissionalLogado', function(req, res) {
		if (req.cookies.email == undefined || req.cookies.password == undefined){
			res.json({"ok": 0, "msg": "Não logado!"});
		}	else{ 
			res.json({"ok": 0, "email": req.cookies.email});
		}
	});
	
	var doRequest = function(options, data, callback) {
		if (!callback) callback = data;
		var httpReq = http.request(options, function(httpRes) {
			var httpData = '';
			httpRes.on('data', function(data) {
				httpData += data.toString('utf8');
			});
			httpRes.on('end', function() {
				parseString(httpData, function(err, result) {
					if (err) return callback(err);
					return callback(null, result);
				});
			});
		});
		httpReq.on('error', function(err) {
			return callback(err);
		});
		if (arguments.length == 3) {
			httpReq.write(data);
		}
		httpReq.end();
	};
	
	app.post("/confirm-payment", function(req, res) {
		var id = req.body.notificationCode;
		var type = req.body.notificationType;
		doRequest({
			host: (PAGSEGURO_DEBUG ? 'ws.sandbox.pagseguro.uol.com.br' : 'ws.pagseguro.uol.com.br'),
			port: 443,
			path: '/v3/transactions/notifications/' + id + '?email=' + PAGSEGURO_EMAIL + '&token=' + (PAGSEGURO_DEBUG ? PAGSEGURO_SANDBOX_TOKEN : PAGSEGURO_TOKEN),
			method: 'GET'
		}, function(err, result) {
			if (err || !result) return res.json({ ok: 0, msg: err });
			else if (result.errors) return res.json({ ok: 0, msg: result.errors.error[0].message[0] });
			else if (!result.transaction || result.transaction.status != 3) return res.status(200);
			var date = result.transaction.date[0];
			var plan = null;
			var planid = result.transaction.items[0].item[0].id[0];
			for (var i = 0; i < PAGSEGURO_ITEMS.length; i++) {
				if (planid == PAGSEGURO_ITEMS[i].id) {
					plan = PAGSEGURO_ITEMS[i].name;
				}
			}
			var email = result.transaction.reference[0];
			professionalCollection.update({
				email: email
			}, {
				$set: { plan_date: new Date(date), plan: plan, planid: planid }
			}, function(err) {
				if (err) return res.json({ ok: 0, msg: err });
				return res.json({ ok: 1 });
			});
			return res.status(200);
		});
	});
	
	app.post("/add-profissional",  upload.single('picture') , function (req, res){
		var name = req.body.name;
		var cpf = req.body.cpf;
		var phone = req.body.phone;
		var street = req.body.street;
		var number = req.body.number;
		var neighborhood = req.body.neighborhood;
		var city = req.body.city;
		var state = req.body.state;					
		var site = req.body.site;		
		var socialNetwork = req.body.socialNetwork;
		var picture = req.file.filename;
		var services = req.body.services;					
		var email = req.body.email;		
		var password = req.body.password;
		var passwordRepeat = req.body.passwordRepeat;
		var plan = req.body.plan;
		var avaliacoes = [];
		var avg = 0;
		var insert = function(code) {
			professionalCollection.find({
				email: email
			}).toArray(function(err, array){
				if (array.length > 0) {
					res.json({ "ok" : 0, "msg" : "Este email já está em uso!" });
				} else {
					professionalCollection.insert({
						name: name,
						cpf: cpf, 
						phone: phone,
						street: street,
						number: number,
						neighborhood: neighborhood,
						city: city,
						state: state,
						site: site,
						socialNetwork: socialNetwork,
						picture: picture,					
						services: services,	
						avaliacoes: avaliacoes,
						avg: avg,
						email: email,									
						password: password
					}, function (err, doc) {
						if(err){
							res.json({ "ok" : 0, "msg" : err });
						} else {
							res.json({"ok": 1, "msg": "Cadastro realizado com sucesso!", code: code });	
						}
					});
				}
			});
		}
		if (plan == 'free') {
			insert();
		} else {
			var httpReqData = 'email=' + PAGSEGURO_EMAIL + '&token=' + (PAGSEGURO_DEBUG ? PAGSEGURO_SANDBOX_TOKEN : PAGSEGURO_TOKEN) + '&currency=BRL';
			httpReqData += '&senderName=' + name + '&senderEmail=' + email;
			for (var i = 0; i < PAGSEGURO_ITEMS.length; i++) {
				var item = PAGSEGURO_ITEMS[i];
				if (plan == item.name) {
					httpReqData += '&itemId1=' + item.id + '&itemDescription1=' + item.description + '&itemAmount1=' + item.price + '&itemQuantity1=1&reference=' + email;
					break;
				}
			}
			doRequest({
				host: (PAGSEGURO_DEBUG ? 'ws.sandbox.pagseguro.uol.com.br' : 'ws.pagseguro.uol.com.br'),
				port: 443,
				path: '/v2/checkout',
				method: 'POST',
				headers: {
					'Content-Type': 'application/x-www-form-urlencoded; charset=ISO-8859-1'
				}
			}, httpReqData, function(err, result) {
				if (err) return res.json({ ok: 0, msg: err });
				if (result.errors) {
					if (result.errors.error[0].code == '11012') {
						return res.json({ ok: 0, msg: 'Nome inválido.' }); 
					} else {
						return res.json({ ok: 0, msg: result.errors.error[0].message }); 
					}
				} else {
					insert(result.checkout.code[0]);
				}
			});
		}
		
	});
	
	app.post("/update-profissional", upload.single('picture'), function (req, res){
		var name = req.body.name;
        var cpf = req.body.cpf;
        var phone = req.body.phone;
        var street = req.body.street;
        var number = req.body.number;
        var neighborhood = req.body.neighborhood;
        var city = req.body.city;
        var state = req.body.state;
        var site = req.body.site;
        var socialNetwork = req.body.socialNetwork;
        var picture = req.file.filename;
        var services = req.body.services;
        var email = req.body.email;
        var password = req.body.password;
        var passwordRepeat = req.body.passwordRepeat;

		professionalCollection.find({
			email: email
		}).toArray(function(err, array){
			if (array.length == 0) {
				res.json({ "ok" : 0, "msg" : "Profissional não encontrado!" });
			} else {
				professionalCollection.update({						
						email: email						
					},
					{ $set: {
						name: name,
						cpf: cpf, 
						phone: phone,
						street: street,
						number: number,
						neighborhood: neighborhood,
						city: city,
						state: state,
						site: site,
						socialNetwork: socialNetwork,					
						services: services,							
						password: password,
						picture : picture
					}
						
					}, function (err, doc) {
						if(err){
							res.json({ "ok" : 0, "msg" : err });
						} else {
							res.json({"ok": 1, "msg": "Atualizado com sucesso!"});	
						}
					});
				
			}
		});
	});
	
	app.get("/download-image", function(req, res) {
		var img = req.query.img;
		var dir = path.resolve(".")+'/uploads/'+img;
		console.log(path.resolve(".")+'/uploads/'+img);
		/*fs.readdir(dir, function(err, list) { // read directory return  error or list
		if (err) 
			return res.json(err);
		else
			res.json(list);

        });*/
		res.download(dir);
	});
	
    
	app.post("/add-all-profissional", function (req, res){
		console.log("HELLO");
		console.log(req);
		for (var i = 0; i < req.body.pro.length; i++) { 
			console.log("HELLO");
			var name = req.body.pro[i].name;
			var cpf = req.body.pro[i].cpf;
			var phone = req.body.pro[i].phone;
			var street = req.body.pro[i].street;
			var number = req.body.pro[i].number;
			var neighborhood = req.body.pro[i].neighborhood;
			var city = req.body.pro[i].city;
			var state = req.body.pro[i].state;				
			var site = req.body.pro[i].site;
			
			var socialNetwork = req.body.pro[i].socialNetwork;
			var picture = req.body.pro[i].picture;
			var services = req.body.pro[i].services;
			
					
			var email = req.body.pro[i].email;		
			var password = req.body.pro[i].password;
			
			var avaliacoes = [];
			var avg = 0;
			
			console.log(email);
			professionalCollection.insert({
				name: name,
				cpf: cpf, 
				phone: phone,
				street: street,
				number: number,
				neighborhood: neighborhood,
				city: city,
				state: state,
				site: site,
				socialNetwork: socialNetwork,
				picture: picture,					
				services: services,	
				avaliacoes: avaliacoes,
				avg: avg,
				email: email,									
				password: password
				
			}, function (err, doc) {
				if(err){
					res.json({ "ok" : 0, "msg" : err });
				} else {
					res.json({"ok": 1, "msg": "Cadastro realizado com sucesso!"});	
				}
			});
		}
		
	});
	
	
	app.post("/login", function(req, res) {
		var email = req.body.email;
		var password = req.body.password;
		var remember = req.body.remember;
		
		AM.manualLogin(email, password, function(err, obj){
			if (!obj){
				res.json({ "ok" : 0, "msg" : err });
			}	else{
				req.session.email = obj;				
				res.cookie('email', obj.email, { maxAge: 900000 });
				res.cookie('password', obj.password, { maxAge: 900000 });				
				res.json({ "ok" : 1, "msg" : obj });
			}
		});				
	
	});
	
	app.get("/get-profissional", function(req, res) {
		var email = req.query.email;
				
		if (!email) {
			res.json({"ok": 0, "msg": "E-mail deve ser informado!"});
		} else {
			professionalCollection.findOne({
				email: email
				
			}, function(err, doc){
				if(err){
					res.json({ "ok" : 0, "msg" : err });
				} else {
					res.json({"ok": 1, "result": doc});	
				}
			});
		}
		
	});
	
	app.get("/get-todos-profissionais", function(req, res){
		professionalCollection.find({}).toArray(function(err, array){
			if(err) {
				res.json({ "ok" : 0, "msg" : err });
			} else {					
				if(array.length < 1) {						
					res.json({ "ok" : 0, "msg" : "Lista de Profissionais vazia!" });
				} else {
					res.json({ "ok" : 1, "result" : array });
				}
			}			
		});
	});	
	
	app.get("/get-by-service", function(req, res){
		var service = req.query.service;
		
		professionalCollection.find({ "services" : { "$in" : [service]}}).toArray(function(err, array){
			if(err) {
				res.json({ "ok" : 0, "msg" : err });
			} else {					
				if(array.length < 1) {						
					res.json({ "ok" : 0, "msg" : "Nenhum profissional com este serviço foi encontrado." });
				} else {
					res.json({ "ok" : 1, "result" : array });
				}
			}			
		});
	});	
	
	app.post("/add-avaliacao", function(req, res) {
		var emailProfissional = req.body.emailProfissional;
		var emailUsuario = req.body.emailUsuario;
		var avaliacao = req.body.avaliacao;
		var comentario = req.body.comentario;
		var fotoUsuario = req.body.fotoUsuario;
		var data = req.body.data;
		var service = req.body.service;
		
		if (!emailProfissional) {
			res.json({"ok": 0, "msg": "Email do profissional não informado!"});
		} else if (!emailUsuario) {
			res.json({"ok": 0, "msg": "Email do usuário não informado!"});
		} else if (!avaliacao) {
			res.json({"ok": 0, "msg": "Avaliacao não informada!"});
		} else if (!data) {
			res.json({"ok": 0, "msg": "Data não informada!"});
		} else if (!service) {
			res.json({"ok": 0, "msg": "Serviço não informado!"});
		} else {
			avaliacao = parseFloat(avaliacao);
			professionalCollection.find({
				email: emailProfissional
				
			}).toArray(function(err, array){
				if (array.length < 1) {
					res.json({ "ok" : 0, "msg" : "Nenhum profissional encontrado com esse email!" });
				} else {
					
					professionalCollection.update({ 
						email: emailProfissional, 
						"avaliacoes.emailAvaliador": emailUsuario, 
						"avaliacoes.service" : service
					},
					{
						$pull : {
							"avaliacoes": {emailAvaliador : emailUsuario, service: service}
						}
					
					},function(err, doc){
						if(err){
							res.json({"ok" : 0, "msg" : err });
						} else {
							
							professionalCollection.update({
								email: emailProfissional
							},
							{ $push: { 
								avaliacoes: {								
									emailAvaliador: emailUsuario, 
									avaliacao: avaliacao,
									comentario: comentario,
									fotoUsuario: fotoUsuario,
									data: data, 
									service: service
								} 
							}							
							},function(err, doc){
								if(err){
									res.json({"ok" : 0, "msg" : err });
								} else {
									res.json({"ok": 1, "msg": "Avaliação Realizada!"});	
									
								}
							});
							
							
							
						}
					});
					
				}
			});	
		}
		
	});
	
	app.get("/get-avaliacoes-profissional", function (req, res) {
		var email = req.query.email;
		var service = req.query.service;
		
		 if (!email) {
			res.json({"ok": 0, "msg": "Email do profissional não informado!"});
		} else if (!service) {
			res.json({"ok": 0, "msg": "Serviço não informado!"});
		} else {
			professionalCollection.find({
				email: email,
				"avaliacoes.service": service
				
			}).toArray(function(err, array){
				if (array.length < 1) {
					res.json({ "ok" : 0, "msg" : "Nenhum profissional encontrado com esse email!" });
				} else {					
					var result = [];
					var avaliacoes = 0;
					var numAvaliacoes = 0;
					var avg = 0;
					for (i = 0; i < array[0].avaliacoes.length; i++) { 
						if (array[0].avaliacoes[i].service == service){
							avaliacoes += array[0].avaliacoes[i].avaliacao;
							numAvaliacoes += 1;
							result.push(array[0].avaliacoes[i]);
						}
					}
					if (numAvaliacoes > 0) {
						avg = avaliacoes / numAvaliacoes;
					}
					res.json({"ok": 1, "avaliacoes": result, "avg": avg});
				}
			});			
		}			
	});	
		
	app.get("/sort-profissionais-servico", function(req, res){
		var service = req.query.service;
		if (!service) {
			res.json({"ok" : 0, "msg": "Servico deve ser informado!"});
		} else {
			
			professionalCollection.find({
				"services" : { "$in" : [service]}
				
			}).toArray(function(err, array){
				if (array.length < 1) {
					res.json({ "ok" : 0, "msg" : "Lista Vazia" });
				} else {					
										
					for (i = 0; i < array.length; i++) {
						var avaliacoes = 0;
						var numAvaliacoes = 0;
						var avg = 0;
						var emailProfessional = array[i].email;
						console.log(emailProfessional);
						for (j = 0; j < array[i].avaliacoes.length; j++) { 
							if (array[i].avaliacoes[j].service == service){
								avaliacoes += array[i].avaliacoes[j].avaliacao;
								numAvaliacoes += 1;
							}
						}
						if (numAvaliacoes > 0) {
							avg = avaliacoes / numAvaliacoes;
						}
						
						professionalCollection.update({email:emailProfessional},{$set:{avg:avg}});
					}
				}
			});	
	
			professionalCollection.find({"services" : { "$in" : [service]}}).sort({avg:-1}).toArray(function(err, array){
				if(err) {
					res.json({ "ok" : 0, "msg" : err });
				} else {					
					if(array.length < 1) {						
						res.json({ "ok" : 0, "msg" : "Lista de Profissionais vazia!" });
					} else {
						res.json({ "ok" : 1, "result" : array });
					}
				}			
			});
		}
	});
	
	function checkInconsistencyLogin(email, password) {
		if (!email) {
			return '{ "ok" : 0, "msg" : "Email ser informado!" }';
		}
		
		if (!password) {
			return '{ "ok" : 0, "msg" : "Senha deve ser informada!" }';	
		}
	}
	
	return this;
}