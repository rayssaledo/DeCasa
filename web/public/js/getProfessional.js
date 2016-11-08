
$(function() {
	var email = getCookie('email');
	email = email.replace("%40", "@");
	
	if(email != undefined || email != null){
		$.ajax({ 
			url: '/get-professional',
			type: "GET",
			data: {email:email},
			success: function(data) {
				if(data.ok == 0){
					console.log("Erro ao capturar os dados." + data.msg);
				}else {
					$(function(){
                        setCookie('user',JSON.stringify(data.result),900000);
                        console.log("cookie user..." + getCookie('user'));
						console.log(data.result.plan);
						console.log(data.result);

						hideFormAd(data.result.plan);
						// Se estiver na tela de editar o anuncio
						
						if(document.getElementById("business_free")) {
							document.getElementById("business_free").value = data.result.businessName;
							document.getElementById("phone1_free").value = data.result.phone1;
							document.getElementById("description_free").innerHTML = data.result.description;
							 if (data.result.service == "Encanador") {
								$('select>option:eq(1)').attr('selected', true);
							} else if (data.result.service == "Montador") {
								$('select>option:eq(2)').attr('selected', true);
							} else if (data.result.service == "Eletricista") {
								$('select>option:eq(3)').attr('selected', true);
							}
						} if (document.getElementById("street_gold")){
							document.getElementById("business_gold").value = data.result.businessName;
							document.getElementById("phone1_gold").value = data.result.phone1;
							document.getElementById("description_gold").innerHTML = data.result.description;
							 if (data.result.service == "Encanador") {
								$('select>option:eq(1)').attr('selected', true);
							} else if (data.result.service == "Montador") {
								$('select>option:eq(2)').attr('selected', true);
							} else if (data.result.service == "Eletricista") {
								$('select>option:eq(3)').attr('selected', true);
							}
							document.getElementById("street_gold").value = data.result.street;
							document.getElementById("neighborhood_gold").value = data.result.neighborhood;
							document.getElementById("number_gold").value = data.result.number;
							document.getElementById("city_gold").value = data.result.city;
							document.getElementById("state_gold").value = data.result.state;
							document.getElementById("phone2_gold").value = data.result.phone2;
							document.getElementById("phone3_gold").value = data.result.phone3;
							document.getElementById("phone4_gold").value = data.result.phone4;
							document.getElementById("socialNetwork1_gold").value = data.result.socialNetwork1;
							document.getElementById("socialNetwork2_gold").value = data.result.socialNetwork2;
							document.getElementById("site_gold").value = data.result.site;
						} if (document.getElementById("street_silver")){
							document.getElementById("business_silver").value = data.result.businessName;
							document.getElementById("phone1_silver").value = data.result.phone1;
							document.getElementById("description_silver").innerHTML = data.result.description;
							 if (data.result.service == "Encanador") {
								$('select>option:eq(1)').attr('selected', true);
							} else if (data.result.service == "Montador") {
								$('select>option:eq(2)').attr('selected', true);
							} else if (data.result.service == "Eletricista") {
								$('select>option:eq(3)').attr('selected', true);
							}
							document.getElementById("street_silver").value = data.result.street;
							document.getElementById("neighborhood_silver").value = data.result.neighborhood;
							document.getElementById("number_silver").value = data.result.number;
							document.getElementById("city_silver").value = data.result.city;
							document.getElementById("state_silver").value = data.result.state;
							document.getElementById("phone2_silver").value = data.result.phone2;
							document.getElementById("phone3_silver").value = data.result.phone3;
							document.getElementById("socialNetwork1_silver").value = data.result.socialNetwork1;
							document.getElementById("site_silver").value = data.result.site;		
						} if (document.getElementById("business_bronze")){
							document.getElementById("business_bronze").value = data.result.businessName;
							document.getElementById("phone1_bronze").value = data.result.phone1;
							document.getElementById("description_bronze").innerHTML = data.result.description;
							 if (data.result.service == "Encanador") {
								$('select>option:eq(1)').attr('selected', true);
							} else if (data.result.service == "Montador") {
								$('select>option:eq(2)').attr('selected', true);
							} else if (data.result.service == "Eletricista") {
								$('select>option:eq(3)').attr('selected', true);
							}
							document.getElementById("phone2_bronze").value = data.result.phone2;
							document.getElementById("socialNetwork1_bronze").value = data.result.socialNetwork1;
						}
						else {
							document.getElementById("tv_name").innerHTML = data.result.name;
							document.getElementById("tv_cpf").innerHTML = data.result.cpf;
							document.getElementById("tv_email").innerHTML = data.result.email;
							if (data.result.plan == "gold"){
								try{
									document.getElementById("img_photo").innerHTML = "<img src=\"" + "uploads/" + data.result.picture +
									"\" class=\"img-responsive img-circle\" alt=\"Foto\" height=80% width=80% align=\"left\"/>";
								} catch(err){}
							}
							
						}	

						try{
							document.getElementById("plan_name").innerHTML = data.result.plan;
						} catch(err) {}
						
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

function hideFormAd(plan_name) {
	if (plan_name == "free"){
		 $("#form-gold").hide();
		 $("#form-silver").hide();
		 $("#form-bronze").hide();
	} else if (plan_name == "gold"){
		$("#form-free").hide();
		 $("#form-silver").hide();
		 $("#form-bronze").hide();
	}  else if (plan_name == "silver"){
		 $("#form-free").hide();
		 $("#form-gold").hide();
		 $("#form-bronze").hide();
	} else {
		$("#form-free").hide();
		 $("#form-gold").hide();
		 $("#form-silver").hide();
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

function setCookie(cname, cvalue, time) {
    var d = new Date();
    d.setTime(d.getTime() + time);
    var expires = "expires="+ d.toUTCString();
    document.cookie = cname + "=" + cvalue + "; " + expires;
}
