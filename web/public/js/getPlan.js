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
					
					var plan = getPlanName(data.result.plan);					
					try{
						document.getElementById("plan_name").innerHTML = plan;
					} catch(err) {}	
					
					var date = data.result.plan_date;
					var status;
					if (date == undefined) {
						status = "INATIVO"; 
						date = "AINDA NÃO EFETUADO";
					} else {
						status = "ATIVO";
					}
					
					
					var content;
					if (plan == "OURO") {
						content = "<img src='img/plans/medal_gold.png' alt='Plano Ouro'>" +
							"<span class='titulo'>Plano Ouro</span>" +
							"<span>Permite o profissional cadastrar muitas informações, divulgando melhor seu serviço. Garante as primeras posições no resultado de busca pelo serviço.</span>" + 
							"<br><br><br>"+
							"<div>"+
								"<label>Status do anúncio:</label>"+
								"<p id='status'>"+status+"</p>"+
								"<label>ÚLTIMO PAGAMENTO:</label>" +
								"<p id='last_payment'>"+date+"</p>"+
							"</div>" +
					
							"<div>"+
								"<div id='success'></div>"+
								"<input id='btnSubmit' type='submit' class='btn btn-primary'  value='Fazer pagamento' />"+
							"</div>" +
							
							"<div class='emptyBox20'></div>"+
							"<div class='emptyBox20'></div>"+
							
							"<a href='/plans'>Conheça outros planos</a>"+
							
							"<div class='emptyBox20'></div>";
							
					} else if (plan == "PRATA") {
						content = "<img src='img/plans/medal_silver.png' alt='Plano Prata'>" +
							"<span class='titulo'>Plano Prata</span>" +
							"<span>Permite o profissional cadastrar muitas informações, um pouco menos que no Plano Ouro. Garante boas posições no resultado de busca pelo serviço, ficando depois dos anúncios Ouro.</span>" + 
							"<br><br><br>"+
							"<div>"+
								"<label>Status do anúncio:</label>"+
								"<p id='status'>"+status+"</p>"+
								"<label>ÚLTIMO PAGAMENTO:</label>" +
								"<p id='last_payment'>"+date+"</p>"+
							"</div>" +
					
							"<div>"+
								"<div id='success'></div>"+
								"<input id='btnSubmit' type='submit' class='btn btn-primary'  value='Fazer pagamento' />"+
							"</div>" +
							
							"<div class='emptyBox20'></div>"+
							"<div class='emptyBox20'></div>"+
							
							"<a href='/plans'>Conheça outros planos</a>"+
							
							"<div class='emptyBox20'></div>";
							
					} else if (plan == "BRONZE") {
						content = "<img src='img/plans/medal_bronze.png' alt='Plano Bronze'>" +
							"<span class='titulo'>Plano Bronze</span>" +
							"<span>Permite o profissional cadastrar algumas informações. Garante posições depois dos anúncios Prata e antes dos anúncios Gratuitos.</span>" + 
							"<br><br><br>"+
							"<div>"+
								"<label>Status do anúncio:</label>"+
								"<p id='status'>"+status+"</p>"+
								"<label>ÚLTIMO PAGAMENTO:</label>" +
								"<p id='last_payment'>"+date+"</p>"+
							"</div>" +
					
							"<div>"+
								"<div id='success'></div>"+
								"<input id='btnSubmit' type='submit' class='btn btn-primary'  value='Fazer pagamento' />"+
							"</div>" +
							
							"<div class='emptyBox20'></div>"+
							"<div class='emptyBox20'></div>"+
							
							"<a href='/plans'>Conheça outros planos</a>"+
							
							"<div class='emptyBox20'></div>";
					} else {
						content = "<img src='img/plans/medal_free.png' alt='Plano Grátis'>" +
							"<span class='titulo'>Plano Grátis</span>" +
							"<span>Permite o profissional cadastrar informações básicas do contato do seu serviço. Estes anúncios encontram-se posicionados depois dos anúncios Bronze.</span>" + 
							"<br><br><br>"+
							"<div>"+
								"<label>Status do anúncio:</label>"+
								"<p id='status'>ATIVO</p>"+
							"</div>" +							
							"<div class='emptyBox20'></div>"+
							"<div class='emptyBox20'></div>"+
							
							"<a href='/plans'>Conheça outros planos</a>"+
							
							"<div class='emptyBox20'></div>";
					}
					
					try {
						document.getElementById("card").innerHTML = content;
						
					} catch(err){}
					
				}
			},
			error: function() {
						
			},
	});
		
	}

});

function getPlanName(plan) {
	if (plan == "gold") {
		return "OURO";
	} else if (plan == "silver") {
		return "PRATA";
	} else if (plan == "bronze") {
		return "BRONZE";
	} else {
		return "GRÁTIS"
	}
}