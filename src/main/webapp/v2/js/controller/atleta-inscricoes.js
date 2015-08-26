$(function() {
	moment.locale("pt-br");
	$("#registration-list-menu-item").addClass("active");

	RegistrationProxy.find().done(findOk);
	
});

function findOk(data, status, request){

	switch (request.status) {
		case 204: $("#minhas-inscricoes").removeClass("row").append("<div class='alert alert-danger alert-danger-rw' role='alert'>Você não se inscreveu em nenhuma prova ainda.</div>");
				  break;
		case 200: var template = $('#template').html();
				  $.each(data, function(index, value) {
					  value.race.date = moment(value.race.date, "YYYY-MM-DD").locale("pt-br").format('LL');
					  switch(value.status){
						  case "pendent" : value.status = "warning"; 
							 			   value.statusTranslate = "Pendente"; 
							 			   break;
			
						  case "confirmed" : value.status = "success";
						  					 value.statusTranslate = "Confirmada";
						  					 break;
				
						  case "cancelled" : value.status = "danger";
							   				 value.statusTranslate = "Cancelada";
							   				 break;
					  }
					  $('#minhas-inscricoes').append(Mustache.render(template, value));
				  });
				  break;
	}
}