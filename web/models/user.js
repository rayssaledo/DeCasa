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
		var favorites = [];
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
					res.json({ "ok" : 0, "msg" : "User already exists." });
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
						favorites: favorites,
						username: username,
						password: password
						
					}, function (err, doc) {
						if(err){
							res.json({ "ok" : 0, "msg" : err });
						} else {
							res.json({"ok": 1, "msg": "Success on register!"});	
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
					res.json({ "ok" : 0, "msg" : "User not found!" });
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
							res.json({"ok": 1, "msg": "Success on update!"});	
						}
					});
				}
			});
		}
	});
	
	app.post("/add-favorite", function(req, res) {
		var username = req.body.username;
		var name = req.body.nameProfessional;
		var businessName = req.body.businessNameProfessional;
		var cpf = req.body.cpfProfessional;
		var phone1 = req.body.phoneProfessional1;
		var phone2 = req.body.phoneProfessional2;
		var phone3 = req.body.phoneProfessional3;
		var phone4 = req.body.phoneProfessional4;
		var street = req.body.streetProfessional;
		var number = req.body.numberProfessional;
		var neighborhood = req.body.neighborhoodProfessional;
		var city = req.body.cityProfessional;
		var state = req.body.stateProfessional;					
		var site = req.body.siteProfessional;		
		var socialNetwork1 = req.body.socialNetworkProfessional1;
		var socialNetwork2 = req.body.socialNetworkProfessional2;
		var service = req.body.serviceProfessional;	
		var description = req.body.descriptionProfessional;
		var email = req.body.emailProfessional;	
		var avg = req.body.avgProfessional;	
		var plan = req.body.plan;
		var picture = req.body.picture;
		
		if (!username) {
			res.json({"ok": 0, "msg": "Username not informed!"});
		} else {
			userCollection.find({
				username: username
				
			}).toArray(function(err, array){
				if (array.length == 0) {
					res.json({ "ok" : 0, "msg" : "User not found!" });
				} else {										
					userCollection.update({						
						username: username						
					},
					{ $push: { 
						favorites : {								
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
							avg: avg,
							email: email,	
							picture: picture,
							plan : plan
			
						} 
					}							
					}, function (err, doc) {
						if(err){
							res.json({ "ok" : 0, "msg" : err });
						} else {
							res.json({"ok": 1, "msg": "Success!"});	
						}
					});
					
				}
			});
		}	
		
	});
	
	

	app.post("/remove-favorite", function(req, res) {
		var username = req.body.username;				
		var email = req.body.emailProfessional;	
		var service = req.body.serviceProfessional;
		
		if (!username) {
			res.json({"ok": 0, "msg": "Username not informed!"});
		} else {
			userCollection.find({
				username: username
				
			}).toArray(function(err, array){
				if (array.length == 0) {
					res.json({ "ok" : 0, "msg" : "User not found!" });
				} else {				
					
					userCollection.update({						
						username: username						
					},
					{
						$pull : {
							"favorites": {email : email, service: service}
						}
					
					}, function (err, doc) {
						if(err){
							res.json({ "ok" : 0, "msg" : err });
						} else {
							res.json({"ok": 1, "msg": "Success!"});	
						}
					});
					
				}
			});
		}	
		
	});
	
	app.get("/get-is-favorite", function(req, res) {
		var username = req.query.username;
		var email = req.query.emailProfessional;
		var service = req.query.serviceProfessional;
				
		if (!username) {
			res.json({"ok": 0, "msg": "Username not informed!"});
		}  else if (!email) {
			res.json({"ok": 0, "msg": "Email not informed!"});
		} else {
			
			userCollection.find({
					username: username,
					"favorites.email" : email,
					"favorites.service": service
				}).toArray(function(err, array){
					if (array.length == 1) {
						res.json({ "ok" : 1, "msg" : "Professional is on the list." });
					} else {
						res.json({ "ok" : 0, "msg" : "Professional is not on the list." });	
					}
				});
			
		}	
	});
	
	
	app.get("/get-favorites-user-by-service", function (req, res) {
		var username = req.query.username;
		var service = req.query.service;
		
		 if (!username) {
			res.json({"ok": 0, "msg": "Username must be informed!"});
		} else if (!service) {
			res.json({"ok": 0, "msg": "Service must be informed!"});
		} else {
			userCollection.find({
				username: username
				
			}).toArray(function(err, array){
				if (array.length < 1) {
					res.json({ "ok" : 0, "msg" : "User not found!" });
				} else {					
					
					userCollection.aggregate( [ 
							{ $match: { username: username } },
							{ $unwind: '$favorites' },
							{ $match: { 'favorites.service': service } },
							{ $group: { _id: '$_id', list: { $push: '$favorites' } } }
					   ], function(err, result) {
						   if (err){
							   res.json({ "ok" : 0, "msg" : err });
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
						res.json({ "ok" : 0, "msg" : "User or password invalid!" });
					} else {
						res.json({ "ok" : 1, "msg" : "User already exists." });
					}
				}
				
			});
		}
		
	});
		
	app.get("/get-user", function(req, res) {
		var username = req.query.username;
				
		if (!username) {
			res.json({"ok": 0, "msg": "Username not informed!"});
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
					res.json({ "ok" : 0, "msg" : "None user registred!" });
				} else {
					res.json({ "ok" : 1, "result" : array });
				}
			}
			
		});
	});	
	
	function checkInconsistency(name, birthDate, gender, street, number, neighborhood, city, state, username, password) {
		if (!name) {
			return '{ "ok" : 0, "msg" : "Name not informed!" }';
		}
		
		if (!birthDate) {
			return '{ "ok" : 0, "msg" : "Birthdate not informed!" }';
		}
		
		if (!gender) {
			return '{ "ok" : 0, "msg" : "Gender not informed!" }';
		}
		
		if (!street) {
			return '{ "ok" : 0, "msg" : "Street not informed!" }';
		}
				
		
		if (!number) {
			return '{ "ok" : 0, "msg" : "Number not informed!" }';
		}
		
		if (!neighborhood) {
			return '{ "ok" : 0, "msg" : "Neighborhood not informed!" }';
		}
		
		if (!city) {
			return '{ "ok" : 0, "msg" : "City not informed!" }';
		}
		
		if (!state) {
			return '{ "ok" : 0, "msg" : "State not informed!" }';
		}
		
		if (!username) {
			return '{ "ok" : 0, "msg" : "Username not informed!" }';
		}
		
		if (!password) {
			return '{ "ok" : 0, "msg" : "Password not informed!" }';
		}
		
	}
	
	function checkInconsistencyUsername(username, password) {
		if (!username) {
			return '{ "ok" : 0, "msg" : "Username not informed!" }';
		}
		
		if (!password) {
			return '{ "ok" : 0, "msg" : "Password not informed!" }';	
		}
	}

	return this;
}