
$(function() {
	var email = getCookie('email');
	email = email.replace("%40", "@");
	
	if(email != undefined || email != null){
		$.ajax({ 
			url: '/get-profissional',
			type: "GET",
			data: {email:email},
			success: function(data) {
				if(data.ok == 0){
					console.log("Erro ao capturar os dados." + data.msg);
				}else {
					$(function(){
						document.getElementById("name").value = data.result.name;
						document.getElementById("cpf").value = data.result.cpf;
                        document.getElementById("phone").value = data.result.phone;
                        document.getElementById("neighborhood").value = data.result.neighborhood;
                        document.getElementById("street").value = data.result.street;
                        document.getElementById("number").value = data.result.number;
                        document.getElementById("site").value = data.result.site;
                        document.getElementById("socialNetwork").value = data.result.socialNetwork;
                        document.getElementById("city").value = data.result.city;
                        document.getElementById("state").value = data.result.state;
                        document.getElementById("email").value = data.result.email;
                        document.getElementById("picture").filename = data.result.picture;
                        document.getElementById("password").value = data.result.password;
                        document.getElementById("passwordRepeat").value = data.result.password;

                        loadServices(data.result.services)
					//	listServices(data.result.services)

						document.getElementById("tv_name").innerHTML = data.result.name;
						document.getElementById("tv_cpf").innerHTML = data.result.cpf;
						document.getElementById("tv_telefone").innerHTML = data.result.phone;
						document.getElementById("tv_bairro").innerHTML = data.result.neighborhood;
						document.getElementById("tv_rua").innerHTML = data.result.street;
						document.getElementById("tv_numero").innerHTML = data.result.number;
						document.getElementById("tv_site").innerHTML = data.result.site;
						document.getElementById("tv_rede-social").innerHTML = data.result.socialNetwork;
						document.getElementById("tv_all-servicos").innerHTML = data.result.services;
						document.getElementById("tv_email").innerHTML = data.result.email;
						document.getElementById("foto").innerHTML = "<img src=\"" +"uploads/" + data.result.picture +
						 "\" class=\"img-responsive img-circle\" alt=\"Foto\" height=130 width=130 align=\"right\"/>";
					});
				}	
						
			},
			error: function() {
						
			},
	});
		
	}

});

function loadServices(services) {
    var step;
    for (step = 0; step < services.length; step++) {
        if(services[step] == "Eletricista"){
            document.getElementById("ServiceElectrician").checked = true;
        }
        else if(services[step] == "Montador"){
            document.getElementById("ServiceFitter").checked = true;
        }
        else {
            document.getElementById("ServicePlumber").checked = true;
        }
    }
}

/*function listServices(services) {
    var i;
    for (i = 0; i < services.length; i++) {
		var service = services[i];
        if(typeof service != "undefined"){
            document.getElementById("tv_service_".concat(i)).innerHTML = services[i];
        }
    }
}*/
