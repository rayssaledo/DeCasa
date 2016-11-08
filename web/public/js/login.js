$(function(){
	$("#form_login input, select").jqBootstrapValidation({
        preventSubmit: true,
        submitError: function($ , event, errors) {
            // additional error messages or events
        },
        submitSuccess: function($form, event) {
            event.preventDefault(); // prevent default submit behaviour
            // get values from FORM
			
			var email = document.getElementById("email_login").value;		
			var password = document.getElementById("password_login").value;	
				
			$.ajax({
				url: '/login',
				type: "POST",
				data: { 
					email: email,											
					password: password
				},
				cache: false,
				success: function(data) {
					if (data.ok == 0) {
						// Fail message
						$('#success_login').html("<div class='alert alert-danger'>");
						$('#success_login > .alert-danger').html("<button type='button' class='close'" +
						"data-dismiss='alert' aria-hidden='true'>&times;")
							.append("</button>");
						$('#success_login > .alert-danger').append("<strong>Erro: " + data.msg);
						$('#success_login > .alert-danger').append('</div>');
						//clear all fields
						$('#form_login').trigger("reset");
					} else {
						// Success message						
						window.location.href = "/profile";
					}
				},
				error: function() {
					// Fail message
					$('#success_login').html("<div class='alert alert-danger'>");
					$('#success_login > .alert-danger').html("<button type='button' class='close' data-dismiss='alert'" +
					"aria-hidden='true'>&times;")
						.append("</button>");
					$('#success_login > .alert-danger').append("<strong>Desculpe, ocorreu um erro inesperado. Por favor, "+
					 "tente novamente mais tarde!");
					$('#success_login > .alert-danger').append('</div>');
					//clear all fields
					$('#form_login').trigger("reset");
				},
			});
        },
        filter: function() {
            return $(this).is(":visible");
        },
    });
});

$('.navbar-brand').click(function() {
	window.location.href = '/';
});