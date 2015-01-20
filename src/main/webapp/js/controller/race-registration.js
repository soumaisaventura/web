$(function() {
	
	var team = [];
	UserProxy.getLoggedInUser().done(function(user){
		team.push(user.id);
	});
	
	
	/**
	 * 
	 * */
	$(".list-group").on("click", "a", function(e){
		e.preventDefault();
		var index = team.indexOf($(this).data("remove"));
		if (index > -1) {
			team.splice(index, 1);
			$("#member-"+$(this).data("remove")).remove();
		}
		console.log(team);
	});
	
	$("#name").focus();
	
	App.loadDateCombos($("#birthday"), $("#birthday-month"), $("#birthday-year"));

	ProfileProxy.load().done(loadStep1Ok);
	HealthProxy.load().done(loadStep2Ok);
	RaceProxy.findCourses($("#race").val()).done(loadComboCategoriesOk);

	/**
	 * TODO Trazer o nome do atleta logo como primeiro item e sem permissão de
	 * exclusão Guardar as informações num array e montar a lista baseada no
	 * array. Ao remover item retirar do array. Adicionar o excludes na
	 * pesquisa. Não permitir adicionar mais atletas que a quantidade permitida
	 * da corrida.
	 */
	$("#user").autocomplete(
			{
				source : function(request, response) {
					RaceProxy.searchAvailableUsers($("#race").val(), request.term, team).done(function(data) {
						response(convertToLabelValueStructureFromUser(data));
					});
				},
				minLength : 3,
				select : function(event, ui) {
					team.push(ui.item.value);
					$("#members-list").append(
							'<li class="list-group-item" id="member-' + ui.item.value + '">' 
							+ ui.item.label
							+ '<a href="#" data-remove="' + ui.item.value + '"><span class="pull-right glyphicon glyphicon-remove" aria-hidden="true" title="Remover membro da equipe"></span></a></li>');
					$("#user").val("");
					console.log(team);
					return false;
				},
				focus: function( event, ui ) {
					$("#user").val(ui.item.label);
					return false;
				}
			});

	
	$("#city").autocomplete({
		source : function(request, response) {
			LocationProxy.searchCity(request.term).done(function(data) {
				response(convertToLabelValueStructureFromCity(data));
			});
		},
		minLength : 3,
		select : function(event, ui) {
			$("#city").val(ui.item.label.split("/")[0]);
			$("#city\\.id").val(ui.item.value);
			return false;
		},
		change : function(event, ui) {
			$("#city\\.id").val(ui.item ? ui.item.value : null);
			return false;
		},
		focus: function( event, ui ) {
			$("#city\\.id").val(ui.item.value);
			$("#city").val(ui.item.label.split("/")[0]);
			return false;
		}
	});

	// Barra de Navegação
	var navListItems = $('ul.setup-panel li a'), allWells = $('.setup-content');

	allWells.hide();

	navListItems.click(function(e) {
		e.preventDefault();
		var $target = $($(this).attr('href')), $item = $(this).closest('li');

		if (!$item.hasClass('disabled')) {
			navListItems.closest('li').removeClass('active');
			$item.addClass('active');
			allWells.hide();
			$target.show();
		}
	});

	$('ul.setup-panel li.active a').trigger('click');

	// Cadastro dos dados pessoais
	$('#activate-step-2').on('click', function(e) {
		var birthday = "";
		
		if(!isNaN($("#birthday-year").val()) && !isNaN($("#birthday-month").val()) && !isNaN($("#birthday").val())){
			birthday = $("#birthday-year").val() + "-" + $("#birthday-month").val() + "-" + $("#birthday").val(); 
		}
		
		
		var data = {
			'name' : $("#name").val(),
			'birthday' : birthday,
			'rg' : $("#rg").val(),
			'cpf' : $("#cpf").val(),
			'city' : {
				"id" : $("#city\\.id").val()
			},
			'gender' : $("#gender").val(),
			'mobile' : $("#mobile").val()
		};
		ProfileProxy.update(data).done(updateStep1Ok).fail(updateStep1Fail);
	});

	// Cadastro dos dados médicos
	$('#activate-step-3').on('click', function(e) {
		var data = {
			'bloodType' : $("#bloodType").val(),
			'allergy' : $("#allergy").val(),
			'healthCareName' : $("#healthCareName").val(),
			'healthCareNumber' : $("#healthCareNumber").val(),
			'emergencyContactName' : $("#emergencyContactName").val(),
			'emergencyContactPhoneNumber' : $("#emergencyContactPhoneNumber").val(),
		};
		HealthProxy.update(data).done(updateStep2Ok).fail(updateStep2Fail);
	});

	// Cadastro dos dados da equipe
	$('#activate-step-4').on('click', function(e) {

		var data = {
			'teamName' : $("#teamName").val(),
			'category' : $("#category").val().split("#")[0],
			'course' : $("#category").val().split("#")[1],
			'members' : team
		};
		RaceRegistrationProxy.validateRegistration($("#race").val(), data).done(updateStep3Ok).fail(updateStep3Fail);
	});

});

/**
 * Função que carrega os dados pessoais do usuário.
 */
