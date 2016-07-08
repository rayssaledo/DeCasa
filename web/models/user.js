module.exports = function(mongodb, app, userCollection) {

	app.post("/addUser", function (req, res){
		var login = req.body.login;
		var pass = req.body.pass;
		var name = req.body.name;
		var state = req.body.state;
		var city = req.body.city;
		var birthDate= req.body.birthDate;
		var gender = req.body.gender;
		var bloodType = req.body.bloodType;
		var lastDonation = req.body.lastDonation;
		var canDonate = req.body.canDonate;
		
		var inconsistency = checkInconsistency(login, pass, name, state, city, birthDate);

		if (inconsistency) {
			res.send(inconsistency);
		} else {
			userCollection.find({
				login: login,
				pass: pass
				
			}).toArray(function(err, array){
				if (array.length > 0) {
					res.json({ "ok" : 0, "msg" : "Usuário já cadastrado" });
				} else {
					
					userCollection.insert({
						login: login,
						pass: pass,
						name: name,
						state: state,
						city: city,
						birthDate: birthDate,
						gender: gender,
						bloodType: bloodType,
						lastDonation: lastDonation,
						canDonate: canDonate
						
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
	
	app.post("/checkLogin", function(req, res) {
		var login = req.body.login;
		var pass = req.body.pass;
		
		var inconsistency = checkInconsistencyLogin(login, pass);
		
		if (inconsistency) {
			res.send(inconsistency);
		} else {
			userCollection.find({
				login: login,
				pass: pass
				
			}).toArray(function(err, array){
				if(err) {
					res.json({ "ok" : 0, "msg" : err });
				} else {					
					if(array.length < 1) {						
						res.json({ "ok" : 0, "msg" : "Usuário não é cadastrado" });
					} else {
						res.json({ "ok" : 1, "msg" : "Usuário é cadastrado" });
					}
				}
				
			});
		}
		
	});
	
	app.get("/getUser", function(req, res) {
		var login = req.query.login;
				
		if (!login) {
			res.json({"ok": 0, "msg": "Login do usuário não informado!"});
		} else {
			userCollection.findOne({
				login: login
				
			}, function(err, doc){
				if(err){
					res.json({ "ok" : 0, "msg" : err });
				} else {
					res.json({"ok": 1, "result": doc});	
				}
			});
		}
		
	});
	
	app.get("/getAllUsers", function(req, res){
		userCollection.find({}).toArray(function(err, array){
			if(err) {
				res.json({ "ok" : 0, "msg" : err });
			} else {					
				if(array.length < 1) {						
					res.json({ "ok" : 0, "msg" : "Nenhum usuário cadastrado!" });
				} else {
					res.json({ "ok" : 1, "result" : array });
				}
			}
			
		});
	});	
	
	app.post("/updateLastDonation", function(req, res) {
		var login = req.body.login;
		var lastDonation = req.body.lastDonation;
		
		if (!login) {
			res.json({"ok": 0, "msg": "Login do usuário não informado!"});
		} else if (!lastDonation) {		
			res.json({"ok": 0, "msg": "Data não informada!"});
		} else {
			userCollection.find({
				login: login
				
			}).toArray(function(err, array){
				if(err) {
					res.json({ "ok" : 0, "msg" : err });
				} else {					
					if(array.length < 1) {						
						res.json({ "ok" : 0, "msg" : "Usuário não encontrado!" });
					} else {
						userCollection.update({
							login: login
						},
						{ $set: {"lastDonation": lastDonation}
						
						},function(err, doc){
							if(err){
								res.json({"ok" : 0, "msg" : err });
							} else {
								res.json({"ok": 1, "msg": "Data atualizada!"});	
								
							}
						});
					}
				}				
			});
		}		
	});
	
	app.get("/getUsersByBloodType", function(req, res){		
		var bloodType = req.query.bloodType;
		
		if (!bloodType) {
			res.json({"ok": 0, "msg": "Tipo de sangue não informado!"});
		} else {
			userCollection.find({
				bloodType: bloodType
			}).toArray(function(err, array){
				if(err) {
					res.json({ "ok" : 0, "msg" : err });
				} else {					
					if(array.length < 1) {						
						res.json({ "ok" : 0, "msg" : "Nenhum usuário com o tipo "+ bloodType +" cadastrado!" });
					} else {
						res.json({ "ok" : 1, "result" : array });
					}
				}
				
			});
		}
		
	});	
	
	app.post("/addForm", function(req, res){
		var login = req.body.login;
		var patientName = req.body.patientName;
		var hospitalName = req.body.hospitalName;
		var city = req.body.city;
		var state = req.body.state;
		var bloodType = req.body.bloodType;
		var deadline = req.body.deadline;
		
		var inconsistency = checkInconsistencyForm(login, patientName, hospitalName, city, state, bloodType, deadline);
		
		if (inconsistency) {
			res.send(inconsistency);
		} else {
			userCollection.find({
				login: login
			}).toArray(function(err, array){
				if(err) {
					res.json({ "ok" : 0, "msg" : err });
				} else {					
					if(array.length < 1) {						
						res.json({ "ok" : 0, "msg" : "Usuário não encontrado!"});
					} else {
						userCollection.update({
							login: login
						},
						{ 
							$push: { 
								forms: {								
									patientName: patientName, 
									hospitalName: hospitalName, 
									city: city, 
									state: state,
									bloodType: bloodType, 
									deadline: deadline								
								} 
							}	
						},function(err, doc){
							if(err){
								res.json({"ok" : 0, "msg" : err });
							} else {
								res.json({"ok": 1, "msg": "Form Criado!"});	
								
							}
						});
					}
				}
				
			});			
		}
	});		
		
	function checkInconsistencyForm(login, patientName, hospitalName, city, state, bloodType, deadline) {
		if (!login) {
			return '{ "ok" : 0, "msg" : "Login do usuário não informado!" }';
		}
		
		if (!patientName) {
			return '{ "ok" : 0, "msg" : "Nome do paciente não informado!" }';
		}
		
		if (!hospitalName) {
			return '{ "ok" : 0, "msg" : "Hospital não informado!" }';
		}
		
		if (!city) {
			return '{ "ok" : 0, "msg" : "Cidade não informada!" }';
		}
		
		if (!state) {
			return '{ "ok" : 0, "msg" : "Estado não informado!" }';
		}
		
		if (!bloodType) {
			return '{ "ok" : 0, "msg" : "Tipo de sangue não informado!" }';
		}
		
		if (!deadline) {
			return '{ "ok" : 0, "msg" : "Data limite não informada!" }';
		}
		
	}
	
	
	function checkInconsistencyLogin(login, pass) {
		if (!login) {
			return '{ "ok" : 0, "msg" : "Login do usuário não informado!" }';
		}
		
		if (!pass) {
			return '{ "ok" : 0, "msg" : "Senha não informada!" }';	
		}
	}
	
	function checkInconsistency(login, pass, name, state, city, birthDate) {
		if (!login) {
			return '{ "ok" : 0, "msg" : "Login do usuário não informado!" }';
		}
		
		if (!pass) {
			return '{ "ok" : 0, "msg" : "Senha não informada!" }';			
		}

		if (pass.length < 6) {
			return '{ "ok" : 0, "msg" : "A senha deve conter pelo menos 6 caracteres!" }';
		} 
		
		if (!name) {
			return '{ "ok" : 0, "msg" : "Nome do usuário não informado!" }';
		}
		
		if (!state) {
			return '{ "ok" : 0, "msg" : "Estado não infomado!" }';
		}
			
		if (!city) {
			return '{ "ok" : 0, "msg" : "Cidade não infomada!" }';
		}
			
		if (!birthDate) {
			return '{ "ok" : 0, "msg" : "Data de nascimento não informada!" }';
		}
		return;
	}

	return this;
}