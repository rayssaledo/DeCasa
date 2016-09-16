module.exports = function(mongodb, app, userCollection) {

	app.post("/add-user", function (req, res){
		var name = req.body.name;
		var birthDate= req.body.birthDate;
		var gender = req.body.gender;
		var street = req.body.street;
		var number = req.body.number;
		var neighborhood= req.body.neighborhood;
		var city = req.body.city;
		var state = req.body.state;
		var photo = req.body.photo;
		var favoritesElectricians = [];
		var favoritesPlumbers = [];
		var favoritesFitters = [];
		var username = req.body.username;
		var password = req.body.password;
		
		var inconsistency = checkInconsistency(name, birthDate, gender, street, number, neighborhood, city, state, username, password);

		if (inconsistency) {
			res.send(inconsistency);
		} else {
			userCollection.find({
				username: username,
				password: password
				
			}).toArray(function(err, array){
				if (array.length > 0) {
					res.json({ "ok" : 0, "msg" : "Usuário já cadastrado" });
				} else {
					
					userCollection.insert({
						name: name,
						birthDate: birthDate,
						gender: gender,
						street: street,
						number: number,
						neighborhood: neighborhood,
						city: city,
						state: state,
						photo: photo,
						favoritesElectricians: favoritesElectricians,
						favoritesPlumbers: favoritesPlumbers,
						favoritesFitters : favoritesFitters,
						username: username,
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
		}
	});
	
	app.post("/update-user", function (req, res){
		var name = req.body.name;
		var birthDate= req.body.birthDate;
		var gender = req.body.gender;
		var street = req.body.street;
		var number = req.body.number;
		var neighborhood= req.body.neighborhood;
		var city = req.body.city;
		var state = req.body.state;
		var photo = req.body.photo;
		
		var username = req.body.username;
		var password = req.body.password;
		
		var inconsistency = checkInconsistency(name, birthDate, gender, street, number, neighborhood, city, state, username, password);

		if (inconsistency) {
			res.send(inconsistency);
		} else {
			userCollection.find({
				username: username
				
			}).toArray(function(err, array){
				if (array.length == 0) {
					res.json({ "ok" : 0, "msg" : "Usuário não encontrado!" });
				} else {
					
					userCollection.update({						
						username: username						
					},
					{ $set: {
						name: name,
						birthDate: birthDate,
						gender: gender,
						street: street,
						number: number,
						neighborhood: neighborhood,
						city: city,
						state: state,
						photo: photo,						
						password: password
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
		}
	});
	
	app.post("/add-favorite", function(req, res) {
		var username = req.body.username;
		var name = req.body.nameProfessional;
		var cpf = req.body.cpfProfessional;
		var phone = req.body.phoneProfessional;
		var street = req.body.streetProfessional;
		var number = req.body.numberProfessional;
		var neighborhood = req.body.neighborhoodProfessional;
		var city = req.body.cityProfessional;
		var state = req.body.stateProfessional;					
		var site = req.body.siteProfessional;		
		var socialNetwork = req.body.socialNetworkProfessional;
		var service = req.body.serviceProfessional;					
		var email = req.body.emailProfessional;	
		var avg = req.body.avgProfessional;	
		var services = req.body.servicesProfessional;
		
		if (!username) {
			res.json({"ok": 0, "msg": "Nome do usuário não informado!"});
		} else if (!service) {
			res.json({"ok": 0, "msg": "Serviço não informado!"});			
		} else {
			userCollection.find({
				username: username
				
			}).toArray(function(err, array){
				if (array.length == 0) {
					res.json({ "ok" : 0, "msg" : "Usuário não encontrado!" });
				} else {
					console.log(service);
					
					if (service == "Electrician") {
						userCollection.update({						
							username: username						
						},
						{ $push: { 
									favoritesElectricians : {								
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
										service: service,	
										email: email,
										avg: avg,
										services: services
						
									} 
								}							
						}, function (err, doc) {
							if(err){
								res.json({ "ok" : 0, "msg" : err });
							} else {
								res.json({"ok": 1, "msg": "inserido com sucesso!"});	
							}
						});
					} else if (service == "Plumber") {
						userCollection.update({						
							username: username						
						},
						{ $push: { 
									favoritesPlumbers : {								
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
										service: service,	
										email: email,
										avg: avg,
										services: services
									} 
								}							
						}, function (err, doc) {
							if(err){
								res.json({ "ok" : 0, "msg" : err });
							} else {
								res.json({"ok": 1, "msg": "inserido com sucesso!"});	
							}
						});
					} else if (service == "Fitter") {
						userCollection.update({						
							username: username						
						},
						{ $push: { 
									favoritesFitters : {								
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
										service: service,	
										email: email,
										avg: avg,
										services: services
									} 
								}							
						}, function (err, doc) {
							if(err){
								res.json({ "ok" : 0, "msg" : err });
							} else {
								res.json({"ok": 1, "msg": "inserido com sucesso!"});	
							}
						});
					}
				}
			});
		}	
		
	});
	
	

	app.post("/remove-favorite", function(req, res) {
		var username = req.body.username;				
		var email = req.body.emailProfessional;	
		var service = req.body.serviceProfessional;
		
		if (!username) {
			res.json({"ok": 0, "msg": "Nome do usuário não informado!"});
		} else {
			userCollection.find({
				username: username
				
			}).toArray(function(err, array){
				if (array.length == 0) {
					res.json({ "ok" : 0, "msg" : "Usuário não encontrado!" });
				} else {
					
					if (service == "Electrician") {
						userCollection.update({						
							username: username						
						},
						{
							$pull : {
								"favoritesElectricians": {email : email}
							}
						
						}, function (err, doc) {
							if(err){
								res.json({ "ok" : 0, "msg" : err });
							} else {
								res.json({"ok": 1, "msg": "Removido com sucesso!"});	
							}
						});
					} else if (service == "Plumber") {
						userCollection.update({						
							username: username						
						},
						{
							$pull : {
								"favoritesPlumbers": {email : email}
							}
						
						}, function (err, doc) {
							if(err){
								res.json({ "ok" : 0, "msg" : err });
							} else {
								res.json({"ok": 1, "msg": "Removido com sucesso!"});	
							}
						});
					} else if (service == "Fitter") {
						userCollection.update({						
							username: username						
						},
						{
							$pull : {
								"favoritesFitters": {email : email}
							}
						
						}, function (err, doc) {
							if(err){
								res.json({ "ok" : 0, "msg" : err });
							} else {
								res.json({"ok": 1, "msg": "Removido com sucesso!"});	
							}
						});
					}
				}
			});
		}	
		
	});
	
	app.get("/get-is-favorite", function(req, res) {
		var username = req.query.username;
		var email = req.query.emailProfessional;
		var service = req.query.serviceProfessional;
				
		if (!username) {
			res.json({"ok": 0, "msg": "Nome do usuário não informado!"});
		}  else if (!email) {
			res.json({"ok": 0, "msg": "Email do profissional não informado!"});
		} else {
			
			if (service == "Electrician") {
				userCollection.find({
					username: username,
					"favoritesElectricians.email" : email
				}).toArray(function(err, array){
					if (array.length == 1) {
						res.json({ "ok" : 1, "msg" : "Profissional na lista de favorito" });
					} else {
						res.json({ "ok" : 0, "msg" : "Profissional não está na lista de favorito" });	
					}
				});
			} else if (service == "Plumber") {
				userCollection.find({
					username: username,
					"favoritesPlumbers.email" : email
				}).toArray(function(err, array){
					if (array.length == 1) {
						res.json({ "ok" : 1, "msg" : "Profissional na lista de favorito" });
					} else {
						res.json({ "ok" : 0, "msg" : "Profissional não está na lista de favorito" });	
					}
				});
			} else if (service == "Fitter") {
				userCollection.find({
					username: username,
					"favoritesFitters.email" : email
				}).toArray(function(err, array){
					if (array.length == 1) {
						res.json({ "ok" : 1, "msg" : "Profissional na lista de favorito" });
					} else {
						res.json({ "ok" : 0, "msg" : "Profissional não está na lista de favorito" });	
					}
				});
			}
			
		}	
	});
	
	
	app.get("/get-favoritesElectricians-user", function (req, res) {
		var username = req.query.username;
		
		 if (!username) {
			res.json({"ok": 0, "msg": "Nome de usuário não informado!"});
		} else {
			userCollection.find({
				username: username
				
			}, {
				_id: 0,
				favoritesElectricians: 1
				
			}).toArray(function(err, array){
				if (array.length < 1) {
					res.json({ "ok" : 0, "msg" : "Nenhum usuário encontrado!" });
				} else {					
					
					userCollection.aggregate(
					   [ { $match : { username : username } },
						  {
							 $project: {
								_id: 0,
								favoritesElectricians: 1
							 }
						  }
					   ], function(err, result) {
						   if (err){
							   res.json({ "ok" : 0, "msg" : "Erro Inesperado!" });
						   } else {							   
							   res.json({ "ok" : 1, "result" : result });
						   }
					   }
					);
				}
			});			
		}			
	});
	
	app.get("/get-favoritesPlumbers-user", function (req, res) {
		var username = req.query.username;
		
		 if (!username) {
			res.json({"ok": 0, "msg": "Nome de usuário não informado!"});
		} else {
			userCollection.find({
				username: username
				
			}, {
				_id: 0,
				favoritesPlumbers: 1
				
			}).toArray(function(err, array){
				if (array.length < 1) {
					res.json({ "ok" : 0, "msg" : "Nenhum usuário encontrado!" });
				} else {					
					
					userCollection.aggregate(
					   [ { $match : { username : username } },
						  {
							 $project: {
								_id: 0,
								favoritesPlumbers: 1
							 }
						  }
					   ], function(err, result) {
						   if (err){
							   res.json({ "ok" : 0, "msg" : "Erro Inesperado!" });
						   } else {							   
							   res.json({ "ok" : 1, "result" : result });
						   }
					   }
					);
				}
			});			
		}			
	});
	
	app.get("/get-favoritesFitters-user", function (req, res) {
		var username = req.query.username;
		
		 if (!username) {
			res.json({"ok": 0, "msg": "Nome de usuário não informado!"});
		} else {
			userCollection.find({
				username: username
				
			}, {
				_id: 0,
				favoritesFitters: 1
				
			}).toArray(function(err, array){
				if (array.length < 1) {
					res.json({ "ok" : 0, "msg" : "Nenhum usuário encontrado!" });
				} else {					
					
					userCollection.aggregate(
					   [ { $match : { username : username } },
						  {
							 $project: {
								_id: 0,
								favoritesFitters: 1
							 }
						  }
					   ], function(err, result) {
						   if (err){
							   res.json({ "ok" : 0, "msg" : "Erro Inesperado!" });
						   } else {							   
							   res.json({ "ok" : 1, "result" : result });
						   }
					   }
					);
				}
			});			
		}			
	});
	
	app.post("/check-login", function(req, res) {
		var username = req.body.username;
		var password = req.body.password;
		
		var inconsistency = checkInconsistencyUsername(username, password);
		
		if (inconsistency) {
			res.send(inconsistency);
		} else {
			userCollection.find({
				username: username,
				password: password
				
			}).toArray(function(err, array){
				if(err) {
					res.json({ "ok" : 0, "msg" : err });
				} else {					
					if(array.length < 1) {						
						res.json({ "ok" : 0, "msg" : "Usuário ou senha inválida!" });
					} else {
						res.json({ "ok" : 1, "msg" : "Usuário é cadastrado" });
					}
				}
				
			});
		}
		
	});
		
	app.get("/get-user", function(req, res) {
		var username = req.query.username;
				
		if (!username) {
			res.json({"ok": 0, "msg": "Nome do usuário não informado!"});
		} else {
			userCollection.findOne({
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
	
	app.get("/get-all-users", function(req, res){
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
	
	function checkInconsistency(name, birthDate, gender, street, number, neighborhood, city, state, username, password) {
		if (!name) {
			return '{ "ok" : 0, "msg" : "Nome não informado!" }';
		}
		
		if (!birthDate) {
			return '{ "ok" : 0, "msg" : "Data de aniversário não informada!" }';
		}
		
		if (!gender) {
			return '{ "ok" : 0, "msg" : "Sexo não informado!" }';
		}
		
		if (!street) {
			return '{ "ok" : 0, "msg" : "Rua não informada!" }';
		}
				
		
		if (!number) {
			return '{ "ok" : 0, "msg" : "Número não informado!" }';
		}
		
		if (!neighborhood) {
			return '{ "ok" : 0, "msg" : "Bairro não informado!" }';
		}
		
		if (!city) {
			return '{ "ok" : 0, "msg" : "Cidade não informada!" }';
		}
		
		if (!state) {
			return '{ "ok" : 0, "msg" : "Estado não informado!" }';
		}
		
		if (!username) {
			return '{ "ok" : 0, "msg" : "Nome de usuário não informado!" }';
		}
		
		if (!password) {
			return '{ "ok" : 0, "msg" : "Senha não informada!" }';
		}
		
	}
	
	function checkInconsistencyUsername(username, password) {
		if (!username) {
			return '{ "ok" : 0, "msg" : "Nome de usuário não informado!" }';
		}
		
		if (!password) {
			return '{ "ok" : 0, "msg" : "Senha não informada!" }';	
		}
	}

	return this;
}