function loadStep1Ok(data) {
	$("#name").val(data.name);
	$("#rg").val(data.rg);
	$("#cpf").val(data.cpf);
	
	if(data.birthday){
		$("#birthday-year").val(parseInt(data.birthday.split("-")[0]));
		$("#birthday-month").val(parseInt(data.birthday.split("-")[1]));
		$("#birthday").val(parseInt(data.birthday.split("-")[2]));
	}
	
	$("#gender").val(data.gender);
	$("#city\\.id").val(data.city.id);
	$("#city").val(data.city.name);
	$("#mobile").val(data.mobile);

	$("#loggedUser").text(data.name);
}

/**
 * Funçao que carrega os dados médicos do usuário.
 */
function loadStep2Ok(data) {
	console.log(data);
	$("#bloodType").val(data.bloodType);
	$("#allergy").val(data.allergy);
	$("#healthCareName").val(data.healthCareName);
	$("#healthCareNumber").val(data.healthCareNumber);
	$("#emergencyContactName").val(data.emergencyContactName);
	$("#emergencyContactPhoneNumber").val(data.emergencyContactPhoneNumber);
}

/**
 * Monta a caixa de seleção das categorias disponíveis da corrida. TODO Pensar
 * numa estrutura para pegar a quantidade de membros da corrida.
 */
function loadComboCategoriesOk(data) {
	$.each(data, function(index, value) {
		var course = value;
		var optgroup = document.createElement("OPTGROUP");
		optgroup.setAttribute("label", course.length + " km");
		$("#category").append(optgroup);
		$.each(course.categories, function(index, value){
			$("#category").append(new Option(this.name, this.id+"#"+course.id));
		});
		
	});
}

/**
 * 
 */
function updateStep1Ok(data) {
	console.log('updateStep1Ok');
	$('ul.setup-panel li:eq(0)').addClass('disabled');
	$('ul.setup-panel li:eq(1)').removeClass('disabled');
	$('ul.setup-panel li a[href="#step-2"]').trigger('click');
}

/**
 * 
 */
function updateStep2Ok(data) {
	console.log('updateStep2Ok');
	$('ul.setup-panel li:eq(1)').addClass('disabled');
	$('ul.setup-panel li:eq(2)').removeClass('disabled');
	$('ul.setup-panel li a[href="#step-3"]').trigger('click');
}

/**
 * 
 */
function updateStep3Ok(data) {
	console.log('updateStep3Ok');
	$('ul.setup-panel li:eq(2)').addClass('disabled');
	$('ul.setup-panel li:eq(3)').removeClass('disabled');
	$('ul.setup-panel li a[href="#step-4"]').trigger('click');
}

/**
 * Tratamento de erro dos dados básicos.
 */
function updateStep1Fail(request) {
	console.log('updateFail');
	switch (request.status) {
		case 422:
			$($("#form-step-1 input").get().reverse()).each(function() {
				var id = $(this).attr('id');
				var message = null;

				$.each(request.responseJSON, function(index, value) {
					if (id == value.property) {
						message = value.message;
						return;
					}
				});

				if (message) {
					$("#" + id + "-message").html(message).show();
					$(this).focus();
				} else {
					$("#" + id + "-message").hide();
				}
			});
			break;

		default:
			break;
	}
}

/**
 * Tratamento de erro dos dados médicos.
 */
function updateStep2Fail(request) {
	console.log('updateFail');
	switch (request.status) {
		case 422:
			$($("#form-step-2 input").get().reverse()).each(function() {
				var id = $(this).attr('id');
				var message = null;

				$.each(request.responseJSON, function(index, value) {
					if (id == value.property) {
						message = value.message;
						return;
					}
				});

				if (message) {
					$("#" + id + "-message").html(message).show();
					$(this).focus();
				} else {
					$("#" + id + "-message").hide();
				}
			});
			break;

		default:
			break;
	}
}

/**
 * Tratamento de erro dos dados corrida.
 */
function updateStep3Fail(request) {
	console.log('updateStep3Fail');
	switch (request.status) {
		case 422:
			$($("#form-step-3 input").get().reverse()).each(function() {
				var id = $(this).attr('id');
				var message = null;

				$.each(request.responseJSON, function(index, value) {
					if (id == value.property) {
						message = value.message;
						return;
					}
				});

				if (message) {
					$("#" + id + "-message").html(message).show();
					$(this).focus();
				} else {
					$("#" + id + "-message").hide();
				}
			});
			break;

		default:
			break;
	}
}

/**
 * Função utilitária que converte o objeto retornado no suggest para o formato
 * do jqueryUi.
 */
function convertToLabelValueStructureFromUser(data) {
	var newData = [];
	$.each(data, function() {
		newData.push({
			"label" : this.profile.name,
			"value" : this.id
		});
	});
	return newData;
}

/**
 * Função utilitária que converte o objeto retornado no suggest para o formato
 * do jqueryUi.
 */
function convertToLabelValueStructureFromCity(data) {
	var newData = [];
	$.each(data, function() {
		newData.push({
			"label" : this.name + "/" + this.state,
			"value" : this.id
		});
	});
	return newData;
}
