$(function() {

	/**
	 * Objeto que guardará os dados da equipe
	 */
	var teamData;

	/**
	 * Array usado para armazenar os id dos membros da equipe Já inicializa com
	 * id do usuário logado
	 */
	var team = [];
	UserProxy.getLoggedInUser().done(function(user) {
		team.push(user.id);
	});

	/**
	 * Adiciona um event listener para remoção nos membros inseridos na equipe
	 */
	$(".list-group").on("click", "a", function(e) {
		e.preventDefault();
		var index = team.indexOf($(this).data("remove"));
		if (index > -1) {
			team.splice(index, 1);
			$("#member-" + $(this).data("remove")).remove();
		}
	});

	/**
	 * Carrega a combo de categoria com as categorias disponíveis para a corrida
	 */
	RaceProxy.findCourses($("#race").val()).done(loadComboCategoriesOk);

	/**
	 * Carrega os dados pessoais
	 */
	ProfileProxy.load().done(loadStep1Ok);

	/**
	 * Carrega os dados médicos
	 */
	HealthProxy.load().done(loadStep2Ok);

	$("#name").focus();

	/**
	 * Habilita o autocomplete no campo Atleta Ao selecionar o atleta
	 * automaticamente entra na lista de membros
	 */
	$("#user")
			.autocomplete(
					{
						source : function(request, response) {
							UserProxy.search(request.term, team).done(function(data) {
								response(convertToLabelValueStructureFromUser(data));
							});
						},
						minLength : 3,
						select : function(event, ui) {
							team.push(ui.item.value);
							$("#members-list")
									.append(
											'<li class="list-group-item" id="member-'
													+ ui.item.value
													+ '">'
													+ ui.item.label
													+ '<a href="#" data-remove="'
													+ ui.item.value
													+ '"><span class="pull-right glyphicon glyphicon-remove" aria-hidden="true" title="Remover membro da equipe"></span></a></li>');
							$("#user").val("");
							console.log(team);
							return false;
						},
						focus : function(event, ui) {
							$("#user").val(ui.item.label);
							return false;
						}
					});

	

	/**
	 * Inicializa a barra de navegação
	 */
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

	/**
	 * Cadastro dos dados médicos
	 */
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

	/**
	 * Cadastro dos dados da equipe
	 */
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

/* ---------------- Funções de Callback ---------------- */

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
		$.each(course.categories, function(index, value) {
			$("#category").append(new Option(this.name, this.id + "#" + course.id));
		});

	});
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

/* ---------------- Funções Utilitárias ---------------- */

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

