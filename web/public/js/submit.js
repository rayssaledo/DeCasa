$(function(){				
	$('#btnSubmit').click(function(e){
		e.preventDefault();
						 
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
				
		var data =  { 
			cpf: cpf, 
			businessName: businessName,
			personalName: personalName,
			email: email,
			address: address,
			phone: phone,
			socialNetwork: socialNetwork,
			username: username,
			password: password,
			passwordRepeat: passwordRepeat};
			
				
		$.ajax({
			type: 'POST',
			data: data,			
			url: '/add-profissional',						
			success: function(data) {
				console.log('success');
				console.log(JSON.stringify(data));				
				alert(data.msg);								
			}, 
			error: function (request, status, error) {
				alert(request.responseText);
			}
		});
	});				
});