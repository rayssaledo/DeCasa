module.exports = function(mongodb, app, professionalCollection) {
	var PAGSEGURO_TOKEN = 'B21F539778C54CAB8E1A0355C9C12AE1';
	var PAGSEGURO_SANDBOX_TOKEN = 'FD218C1927A34DE891BEB11C465BE775';
	var PAGSEGURO_EMAIL = 'dcasa.projeto1@gmail.com';
	var PAGSEGURO_DEBUG = false; // true habilita o sandbox
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
		price: '19.90'
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

	app.get('/change-my-plan',function(req,res){
        res.render('change-my-plan');
    });
	app.get('/my-plan',function(req,res){
        res.render('my-plan');
    });
	app.get('/my-profile',function(req,res){
        res.render('my-profile');
    });
	app.get('/delete-account',function(req,res){
        res.render('delete-account');
    });
	app.get('/edit-register',function(req,res){
        res.render('edit-register');
    });
	app.get('/change-password',function(req,res){
        res.render('change-password');
    });
	app.get('/plans',function(req,res){
        res.render('plans');
    });
	app.get('/free-registration',function(req,res){
        res.render('free-registration');
    });
	app.get('/register-silver',function(req,res){
        res.render('register-silver');
    });
	 app.get('/register-bronze',function(req,res){
        res.render('register-bronze');
    });
	
    app.get('/register-gold',function(req,res){
        res.render('register-gold');
    });
	app.get('/change-to-gold',function(req,res){
        res.render('change-to-gold');
    });
	app.get('/change-to-silver',function(req,res){
        res.render('change-to-silver');
    });
	app.get('/change-to-bronze',function(req,res){
        res.render('change-to-bronze');
    });
	app.get('/change-to-free',function(req,res){
        res.render('change-to-free');
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
	
	app.post("/add-professional",  upload.single('picture') , function (req, res){
		var name = req.body.name;
		var businessName = req.body.businessName;
		var cpf = req.body.cpf;
		var phone1 = req.body.phone1;
		var phone2 = req.body.phone2;
		var phone3 = req.body.phone3;
		var phone4 = req.body.phone4;
		var street = req.body.street;
		var number = req.body.number;
		var neighborhood = req.body.neighborhood;
		var city = req.body.city;
		var state = req.body.state;					
		var site = req.body.site;		
		var socialNetwork1 = req.body.socialNetwork1;
		var socialNetwork2 = req.body.socialNetwork2;
		var service = req.body.service;
		var description = req.body.description;
		var email = req.body.email;		
		var password = req.body.password;
		var passwordRepeat = req.body.passwordRepeat;		
		var evaluations = [];
		var avg = 0;
		var plan = req.body.plan;
		/*if (req.file){
			picture = req.file.filename;
		}*/
		var picture;
		try {
			picture = req.file.filename;
		}catch(err) {
		}
			
		var insert = function(code) {
			professionalCollection.find({
				email: email
			}).toArray(function(err, array){
				if (array.length > 0) {
					res.json({ "ok" : 0, "msg" : "Email is being used!" });
				} else {
					
					professionalCollection.insert({
						name: name,
						businessName: businessName,
						cpf: cpf, 
						phone1: phone1,
						phone2: phone2,
						phone3: phone3,
						phone4: phone4,
						street: street,
						number: number,
						neighborhood: neighborhood,
						city: city,
						state: state,
						site: site,
						socialNetwork1: socialNetwork1,
						socialNetwork2: socialNetwork2,
						picture: picture,					
						service: service,	
						description: description,
						evaluations: evaluations,
						avg: avg,
						email: email,									
						password: password,
						plan : plan
						
					}, function (err, doc) {
						if(err){
							res.json({ "ok" : 0, "msg" : err });
						} else {
							var redirect = null;
							if (code) {
								if (PAGSEGURO_DEBUG) {
									redirect = "https://sandbox.pagseguro.uol.com.br/v2/checkout/payment.html?code=" + code;
								} else {
									redirect = "https://pagseguro.uol.com.br/v2/checkout/payment.html?code=" + code;
								}
							}
							res.json({"ok": 1, "msg": "Success on register!", "redirect": redirect });	
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
	
	
	
	app.post("/change-plan", function (req, res){
		var name = req.body.name;
		var businessName = req.body.businessName;
		var cpf = req.body.cpf;
		var phone1 = req.body.phone1;
		var phone2 = req.body.phone2;
		var phone3 = req.body.phone3;
		var phone4 = req.body.phone4;
		var street = req.body.street;
		var number = req.body.number;
		var neighborhood = req.body.neighborhood;
		var city = req.body.city;
		var state = req.body.state;					
		var site = req.body.site;		
		var socialNetwork1 = req.body.socialNetwork1;
		var socialNetwork2 = req.body.socialNetwork2;
		var service = req.body.service;
		var description = req.body.description;
		var email = req.body.email;		
		var password = req.body.password;	
		var plan = req.body.plan;
		var planid = req.body.planid;
					
		var insert = function(code) {
			professionalCollection.find({
				email: email
			}).toArray(function(err, array){
				if (array.length == 0) {
					res.json({ "ok" : 0, "msg" : "E-mail must be informed!" });
				} else {
					
					professionalCollection.update({						
						email: email
					},
					{ $set: {
						name: name,
						businessName: businessName,
						cpf: cpf, 
						phone1: phone1,
						phone2: phone2,
						phone3: phone3,
						phone4: phone4,
						street: street,
						number: number,
						neighborhood: neighborhood,
						city: city,
						state: state,
						site: site,
						socialNetwork1: socialNetwork1,
						socialNetwork2: socialNetwork2,
						description: description,															
						password: password,
						plan_date: new Date(),
						plan: plan, 
						planid: planid
					}
						
					}, function (err, doc) {
						if(err){
							res.json({ "ok" : 0, "msg" : err });
						} else {
							res.json({"ok": 1, "msg": "Success on update!"});	
						}
					});
				}
			});
		}
		if (plan == 'free') {
			insert();
		} else {
			console.log("EMAIL EMAIL " + email);
			var httpReqData = 'email=' + PAGSEGURO_EMAIL + '&token=' + (PAGSEGURO_DEBUG ? PAGSEGURO_SANDBOX_TOKEN : PAGSEGURO_TOKEN) + '&currency=BRL';
			httpReqData += '&senderName=' + name + '&senderEmail=' + email;
			for (var i = 0; i < PAGSEGURO_ITEMS.length; i++) {
				var item = PAGSEGURO_ITEMS[i];
				if (plan == item.name) {
					httpReqData += '&itemId1=' + item.id + '&itemDescription1=' + item.description + '&itemAmount1=' + item.price + '&itemQuantity1=1&reference=' + email;
					break;
				}
			}
			console.log(httpReqData);
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
	
	app.post("/update-professional", function (req, res){
		var name = req.body.name;
		var businessName = req.body.businessName;
		var cpf = req.body.cpf;
		var phone1 = req.body.phone1;
		var phone2 = req.body.phone2;
		var phone3 = req.body.phone3;
		var phone4 = req.body.phone4;
		var street = req.body.street;
		var number = req.body.number;
		var neighborhood = req.body.neighborhood;
		var city = req.body.city;
		var state = req.body.state;					
		var site = req.body.site;		
		var socialNetwork1 = req.body.socialNetwork1;
		var socialNetwork2 = req.body.socialNetwork2;
		var service = req.body.service;
		var description = req.body.description;
		var email = req.body.email;		
		var password = req.body.password;
		var passwordRepeat = req.body.passwordRepeat;
		
		professionalCollection.find({
			email: email
		}).toArray(function(err, array){
			if (array.length == 0) {
				res.json({ "ok" : 0, "msg" : "Professional not found!" });
			} else {
				professionalCollection.update({						
						email: email
					},
					{ $set: {
						name: name,
						businessName: businessName,
						cpf: cpf, 
						phone1: phone1,
						phone2: phone2,
						phone3: phone3,
						phone4: phone4,
						street: street,
						number: number,
						neighborhood: neighborhood,
						city: city,
						state: state,
						site: site,
						socialNetwork1: socialNetwork1,
						socialNetwork2: socialNetwork2,
						description: description,															
						password: password
					}
						
					}, function (err, doc) {
						if(err){
							res.json({ "ok" : 0, "msg" : err });
						} else {
							res.json({"ok": 1, "msg": "Success on update!"});	
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
	
    app.post("/add-professional-with-plan", function (req, res){
		var name = req.body.name;
		var businessName = req.body.businessName;
		var cpf = req.body.cpf;
		var phone1 = req.body.phone1;
		var phone2 = req.body.phone2;
		var phone3 = req.body.phone3;
		var phone4 = req.body.phone4;
		var street = req.body.street;
		var number = req.body.number;
		var neighborhood = req.body.neighborhood;
		var city = req.body.city;
		var state = req.body.state;					
		var site = req.body.site;		
		var socialNetwork1 = req.body.socialNetwork1;
		var socialNetwork2 = req.body.socialNetwork2;
		var service = req.body.service;
		var description = req.body.description;
		var email = req.body.email;		
		var password = req.body.password;
		var passwordRepeat = req.body.passwordRepeat;		
		var evaluations = [];
		var avg = 0;				
		var plan = req.body.plan;
		var planid= req.body.planid;
		
		professionalCollection.find({
			email: email
		}).toArray(function(err, array){
			if (array.length > 0) {
				res.json({ "ok" : 0, "msg" : "Professional already exists!" });
			} else {
				
				professionalCollection.insert({
					name: name,
					businessName: businessName,
					cpf: cpf, 
					phone1: phone1,
					phone2: phone2,
					phone3: phone3,
					phone4: phone4,
					street: street,
					number: number,
					neighborhood: neighborhood,
					city: city,
					state: state,
					site: site,
					socialNetwork1: socialNetwork1,
					socialNetwork2: socialNetwork2,	
					service: service,	
					description: description,
					evaluations: evaluations,
					avg: avg,
					email: email,									
					password: password,
					plan_date: new Date(),
					plan: plan, 
					planid: planid
					
				}, function (err, doc) {
					if(err){
						res.json({ "ok" : 0, "msg" : err });
					} else {
						res.json({"ok": 1, "msg": "Success on register!"});	
					}
				});
			}
		});						
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
	
	app.get("/get-professional", function(req, res) {
		var email = req.query.email;
				
		if (!email) {
			res.json({"ok": 0, "msg": "E-mail must be informed!"});
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
	
	app.get("/get-all-professionals", function(req, res){
		professionalCollection.find({}).toArray(function(err, array){
			if(err) {
				res.json({ "ok" : 0, "msg" : err });
			} else {					
				if(array.length < 1) {						
					res.json({ "ok" : 0, "msg" : "Empty list!" });
				} else {
					res.json({ "ok" : 1, "result" : array });
				}
			}			
		});
	});	
		
	app.post("/add-evaluation", function(req, res) {
		var emailProfessional = req.body.emailProfessional;
		var emailUser = req.body.emailUser;
		var evaluation = req.body.evaluation;
		var comment = req.body.comment;
		var userPhoto = req.body.userPhoto;
		var date = req.body.date;
		var service = req.body.service;
		
		if (!emailProfessional) {
			res.json({"ok": 0, "msg": "Professional email not found!"});
		} else if (!emailUser) {
			res.json({"ok": 0, "msg": "User email not found!"});
		} else if (!evaluation) {
			res.json({"ok": 0, "msg": "Evaluation not informed!"});
		} else if (!date) {
			res.json({"ok": 0, "msg": "Date not informed!"});
		} else if (!service) {
			res.json({"ok": 0, "msg": "Service not informed!"});
		} else {
			evaluation = parseFloat(evaluation);
			professionalCollection.find({
				email: emailProfessional
				
			}).toArray(function(err, array){
				if (array.length < 1) {
					res.json({ "ok" : 0, "msg" : "Professional not found!" });
				} else {
					
					professionalCollection.update({ 
						email: emailProfessional, 
						"evaluations.emailValuer": emailUser
					},
					{
						$pull : {
							"evaluations": {emailValuer : emailUser}
						}
					
					},function(err, doc){
						if(err){
							res.json({"ok" : 0, "msg" : err });
						} else {
							
							professionalCollection.update({
								email: emailProfessional
							},
							{ $push: { 
								evaluations: {								
									emailValuer: emailUser, 
									evaluation: evaluation,
									comment: comment,
									userPhoto: userPhoto,
									date: date
								} 
							}							
							},function(err, doc){
								if(err){
									res.json({"ok" : 0, "msg" : err });
								} else {
									setAvg(emailProfessional);
									res.json({"ok": 1, "msg": "Evaluation is done!"});	
									
								}
							});
						}
					});
				}
			});	
		}
	});
	
	function setAvg(email) {
		professionalCollection.findOne({
			email: email
				
		}, function(err, doc){
						
			var evaluations = 0;
			var numEvaluations = 0;
			var avg = 0;
			
			for (i = 0; i < doc.evaluations.length; i++) {				
				evaluations += doc.evaluations[i].evaluation;
				numEvaluations += 1;
				
			}			
			
			if (numEvaluations > 0) {
				avg = evaluations / numEvaluations;
			}
			professionalCollection.update({
				email: email
			}, {
				$set: { avg: avg }
			});
			
		});
	}
	
	app.get("/get-evaluations-professional", function (req, res) {
		var email = req.query.email;
		
		 if (!email) {
			res.json({"ok": 0, "msg": "Professional email not informed!"});
		} else {
			professionalCollection.find({
				email: email
				
			},{
				_id: 0, evaluations: 1
			
			}).toArray(function(err, array){
				if (array.length < 1) {
					res.json({ "ok" : 0, "msg" : "Professional not found!" });
				} else {					
					res.json({"ok": 1, "result": array});
				}
			});			
		}			
	});
		
	app.get("/get-avg-professional", function (req, res) {
		var email = req.query.email;
		
		 if (!email) {
			res.json({"ok": 0, "msg": "Professional email not informed!"});
		} else {
					
			professionalCollection.find({
				email: email
				
			}, {
				
				_id:0, avg: 1
			}).toArray(function(err, array){
				if (array.length < 1) {
					res.json({ "ok" : 0, "msg" : "Professional not found!" });
				} else {					
					res.json({"ok": 1, "result": array});
				}
			});			
		}			
	});	
	
		
	app.get("/sort-professionals-plan", function(req, res){
		var service = req.query.service;
		if (!service) {
			res.json({"ok" : 0, "msg": "Service must be informed!"});
		} else {
				
			professionalCollection.find({service:service}).sort({planid: -1, avg:-1}).toArray(function(err, array){
				if(err) {
					res.json({ "ok" : 0, "msg" : err });
				} else {					
					if(array.length < 1) {						
						res.json({ "ok" : 0, "msg" : "Empty list!" });
					} else {
						res.json({ "ok" : 1, "result" : array });
					}
				}			
			});
		}
	});
	
	app.post("/update-password", function(req, res) {
		var email = req.body.email;
		var newPassword = req.body.newPassword;
		if (!email) {
			res.json({ "ok" : 0, "msg" : "Email not informed!" });
		} else if (!newPassword) {
			res.json({ "ok" : 0, "msg" : "New Password not informed!" });
		} else {
			
			professionalCollection.find({
				email: email
			}).toArray(function(err, array){
				if (array.length == 0) {
					res.json({ "ok" : 0, "msg" : "Professional not found!" });
				} else {
					professionalCollection.update({						
						email: email
					},
					{ $set: {
						password: newPassword
					}
						
					}, function (err, doc) {
						if(err){
							res.json({ "ok" : 0, "msg" : err });
						} else {
							res.json({"ok": 1, "msg": "Success on update!"});	
						}
					});
					
				}
			});
		}
	});
	
	app.post("/set-plan", function(req, res) {
		var email = req.body.email;
		var plan = req.body.plan;
		var planid = req.body.planid;
		
		if (!email) {
			res.json({ "ok" : 0, "msg" : "Email not informed!" });
		} else if (!plan) {
			res.json({ "ok" : 0, "msg" : "Plan not informed!" });
		} else if (!planid) {
			res.json({ "ok" : 0, "msg" : "Plan id not informed!" });
		} else {
			professionalCollection.find({
				email: email
			}).toArray(function(err, array){
				if (array.length == 0) {
					res.json({ "ok" : 0, "msg" : "Professional not found!" });
				} else {
					professionalCollection.update({						
							email: email
						},
						{ $set: {
							plan_date: new Date(date), plan: plan, planid: planid
						}
							
						}, function (err, doc) {
							if(err){
								res.json({ "ok" : 0, "msg" : err });
							} else {
								res.json({"ok": 1, "msg": "Success on update!"});	
							}
						});
					
				}
			});	
		}
	});
	
	app.delete("/delete-account", function(req, res){
		var email = req.body.email;
				
		if (!email) {
			res.json({ "ok" : 0, "msg" : "Email not informed!" });
		}  else {		
		
			professionalCollection.remove({						
				email: email
			}, function (err, doc) {
				if(err){
					res.json({ "ok" : 0, "msg" : err });
				} else {
					res.json({"ok": 1, "msg": "Success on delete!"});	
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