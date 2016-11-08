$(function() {
    //TODO checke pass older
    //TODO checke new pass with new pass confirme

    $("#sendChangePassword input, select").jqBootstrapValidation({
        preventSubmit: true,
        submitError: function($ , event, errors) {
            // additional error messages or events
        },
        submitSuccess: function($form, event) {
            event.preventDefault(); // prevent default submit behaviour
            var email = getCookie('email');
            email = email.replace("%40", "@").toString();
            var password = getCookie('password');
            var passwordConfirme = document.getElementById("passwordConfirme").value;
            var newPassword = document.getElementById("newPassword").value;
			var newPasswordConfirme = document.getElementById("newPasswordConfirme").value;

			console.log(email);
            console.log(password);

			if(password == passwordConfirme && newPassword == newPasswordConfirme){
			    $.ajax({
                    url: '/update-password',
                    type: "POST",
                    contentType: "application/json; charset=utf-8",
                    dataType:"json",
                    processData: false,
                    data: JSON.stringify({email: email, newPassword: newPassword}),
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
                            $('#sendChangePassword').trigger("reset");
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
                         " Por favor, tente novamente mais tarde.");
                        $('#success > .alert-danger').append('</div>');
                        //clear all fields
                        $('#sendChangePassword').trigger("reset");
                    },
                });
			}
			else{
			    $('#success').html("<div class='alert alert-danger'>");
                $('#success > .alert-danger').html("<button type='button' class='close' data-dismiss='alert'"
                 + "aria-hidden='true'>&times;")
                    .append("</button>");
                $('#success > .alert-danger').append("<strong>Erro: Dados não conferem.");
                $('#success > .alert-danger').append('</div>');
                $('#sendChangePassword').trigger("reset");
			}
        },
        filter: function() {
            return $(this).is(":visible");
        },
    });
});

/*When clicking on Full hide fail/success boxes */
$('#name').focus(function() {
    $('#success').html('');
});
