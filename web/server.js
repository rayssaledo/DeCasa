var express = require("express");
var app = express();
var mongo = require("mongodb");
var bodyParser = require("body-parser");
 
 
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({
    extended:true
}));

var status ;

new mongo.Db("decasa", new mongo.Server(process.env.OPENSHIFT_MONGODB_DB_HOST || "localhost", process.env.OPENSHIFT_MONGODB_DB_PORT || 27017)).open(function(err, client){
    if (err) {
		status = "open..." + err;
        console.log("ERRO: " + err);
    } else {
        /*client.authenticate(process.env.OPENSHIFT_MONGODB_DB_USERNAME, process.env.OPENSHIFT_MONGODB_DB_PASSWORD, function(err, res) {
            
			if (err) {
				status = "auth..." + err;
                console.log("ERRO DE AUTENTICAÇÃO: " + err);
            } else {*/
                console.log("SUCESSO!");
                client.collection("professional", function(err, professionalCollection){
                    if (err){
						status = "collection..." + err;
                        console.log("ERRO: " + err);
                    } else {
						status = "ok";
						require("./models/professional")(mongo, app, professionalCollection);
                    }
                });
           /* }
        });*/
    }
});
 
app.get("/", function(req, res) {
    res.send("Hello world with status: " + status);
});
 
app.listen(process.env.OPENSHIFT_NODEJS_PORT || 8080, process.env.OPENSHIFT_NODEJS_IP || "localhost");