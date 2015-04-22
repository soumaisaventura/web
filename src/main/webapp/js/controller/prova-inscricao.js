$(function() {
	moment.locale("pt-br");
	numeral.language('pt-br');
	numeral.defaultFormat('$ 0');

	var memberIds = [];
	var id = $("#id").val();

	$("#category").focus();
	$('#members-list').footable({
		breakpoints : {
			phone : 450
		}
	});
	$("#annual-fee-description").text(App.annualFeeDescription);

	LogonProxy.getOAuthAppIds().done(getOAuthAppIdsOk);

	/**
	 * Carrega os dados da corrida
	 */
	RaceProxy.loadSummary(id).done(loadOk);

	/**
	 * Carrega a combo de categoria com as categorias disponíveis para a corrida
	 */
	RaceProxy.findCourses(id).done(loadCategoriesOk);

	/**
	 * Carrega o usuário logado na lista de membros da equipe
	 */
	var user;

	if (!(user = App.getLoggedInUser())) {
		App.handle401();
	}

	RaceProxy.order(id, user.id).done(function(order) {
		memberIds.push(order.rows[0].id);
		addRowOnMemberList(order.rows[0], true);
		updateTotal();
	});

	/**
	 * Habilita o autocomplete no campo Atleta Ao selecionar o atleta
	 * automaticamente entra na lista de membros
	 */
	$("#members").autocomplete({
		source : function(request, response) {
			UserProxy.search(request.term, memberIds).done(function(data) {
				response(convertToLabelValueStructureFromUser(data));
			});
		},
		minLength : 3,
		select : function(event, ui) {
			$("#members-id").val(ui.item.value);
			$("#members").val(ui.item.label);
			return false;
		},
		focus : function(event, ui) {
			$("#members-id").val(ui.item.value);
			$("#members").val(ui.item.label);
			return false;
		}
	});

	/**
	 * 
	 */
	$("#bt-add-athlete").click(function() {
		var members = $("#members");
		var membersId = $("#members-id");
		$("#members-message").hide();

		if (membersId) {
			RaceProxy.order(id, membersId.val()).done(function(order) {
				memberIds.push(order.rows[0].id);
				addRowOnMemberList(order.rows[0], false);
				updateTotal();
			});

			membersId.val("");
			members.val("");

		} else {
			$("#members-message").html("Para incluir um atleta na equipe ele precisa se cadastrar no site e ativar a conta.").show();
			members.focus();
		}
	});

	/**
	 * Adiciona um event listener para remoção nos membros inseridos na equipe
	 */
	$("#members-list").on("click", ".remove", function(e) {
		e.preventDefault();

		var teamId = $(this).data("id");
		memberIds.splice($.inArray(teamId, memberIds), 1);

		var row = $(this).parents('tr:first');
		$('#members-list').data('footable').removeRow(row);
		updateTotal();
	});

	/**
	 * TODO Ajustar método Cadastro dos dados da equipe
	 */
	$("form").submit(function(event) {
		event.preventDefault();
		$("[id$='-message']").hide();

		var category = $("#category");

		var data = {
			'teamName' : $("#teamName").val(),
			'category' : category.val() ? category.val().split("#")[0] : "",
			'course' : category.val() ? category.val().split("#")[1] : "",
			'members' : memberIds
		};

		RaceRegistrationProxy.submitRegistration(id, data).done(registrationOk);
	});
});

/* ---------------- Funções de Callback ---------------- */

function getOAuthAppIdsOk(data) {
	$("#facebook-appid").val(data.facebook);
}

/**
 * Carrega dados da corrida
 */
function loadOk(data) {
	$(".race-name").text(data.name)
	$("#race-date").text(moment(data.date, "YYYY-MM-DD").locale("pt-br").format('LL'));
	$("#race-city").text(data.city);
}

/**
 * Monta a caixa de seleção das categorias disponíveis da corrida. TODO Pensar
 * numa estrutura para pegar a quantidade de membros da corrida.
 */
function loadCategoriesOk(data) {
	$.each(data, function(index, value) {
		var course = value;
		$.each(course.categories, function(index, value) {
			$("#category").append(new Option(this.name + " " + course.length + "km", this.id + "#" + course.id));
		});
	});
}

function registrationOk(data) {
	$("[id$='-message']").hide();
	var url = App.getContextPath() + "/inscricao/" + data;

	bootbox.dialog({
		title : "Parabéns",
		message : "Seu pedido de inscrição <strong>#" + data
				+ "</strong> foi registrado com sucesso. Compartilhe esta boa notícia com os seus amigos.",
		buttons : {
			main : {
				label : "Compartilhar no Facebook",
				className : "btn-primary",
				callback : function() {
					shareOnFacebook();
					location.href = url;
				}
			}
		},
		onEscape : function() {
			location.href = url;
		}
	});
}

function registrationFailed(request) {
	switch (request.status) {
		case 500:
			$("#members-message")
					.html(
							"Ocorreu um erro interno no servidor e sua inscrição não foi concluída. Informe ao administrador pelo e-mail: contato@soumaisaventura.com.br")
					.show();
			break;
	}
}

function shareOnFacebook() {
	var raceUrl = App.getBaseUrl() + "/prova/" + $("#id").val();
	var url = "";
	url += "http://www.facebook.com/dialog/feed";
	url += "?app_id=" + $("#facebook-appid").val();
	url += "&name=" + $("#teamName").val() + " vai para a " + $("#race-name").text() + "!";
	url += "&description=Eu inscrevi a minha equipe na prova " + $("#race-name").text() + " que acontecerá no dia " + $("#race-date").text() + " em "
			+ $("#race-city").text() + ".";
	url += "&link=" + raceUrl;
	url += "&picture=" + App.getBaseUrl() + "/api/race/" + $("#id").val() + "/banner";
	url += "&redirect_uri=" + App.getBaseUrl() + "/close";
	url += "&actions=[{ name: 'Quero me inscrever agora mesmo!', link: '" + raceUrl + "/inscricao' }]";

	// var url = "";
	// url += "http://www.facebook.com/sharer/sharer.php?u=";
	// url += App.getBaseUrl() + "/registration/" + $("#registration-id").val()
	// + "/public";

	// console.log(url);

	window.open(url, '_blank');
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

function addRowOnMemberList(athlete, exclude) {
	// if (athlete.name.length > 30) {
	// athlete.name = athlete.name.substr(0, 27).concat("...");
	// }
	var row = "";
	row = row.concat("<tr id='member-" + athlete.id + "'>");
	row = exclude ? row.concat("<td></td>") : row.concat("<td><a href='#' class='remove' data-id='" + athlete.id
			+ "'><span class='glyphicon glyphicon-trash'/></a></td>");
	row = row.concat("<td class='footable-first-column' style='vertical-align:middle;'>" + athlete.name + "</td>");
	row = row.concat("<td class='text-right' nowrap='nowrap' style='vertical-align:middle;'>" + numeral(athlete.racePrice).format() + "</td>");
	row = row.concat("<td class='text-right' nowrap='nowrap' style='vertical-align:middle;'>" + numeral(athlete.annualFee).format() + "</td>");
	row = row.concat("<td class='text-right ammount' nowrap='nowrap' style='vertical-align:middle;' data-ammount='" + athlete.amount + "'><em>"
			+ numeral(athlete.amount).format() + "</em></td>");
	row = row.concat("</tr>");
	// $("#members-list > tbody:last").append(row);
	$('#members-list').data('footable').appendRow(row);
}

function updateTotal() {
	var total = 0;

	$(".ammount").each(function() {
		total += $(this).data("ammount");
	});

	$("#total").text(numeral(total).format());
}
