
var crypto 		= require('crypto');
var mongo 	= require('mongodb');
var Server 		= require('mongodb').Server;
var moment 		= require('moment');

/*
	ESTABLISH DATABASE CONNECTION
*/
var collection;

var db = new mongo.Db('decasa', 
    new mongo.Server(process.env.OPENSHIFT_MONGODB_DB_HOST || "localhost",
     process.env.OPENSHIFT_MONGODB_DB_PORT || 27017)).open(

function(err, client){    
    if (err) {
        status = "open..." + err;
        console.log("ERRO: " + err);
    } else {
        client.authenticate(process.env.OPENSHIFT_MONGODB_DB_USERNAME,
        process.env.OPENSHIFT_MONGODB_DB_PASSWORD, 

        function(err, res) {
            if (err) {
                status = "auth..." + err;
                console.log("ERRO DE AUTENTICAÇÃO: " + err);
            } else { 
                console.log("SUCESSO!");
                client.collection("professional", function(err, professionalCollection){
                    if (err){
                        status = "collection..." + err;
                        console.log("ERRO: " + err);
                    } else {
                        status = "ok";
                        collection = professionalCollection;
                    }
                });
           }
      });
    }
});

/* login validation methods */

exports.autoLogin = function(email, password, callback)
{
	collection.findOne({email:email}, function(e, o) {
		if (o){
			o.password == password ? callback(o) : callback(null);
		}	else{
			callback(null);
		}
	});
}

exports.manualLogin = function(email, password, callback)
{
	collection.findOne({email:email}, function(e, o) {
		if (o == null){
			callback('usuário não encontrado!');
		}	else{
			//validatePassword(pass, o.pass, function(err, res) {
				if (password == o.password){
					callback(null, o);
				}	else{
					callback('Senha inválida!');
				}
			//});
		}
	});
}

/* record insertion, update & deletion methods */

exports.addNewAccount = function(newData, callback)
{
	collection.findOne({user:newData.user}, function(e, o) {
		if (o){
			callback('username-taken');
		}	else{
			collection.findOne({email:newData.email}, function(e, o) {
				if (o){
					callback('email-taken');
				}	else{
					saltAndHash(newData.pass, function(hash){
						newData.pass = hash;
					// append date stamp when record was created //
						newData.date = moment().format('MMMM Do YYYY, h:mm:ss a');
						collection.insert(newData, {safe: true}, callback);
					});
				}
			});
		}
	});
}

exports.updateAccount = function(newData, callback)
{
	collection.findOne({_id:getObjectId(newData.id)}, function(e, o){
		o.name 		= newData.name;
		o.email 	= newData.email;
		o.country 	= newData.country;
		if (newData.pass == ''){
			collection.save(o, {safe: true}, function(e) {
				if (e) callback(e);
				else callback(null, o);
			});
		}	else{
			saltAndHash(newData.pass, function(hash){
				o.pass = hash;
				collection.save(o, {safe: true}, function(e) {
					if (e) callback(e);
					else callback(null, o);
				});
			});
		}
	});
}

exports.updatePassword = function(email, newPass, callback)
{
	collection.findOne({email:email}, function(e, o){
		if (e){
			callback(e, null);
		}	else{
			saltAndHash(newPass, function(hash){
		        o.pass = hash;
		        collection.save(o, {safe: true}, callback);
			});
		}
	});
}

/* account lookup methods */

exports.deleteAccount = function(id, callback)
{
	collection.remove({_id: getObjectId(id)}, callback);
}

exports.getAccountByEmail = function(email, callback)
{
	collection.findOne({email:email}, function(e, o){ callback(o); });
}

exports.validateResetLink = function(email, passHash, callback)
{
	collection.find({ $and: [{email:email, pass:passHash}] }, function(e, o){
		callback(o ? 'ok' : null);
	});
}

exports.getAllRecords = function(callback)
{
	collection.find().toArray(
		function(e, res) {
		if (e) callback(e)
		else callback(null, res)
	});
}

exports.delAllRecords = function(callback)
{
	collection.remove({}, callback); // reset collection collection for testing //
}

/* private encryption & validation methods */

var generateSalt = function()
{
	var set = '0123456789abcdefghijklmnopqurstuvwxyzABCDEFGHIJKLMNOPQURSTUVWXYZ';
	var salt = '';
	for (var i = 0; i < 10; i++) {
		var p = Math.floor(Math.random() * set.length);
		salt += set[p];
	}
	return salt;
}

var md5 = function(str) {
	return crypto.createHash('md5').update(str).digest('hex');
}

var saltAndHash = function(pass, callback)
{
	var salt = generateSalt();
	callback(salt + md5(pass + salt));
}

var validatePassword = function(plainPass, hashedPass, callback)
{
	var salt = hashedPass.substr(0, 10);
	var validHash = salt + md5(plainPass + salt);
	callback(null, hashedPass === validHash);
}

var getObjectId = function(id)
{
	return new require('mongodb').ObjectID(id);
}

var findById = function(id, callback)
{
	collection.findOne({_id: getObjectId(id)},
		function(e, res) {
		if (e) callback(e)
		else callback(null, res)
	});
}

var findByMultipleFields = function(a, callback)
{
// this takes an array of name/val pairs to search against {fieldName : 'value'} //
	collection.find( { $or : a } ).toArray(
		function(e, results) {
		if (e) callback(e)
		else callback(null, results)
	});
}
