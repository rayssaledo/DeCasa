$(function(){				
	$('#btnSubmit').click(function(e){
								 
		var cpf = document.getElementById("cpf").value;
		var businessName = document.getElementById("businessName").value;
		var email = document.getElementById("email").value;		
		var address = document.getElementById("address").value;
		var phone = document.getElementById("phone").value;
		var socialNetwork = document.getElementById("socialNetwork").value;
		var pictury = document.getElementById("pictury").value;
		var site = document.getElementById("site").value;	
		var username = document.getElementById("username").value;
		var password = document.getElementById("password").value;
		var passwordRepeat = document.getElementById("passwordRepeat").value;
		var personalName = document.getElementById("personalName").value;
		var services = []
		
		
		$('#check-services :checked').each(function() {
		   services.push($(this).val());
		});
		      		
		if (cpf && personalName && businessName &&  email && address && phone && username && password && passwordRepeat) {
			
			var error = false;
			if (password != passwordRepeat) {
				alert('Senhas não coincidem!');
					error = true;
			} else if (password.length < 6) {
				alert('Senha deve conter pelo menos 6 caracteres!');
				error = true;
			} else {
				error = false;
			}
			
			if (error == false) {				
				var data =  { 
					cpf: cpf, 
					businessName: businessName,
					personalName: personalName,
					email: email,
					address: address,
					phone: phone,
					socialNetwork: socialNetwork,
					services: services,
					username: username,
					password: password,
					passwordRepeat: passwordRepeat};
				
				$.ajax({
					type: 'POST',
					data: data,	
					url: '/add-profissional',						
					success: function(data) {
						console.log('success');
						console.log(data);	
						if (typeof data == "string") {
							data = jQuery.parseJSON(data); 
						}								
						alert(data.msg);								
					}, 
					error: function (request, status, error) {
						alert(request.responseText);
					}
				});	
			}
			document.getElementById("register-service").focus();
		}
		
		
	});	


	var requiredCheckboxes = $('.options :checkbox[required]');
	requiredCheckboxes.change(function(){
		if(requiredCheckboxes.is(':checked')) {
			requiredCheckboxes.removeAttr('required');
		} else {
			requiredCheckboxes.attr('required', 'required');
		}
	});
	
	

});