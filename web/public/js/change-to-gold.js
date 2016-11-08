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
					console.log(data.result);						
					document.getElementById("name").value = data.result.name;
					document.getElementById("name").readOnly = data.result.name != null;	
					
					document.getElementById("cpf").value = data.result.cpf;	
					document.getElementById("cpf").readOnly = data.result.cpf != null;
					
					document.getElementById("email").value = data.result.email;	
					document.getElementById("email").readOnly = data.result.email != null;
				
					document.getElementById("password").value = data.result.password;
					document.getElementById("password").readOnly = data.result.password != null;
					
					document.getElementById("businessName").value = data.result.businessName;
					document.getElementById("businessName").readOnly = data.result.businessName != null;	
					
					document.getElementById("street").value = data.result.street;
					document.getElementById("street").readOnly = data.result.street != null;
					
					document.getElementById("number").value = data.result.number;
					document.getElementById("number").readOnly = data.result.number != null;
					
					document.getElementById("neighborhood").value = data.result.neighborhood;
					document.getElementById("neighborhood").readOnly = data.result.neighborhood != null;
					
					document.getElementById("city").value = data.result.city;
					document.getElementById("city").readOnly = data.result.city != null;
					
					document.getElementById("state").value = data.result.state;
					document.getElementById("state").readOnly = data.result.state != null;
					
					document.getElementById("phone1").value = data.result.phone1;
					document.getElementById("phone1").readOnly = data.result.phone1 != null;
					
					document.getElementById("phone2").value = data.result.phone2;					
					document.getElementById("phone2").readOnly = data.result.phone2 != null;
					
					document.getElementById("phone3").value = data.result.phone3;
					document.getElementById("phone3").readOnly = data.result.phone3 != null;
					
					document.getElementById("phone4").value = data.result.phone4;
					document.getElementById("phone4").readOnly = data.result.phone4 != null;
					
					document.getElementById("description").value = data.result.description;					
					document.getElementById("description").readOnly = data.result.description != null;
										
					document.getElementById("socialNetwork1").value = data.result.socialNetwork1;
					document.getElementById("socialNetwork1").readOnly = data.result.socialNetwork1 != null;
					
					document.getElementById("socialNetwork2").value = data.result.socialNetwork2;
					document.getElementById("socialNetwork2").readOnly = data.result.socialNetwork2 != null;
					
					document.getElementById("site").value = data.result.site;
					document.getElementById("site").readOnly = data.result.site != null;	

					document.getElementById("service").value = data.result.service;
					document.getElementById("service").readOnly = true;
					
					console.log("social:" + data.result.socialNetwork1);
					
										
				}
			},
			error: function() {
						
			},
	});
		
	}
	
	register();

});


function getFormData($form){
    var unindexed_array = $form.serializeArray();
    var indexed_array = {};

    $.map(unindexed_array, function(n, i){
        indexed_array[n['name']] = n['value'];
    });

    return indexed_array;
}

function register() {
	$("#cpf").mask("999.999.999-99");
	$("#phone1").mask("(99)99999-99999");
	$("#phone2").mask("(99)99999-99999");
	$("#phone3").mask("(99)99999-99999");
	$("#phone4").mask("(99)99999-99999");
	
    $("#registerForm input, select").jqBootstrapValidation({
        preventSubmit: true,
        submitError: function($ , event, errors) {
            // additional error messages or events
        },
        submitSuccess: function($form, event) {
            event.preventDefault(); // prevent default submit behaviour
            		
			console.log($form);
			console.log($($form[0]).serializeArray());
			//var formData = new FormData($form[0]);
			//formData.append("plan", "gold");
			//formData.append("planid", 4);
			
			var formDataJSON = getFormData($($form[0]));
			formDataJSON.plan = 'gold';
			formDataJSON.planid = 4;
			
			$.ajax({
				url: '/change-plan',
				type: "POST",
				data: formDataJSON,
				success: function(data) {
					console.log(data);
					if (data.ok == 0) {
						// Fail message
						$('#success').html("<div class='alert alert-danger'>");
						$('#success > .alert-danger').html("<button type='button' class='close' data-dismiss='alert'"
						 + "aria-hidden='true'>&times;")
							.append("</button>");
						$('#success > .alert-danger').append("<strong>Erro: " + data.msg);
						$('#success > .alert-danger').append('</div>');
						//clear all fields
						//$('#registerForm').trigger("reset");

					}else {
						// Success message
						$('#success').html("<div class='alert alert-success'>");
						$('#success > .alert-success').html("<button type='button' class='close' data-dismiss='alert'" +
						 "aria-hidden='true'>&times;")
							.append("</button>");
						$('#success > .alert-success')
							.append("<strong>"+data.msg+"</strong>");
						$('#success > .alert-success')
							.append('</div>');
						$('#registerForm').trigger("reset");
						if (data.redirect) {
							window.location.href = data.redirect;
						}
					}
				},
				error: function(data) {
					console.log(data);
					// Fail message
					$('#success').html("<div class='alert alert-danger'>");
					$('#success > .alert-danger').html("<button type='button' class='close' data-dismiss='alert'" +
					 "aria-hidden='true'>&times;")
						.append("</button>");
					$('#success > .alert-danger').append("<strong>Desculpe, ocorreu um erro inesperado." +
					 "Por favor, tente novamente mais tarde.");
					$('#success > .alert-danger').append('</div>');
					//clear all fields
					$('#registerForm').trigger("reset");
				},
			});
			
        },
        filter: function() {
            return $(this).is(":visible");
        },
    });

    $("a[data-toggle=\"tab\"]").click(function(e) {
        e.preventDefault();
        $(this).tab("show");
    });
}