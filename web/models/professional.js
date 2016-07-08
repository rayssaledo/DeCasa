module.exports = function(mongodb, app, professionalCollection) {

	app.post("/addProfessional", function (req, res){
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
		
		var inconsistency = checkInconsistency(cpf, businessName, email, address, phone, username, password);

		if (inconsistency) {
			res.send(inconsistency);
		} else {
			professionalCollection.find({
				username: username,
				password: password
				
			}).toArray(function(err, array){
				if (array.length > 0) {
					res.json({ "ok" : 0, "msg" : "Professional already registered!" });
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
							res.json({"ok": 1, "msg": "Success on register!"});	
						}
					});
				}
			});
		}
	});
	
	app.post("/checkusername", function(req, res) {
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
						res.json({ "ok" : 0, "msg" : "Professional not registered!" });
					} else {
						res.json({ "ok" : 1, "msg" : "Professional registered!" });
					}
				}
				
			});
		}
		
	});
	
	app.get("/getProfessional", function(req, res) {
		var username = req.query.username;
				
		if (!username) {
			res.json({"ok": 0, "msg": "username must be informed!"});
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
	
	app.get("/getAllProfessionals", function(req, res){
		professionalCollection.find({}).toArray(function(err, array){
			if(err) {
				res.json({ "ok" : 0, "msg" : err });
			} else {					
				if(array.length < 1) {						
					res.json({ "ok" : 0, "msg" : "Empty list of professionals!" });
				} else {
					res.json({ "ok" : 1, "result" : array });
				}
			}			
		});
	});	
	
	
	function checkInconsistencyusername(username, password) {
		if (!username) {
			return '{ "ok" : 0, "msg" : "Username must be informed!" }';
		}
		
		if (!password) {
			return '{ "ok" : 0, "msg" : "Password must be informed!" }';	
		}
	}
	
	function checkInconsistency(cpf, businessName, email, address, phone, username, password) {
		if (!cpf) {
			return '{ "ok" : 0, "msg" : "CPF/CNPJ must be informed!" }';
		}
		
		if (!businessName) {
			return '{ "ok" : 0, "msg" : "Business name must be informed!" }';
		}
		
		if (!email) {
			return '{ "ok" : 0, "msg" : "E-mail name must be informed!" }';
		}
		
		if (!address) {
			return '{ "ok" : 0, "msg" : "Address must be informed!" }';
		}
		
		if (!phone) {
			return '{ "ok" : 0, "msg" : "Phone number must be informed!" }';
		}		
		
		if (!username) {
			return '{ "ok" : 0, "msg" : "Username number must be informed!" }';
		}
		
		if (!password) {
			return '{ "ok" : 0, "msg" : "Password must be informed!" }';			
		}

		if (password.length < 6) {
			return '{ "ok" : 0, "msg" : "Password must contain at least 6 characters!" }';
		} 
		
		return;
	}

	return this;
}