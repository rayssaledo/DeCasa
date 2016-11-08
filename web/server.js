var express = require("express");
var app = express();
var mongo = require("mongodb");
var bodyParser = require("body-parser");
var cons = require('consolidate');
var path = require('path');
var session = require('express-session');
var MongoStore = require('connect-mongo')(session);

app.engine('html', cons.swig);
app.set('view engine','html');
app.set('views',__dirname+'/views'); 
app.use(express.static(path.join(__dirname, 'public')));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({
    extended:true
}));

app.use(function(req, res, next) {
	res.header('Access-Control-Allow-Origin', '*'); //https://sandbox.pagseguro.uol.com.br
	res.header('Access-Control-Allow-Methods', 'PUT, GET, POST, DELETE, OPTIONS');
	res.header('Access-Control-Allow-Headers', 'Content-Type');
	next();
});

var status ;

// build mongo database connection url //

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
                        require("./models/professional")(mongo, app, professionalCollection);
                    }
                });
				client.collection("user", function(err, userCollection){
                    if (err){
                        status = "collection..." + err;
                        console.log("ERRO: " + err);
                    } else {
                        status = "ok";
                        require("./models/user")(mongo, app, userCollection);
                    }
                });
           }
        });
    }
});

app.use(
    session({
    secret: 'faeb4453e5d14fe6f6d04637f78077c76c73d1b4',
    proxy: true,
    resave: true,
    saveUninitialized: true,
    store: db
    })
);

app.listen(process.env.OPENSHIFT_NODEJS_PORT || 8082, process.env.OPENSHIFT_NODEJS_IP || "localhost");