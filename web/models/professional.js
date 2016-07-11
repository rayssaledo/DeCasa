module.exports = function(mongodb, app, professionalCollection) {

	app.post("/add-profissional", function (req, res){
		var cpf = req.body.cpf;
		var businessName = req.body.businessName;
		var email = req.body.email;		
		var address = req.body.address;
		var phone = req.body.phone;
		var socialNetwork = req.body.socialNetwork;
		var pictury = req.body.pictury;
		var site = req.body.site;	
		var username = req.body.username;
		var password = req.body.password;	
		var passwordRepeat = req.body.passwordRepeat;
		var personalName = req.body.personalName;
		
		var inconsistency = checkInconsistency(businessName, personalName, phone, cpf, email, address, username, password, passwordRepeat);

		if (inconsistency) {
			res.send(inconsistency);
		} else {
			professionalCollection.find({
				username: username,
				password: password
				
			}).toArray(function(err, array){
				if (array.length > 0) {
					res.json({ "ok" : 0, "msg" : "Profissional já cadastrado!" });
				} else {
					
					professionalCollection.insert({
						username: username,
						password: password,
						cpf: cpf,
						businessName: businessName,
						email: email,
						address: address,
						phone: phone,
						socialNetwork: socialNetwork,
						pictury: pictury,
						site: site
						
					}, function (err, doc) {
						if(err){
							res.json({ "ok" : 0, "msg" : err });
						} else {
							res.json({"ok": 1, "msg": "Cadastro realizado com sucesso!"});	
						}
					});
				}
			});
		}
	});
	
	app.post("/check-username", function(req, res) {
		var username = req.body.username;
		var password = req.body.password;
		
		var inconsistency = checkInconsistencyusername(username, password);
		
		if (inconsistency) {
			res.send(inconsistency);
		} else {
			professionalCollection.find({
				username: username,
				password: password
				
			}).toArray(function(err, array){
				if(err) {
					res.json({ "ok" : 0, "msg" : err });
				} else {					
					if(array.length < 1) {						
						res.json({ "ok" : 0, "msg" : "Profissional não cadastrado!" });
					} else {
						res.json({ "ok" : 1, "msg" : "Profissional cadastrado!" });
					}
				}
				
			});
		}
		
	});
	
	app.get("/get-profissional", function(req, res) {
		var username = req.query.username;
				
		if (!username) {
			res.json({"ok": 0, "msg": "Nome de usuário deve ser informado!"});
		} else {
			professionalCollection.findOne({
				username: username
				
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
	
	
	function checkInconsistencyusername(username, password) {
		if (!username) {
			return '{ "ok" : 0, "msg" : "Nome de usuário deve ser informado!" }';
		}
		
		if (!password) {
			return '{ "ok" : 0, "msg" : "Senha deve ser informada!" }';	
		}
	}
	
	function checkInconsistency(businessName, personalName, phone, cpf, email, address, username, password, passwordRepeat) {
		if (!businessName) {
			return '{ "ok" : 0, "msg" : "Nome do negócio deve ser informado!" }';
		}
		
		if (!personalName) {
			return '{ "ok" : 0, "msg" : "Seu nome deve ser inserido!" }';
		}
		
		if (!phone) {
			return '{ "ok" : 0, "msg" : "Telefone deve ser informado!" }';
		}		
				
		if (!cpf) {
			return '{ "ok" : 0, "msg" : "CPF/CNPJ deve ser informado!" }';
		}
		
		if (!email) {
			return '{ "ok" : 0, "msg" : "E-mail deve ser informado!" }';
		}
		
		if (!address) {
			return '{ "ok" : 0, "msg" : "Endereço deve ser informado!" }';
		}
		
		
		if (!username) {
			return '{ "ok" : 0, "msg" : "Nome de usuário deve ser informado!!" }';
		}
		
		if (!password) {
			return '{ "ok" : 0, "msg" : "Senha deve ser informada!" }';			
		} else if (password != passwordRepeat) {
			return '{ "ok" : 0, "msg" : "Senhas não coincidem!" }';	
		}

		if (password.length < 6) {
			return '{ "ok" : 0, "msg" : "Senha deve conter pelo menos 6 caracteres!" }';
		} 
		
		return;
	}

	return this;
}