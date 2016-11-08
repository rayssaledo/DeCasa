$(function() {
	$("#cpf").mask("999.999.999-99");
	$("#phone1").mask("(99)99999-99999");
	
    $("#registerForm input, select").jqBootstrapValidation({
        preventSubmit: true,
        submitError: function($ , event, errors) {
            // additional error messages or events
        },
        submitSuccess: function($form, event) {
            event.preventDefault(); // prevent default submit behaviour
            // get values from FORM

			var name = document.getElementById("name").value;
			var cpf = document.getElementById("cpf").value;
			var businessName = document.getElementById("businessName").value;
			var description = document.getElementById("description").value;
			var phone1 = document.getElementById("phone1").value;
			var service = document.getElementById("service").value;
			var email = document.getElementById("email").value;		
			var password = document.getElementById("password").value;
			var passwordRepeat = document.getElementById("passwordRepeat").value;
            var plan = "free";

            var firstName = name; // For Success/Failure Message
            // Check for white space in name for Success/Fail message
            if (firstName.indexOf(' ') >= 0) {
                firstName = name.split(' ').slice(0, -1).join(' ');
            }
			
			var formData = new FormData($form[0]);
			formData.append("plan", plan);

			cpf = cpf.replace(/\./g, "");
			cpf = cpf.replace(/-/g, "");
			
			if (cpfValidate(cpf)) {
				$.ajax({
					url: '/add-professional',
					type: "POST",
					contentType: false,
					processData: false,
					data: formData,
					success: function(data) {
						console.log(data);
						if (data.ok == 0 && data.msg == "Email is being used!") {
							// Fail message
							$('#success').html("<div class='alert alert-danger'>");
							$('#success > .alert-danger').html("<button type='button' class='close' data-dismiss='alert'"
							 + "aria-hidden='true'>&times;")
								.append("</button>");
							$('#success > .alert-danger').append("<strong>Erro: " + data.msg);
							$('#success > .alert-danger').append('</div>');
							//clear all fields
							document.getElementById("email").value='';
						} else if (data.ok == 0) {
                        	// Fail message
                            $('#success').html("<div class='alert alert-danger'>");
                            $('#success > .alert-danger').html("<button type='button' class='close' data-dismiss='alert'"
                             + "aria-hidden='true'>&times;")
                                .append("</button>");
                            $('#success > .alert-danger').append("<strong>Erro: " + data.msg);
                            $('#success > .alert-danger').append('</div>');

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
						$('#success > .alert-danger').append("<strong>Desculpe " + firstName + ", ocorreu um erro inesperado." +
						 " Por favor, tente novamente mais tarde.");
						$('#success > .alert-danger').append('</div>');
						//clear all fields
						$('#registerForm').trigger("reset");
					},
				});
			} else {
				$('#success').html("<div class='alert alert-danger'>");
				$('#success > .alert-danger').html("<button type='button' class='close' data-dismiss='alert'" +
				 "aria-hidden='true'>&times;")
					.append("</button>");
				$('#success > .alert-danger').append("<strong> CPF inválido!");
				$('#success > .alert-danger').append('</div>');
				//clear all fields
				document.getElementById("cpf").value='';
			}
        },
        filter: function() {
            return $(this).is(":visible");
        },
    });

    $("a[data-toggle=\"tab\"]").click(function(e) {
        e.preventDefault();
        $(this).tab("show");
    });
});
function cpfValidate(strCPF) {
    var sum;
    var rest;
    sum = 0;
	if (strCPF == "00000000000") return false;

	for (i=1; i<=9; i++) sum = sum + parseInt(strCPF.substring(i-1, i)) * (11 - i);
	rest = (sum * 10) % 11;

    if ((rest == 10) || (rest == 11))  rest = 0;
    if (rest != parseInt(strCPF.substring(9, 10)) ) return false;

	sum = 0;
    for (i = 1; i <= 10; i++) sum = sum + parseInt(strCPF.substring(i-1, i)) * (12 - i);
    rest = (sum * 10) % 11;

    if ((rest == 10) || (rest == 11))  rest = 0;
    if (rest != parseInt(strCPF.substring(10, 11) ) ) return false;
    return true;
}

function emailValidate(){
    //perguntar a dani o que deve ser feito...
    //o que seria email inválido? aquele em uso?
}

/*When clicking on Full hide fail/success boxes */
$('#name').focus(function() {
    $('#success').html('');
});
