$(function() {
	var email = getCookie("email");
    email = email.replace("%40", "@");
    var password = getCookie("password");
    var user = JSON.parse(getCookie("user"));

    $("#form-free input, select").jqBootstrapValidation({
        preventSubmit: true,
        submitError: function($ , event, errors) {
            // additional error messages or events
        },
        submitSuccess: function($form, event) {
            event.preventDefault(); // prevent default submit behaviour
            // get values from FORM
			var businessName = document.getElementById("business_free").value;
            var service = document.getElementById("service").value;
			var description = document.getElementById("description_free").value;
			var phone1 = document.getElementById("phone1_free").value;
			var name = user.name;
			var cpf = user.cpf;

            var firstName = name; // For Success/Failure Message
            // Check for white space in name for Success/Fail message
            if (firstName.indexOf(' ') >= 0) {
                firstName = name.split(' ').slice(0, -1).join(' ');
            }

            $.ajax({
                url: '/update-professional',
                type: "POST",
                contentType: "application/json; charset=utf-8",
                dataType:"json",
                processData: false,
                data: JSON.stringify({
                    name : name,
                    businessName : businessName,
                    cpf : cpf,
                    phone1 : phone1,
                    service : service,
                    description : description,
                    email : email,
                    password : password,
                    passwordRepeat : password
                }),
                success: function(data) {
                    if (data.ok == 0) {
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
                    }
                },
                error: function() {
                    // Fail message
                    $('#success').html("<div class='alert alert-danger'>");
                    $('#success > .alert-danger').html("<button type='button' class='close' data-dismiss='alert'" +
                     "aria-hidden='true'>&times;")
                        .append("</button>");
                    $('#success > .alert-danger').append("<strong>Desculpe " + firstName + ", ocorreu um erro inesperado." +
                     "Por favor, tente novamente mais tarde.");
                    $('#success > .alert-danger').append('</div>');
                },
            });
        },
        filter: function() {
            return $(this).is(":visible");
        },
    });
	
	$("#form-bronze input, select").jqBootstrapValidation({
        preventSubmit: true,
        submitError: function($ , event, errors) {
            // additional error messages or events
        },
        submitSuccess: function($form, event) {
            event.preventDefault(); // prevent default submit behaviour
          
			var businessName = document.getElementById("business_bronze").value;
			var service = document.getElementById("service_bronze").value;			
			var phone1 = document.getElementById("phone1_bronze").value;
			var phone2 = document.getElementById("phone2_bronze").value;
			var description = document.getElementById("description_bronze").value;
			var socialNetwork1 = document.getElementById("socialNetwork1_bronze").value;
			var name = user.name;
			var cpf = user.cpf;

            var firstName = name; // For Success/Failure Message
            // Check for white space in name for Success/Fail message
            if (firstName.indexOf(' ') >= 0) {
                firstName = name.split(' ').slice(0, -1).join(' ');
            }

            $.ajax({
                url: '/update-professional',
                type: "POST",
                contentType: "application/json; charset=utf-8",
                dataType:"json",
                processData: false,
                data: JSON.stringify({
					email: email,
					name: name,
					businessName: businessName,
					cpf: cpf, 
					phone1: phone1,
					phone2: phone2,
					socialNetwork1: socialNetwork1,
					description: description,
					service: service,															
					password: password,
					passwordRepeat : password
                }),
                success: function(data) {
                    if (data.ok == 0) {
                        // Fail message
                        $('#success_bronze').html("<div class='alert alert-danger'>");
                        $('#success_bronze > .alert-danger').html("<button type='button' class='close' data-dismiss='alert'"
                         + "aria-hidden='true'>&times;")
                            .append("</button>");
                        $('#success_bronze > .alert-danger').append("<strong>Erro: " + data.msg);
                        $('#success_bronze > .alert-danger').append('</div>');
                    }else {
                        // Success message
                        $('#success_bronze').html("<div class='alert alert-success'>");
                        $('#success_bronze > .alert-success').html("<button type='button' class='close' data-dismiss='alert'" +
                         "aria-hidden='true'>&times;")
                            .append("</button>");
                        $('#success_bronze > .alert-success')
                            .append("<strong>"+data.msg+"</strong>");
                        $('#success_bronze > .alert-success')
                            .append('</div>');
                    }
                },
                error: function() {
                    // Fail message
                    $('#success_bronze').html("<div class='alert alert-danger'>");
                    $('#success_bronze > .alert-danger').html("<button type='button' class='close' data-dismiss='alert'" +
                     "aria-hidden='true'>&times;")
                        .append("</button>");
                    $('#success_bronze > .alert-danger').append("<strong>Desculpe " + firstName + ", ocorreu um erro inesperado." +
                     "Por favor, tente novamente mais tarde.");
                    $('#success_bronze > .alert-danger').append('</div>');
                },
            });
        },
        filter: function() {
            return $(this).is(":visible");
        },
    });
	
	$("#form-silver input, select").jqBootstrapValidation({
        preventSubmit: true,
        submitError: function($ , event, errors) {
            // additional error messages or events
        },
        submitSuccess: function($form, event) {
            event.preventDefault(); // prevent default submit behaviour
          
			var businessName = document.getElementById("business_silver").value;
			var service = document.getElementById("service_silver").value;			
			var phone1 = document.getElementById("phone1_silver").value;
			var phone2 = document.getElementById("phone2_silver").value;
			var phone3 = document.getElementById("phone3_silver").value;
			var street = document.getElementById("street_silver").value;
            var number = document.getElementById("number_silver").value;
			var neighborhood = document.getElementById("neighborhood_silver").value;
			var city = document.getElementById("city_silver").value;
			var state = document.getElementById("state_silver").value;
			var description = document.getElementById("description_silver").value;
			var socialNetwork1 = document.getElementById("socialNetwork1_silver").value;
			var site = document.getElementById("site_silver").value;
			var name = user.name;
			var cpf = user.cpf;

            var firstName = name; // For Success/Failure Message
            // Check for white space in name for Success/Fail message
            if (firstName.indexOf(' ') >= 0) {
                firstName = name.split(' ').slice(0, -1).join(' ');
            }

            $.ajax({
                url: '/update-professional',
                type: "POST",
                contentType: "application/json; charset=utf-8",
                dataType:"json",
                processData: false,
                data: JSON.stringify({
					email: email,
					name: name,
					businessName: businessName,
					cpf: cpf, 
					phone1: phone1,
					phone2: phone2,
					phone3: phone3,
					street: street,
					number: number,
					neighborhood: neighborhood,
					city: city,
					state: state,
					site: site,
					socialNetwork1: socialNetwork1,
					description: description,			
					service: service,												
					password: password,
					passwordRepeat : password
                }),
                success: function(data) {
                    if (data.ok == 0) {
                        // Fail message
                        $('#success_silver').html("<div class='alert alert-danger'>");
                        $('#success_silver > .alert-danger').html("<button type='button' class='close' data-dismiss='alert'"
                         + "aria-hidden='true'>&times;")
                            .append("</button>");
                        $('#success_silver > .alert-danger').append("<strong>Erro: " + data.msg);
                        $('#success_silver > .alert-danger').append('</div>');
                    }else {
                        // Success message
                        $('#success_silver').html("<div class='alert alert-success'>");
                        $('#success_silver > .alert-success').html("<button type='button' class='close' data-dismiss='alert'" +
                         "aria-hidden='true'>&times;")
                            .append("</button>");
                        $('#success_silver > .alert-success')
                            .append("<strong>"+data.msg+"</strong>");
                        $('#success_silver > .alert-success')
                            .append('</div>');
                    }
                },
                error: function() {
                    // Fail message
                    $('#success_silver').html("<div class='alert alert-danger'>");
                    $('#success_silver > .alert-danger').html("<button type='button' class='close' data-dismiss='alert'" +
                     "aria-hidden='true'>&times;")
                        .append("</button>");
                    $('#success_silver > .alert-danger').append("<strong>Desculpe " + firstName + ", ocorreu um erro inesperado." +
                     "Por favor, tente novamente mais tarde.");
                    $('#success_silver > .alert-danger').append('</div>');
                },
            });
        },
        filter: function() {
            return $(this).is(":visible");
        },
    });
		
	$("#form-gold input, select").jqBootstrapValidation({
        preventSubmit: true,
        submitError: function($ , event, errors) {
            // additional error messages or events
        },
        submitSuccess: function($form, event) {
            event.preventDefault(); // prevent default submit behaviour
          
			var businessName = document.getElementById("business_gold").value;
			var service = document.getElementById("service").value;			
			var street = document.getElementById("street_gold").value;
            var number = document.getElementById("number_gold").value;
			var neighborhood = document.getElementById("neighborhood_gold").value;
			var city = document.getElementById("city_gold").value;
			var state = document.getElementById("state_gold").value;
			var phone1 = document.getElementById("phone1_gold").value;
			var phone2 = document.getElementById("phone2_gold").value;
			var phone3 = document.getElementById("phone3_gold").value;
			var phone4 = document.getElementById("phone4_gold").value;
			var description = document.getElementById("description_gold").value;
			var socialNetwork1 = document.getElementById("socialNetwork1_gold").value;
			var socialNetwork2 = document.getElementById("socialNetwork2_gold").value;
			var site = document.getElementById("site_gold").value;
			var name = user.name;
			var cpf = user.cpf;

            var firstName = name; // For Success/Failure Message
            // Check for white space in name for Success/Fail message
            if (firstName.indexOf(' ') >= 0) {
                firstName = name.split(' ').slice(0, -1).join(' ');
            }

            $.ajax({
                url: '/update-professional',
                type: "POST",
                contentType: "application/json; charset=utf-8",
                dataType:"json",
                processData: false,
                data: JSON.stringify({
					email: email,
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
					description: description,
					service: service,												
					password: password,
					passwordRepeat : password
                }),
                success: function(data) {
                    if (data.ok == 0) {
                        // Fail message
                        $('#success_gold').html("<div class='alert alert-danger'>");
                        $('#success_gold > .alert-danger').html("<button type='button' class='close' data-dismiss='alert'"
                         + "aria-hidden='true'>&times;")
                            .append("</button>");
                        $('#success_gold > .alert-danger').append("<strong>Erro: " + data.msg);
                        $('#success_gold > .alert-danger').append('</div>');
                    }else {
                        // Success message
                        $('#success_gold').html("<div class='alert alert-success'>");
                        $('#success_gold > .alert-success').html("<button type='button' class='close' data-dismiss='alert'" +
                         "aria-hidden='true'>&times;")
                            .append("</button>");
                        $('#success_gold > .alert-success')
                            .append("<strong>"+data.msg+"</strong>");
                        $('#success_gold > .alert-success')
                            .append('</div>');
                    }
                },
                error: function() {
                    // Fail message
                    $('#success_gold').html("<div class='alert alert-danger'>");
                    $('#success_gold > .alert-danger').html("<button type='button' class='close' data-dismiss='alert'" +
                     "aria-hidden='true'>&times;")
                        .append("</button>");
                    $('#success_gold > .alert-danger').append("<strong>Desculpe " + firstName + ", ocorreu um erro inesperado." +
                     "Por favor, tente novamente mais tarde.");
                    $('#success_gold > .alert-danger').append('</div>');
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