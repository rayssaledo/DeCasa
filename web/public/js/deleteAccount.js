$("#btnSubmit").click(function() {
	
	var email = getCookie('email');
	email = email.replace("%40", "@");	
	
	if(email != undefined && email != null){		
		$.ajax({ 
			url: '/delete-account',
			type: "DELETE",
			data: {email:email},
			success: function(data) {				
				if(data["ok"] == 1){
					window.location.href = "/";
				} 
			}
			
		});				
	}
	
	
});
