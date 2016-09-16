function getCookie(cname) {
	    var name = cname + "=";
	    var ca = document.cookie.split(';');
	    for(var i = 0; i <ca.length; i++) {
	        var c = ca[i];
	        while (c.charAt(0)==' ') {
	            c = c.substring(1);
	        }
	        if (c.indexOf(name) == 0) {
	            return c.substring(name.length,c.length);
	        }
	    }
	    return "";
	}

$("#logout").click(function(event){
	event.preventDefault();
	
	var email = getCookie('email');
	var password = getCookie('password');

	$.ajax({
  		type: "POST",
  		url: "/logout",
  		data: { 
			email: email,											
			password: password
		},
		success: function(data) {
			if (data.ok == 1) {
				window.location.href = "/";
				console.log("Deslogado");
			}
		
		},
		error: function() {
			// Fail message
			console.log("Error");
		},
		cache: false
	});
});