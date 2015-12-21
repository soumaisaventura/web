$(function() {
	moment.locale("pt-br");
	numeral.language('pt-br');
	numeral.defaultFormat('0.00');

	var memberIds = [];
	var id = $("#id").val();
	var eventId = $("#event_id").val();
	var raceId = $("#race_id").val();

	$("#category").focus();

	/*
	 * Ajusta o total individual Ajusta o total geral Voltar os valores
	 * originais quando a corrida cobrar annualfee
	 */
	$("#category").on("change", function() {
		var teamsize = $(this).find('option:selected').data("teamsize");
		if (teamsize === 1) {
			$("#pesquisa-atleta").hide();
		} else {
			$("#pesquisa-atleta").show();
		}
		// Verifica se o percurso vai cobrar annualfee
		var annualfee = $(this).find('option:selected').data("annualfee");
		var membersWithAnnualFee = ($('[id^="member-"]').filter('[data-original-annualfee!="0"]'));
		$.each(membersWithAnnualFee, function(i, member) {
			if (!annualfee) {
				resetMemberFee(member);
			} else {
				recoveryMemberFee(member);
			}
		});
		updateTotal();
	});

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
	RaceProxy.loadSummary(raceId, eventId).done(loadSummaryOk);

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
		memberIds.push(order[0].id);
		addRowOnMemberList(order[0], true);
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
				memberIds.push(order[0].id);
				addRowOnMemberList(order[0], false);

				/** Refatorar Ini */
				var annualfee = $("#category").find('option:selected').data("annualfee");
				var membersWithAnnualFee = ($('[id^="member-"]').filter('[data-original-annualfee!="0"]'));
				$.each(membersWithAnnualFee, function(i, member) {
					if (!annualfee) {
						resetMemberFee(member);
					} else {
						recoveryMemberFee(member);
					}
				});
				/** Refatorar Fim */

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
function loadSummaryOk(race) {
	$(".race-name").text(race.event.name)
	$("#race-date").text(App.parsePeriod(race.period));
	$("#race-city").text(race.event.location.city.name + " / " + race.event.location.city.state);
}

/**
 * Monta a caixa de seleção das categorias disponíveis da corrida. TODO Pensar
 * numa estrutura para pegar a quantidade de membros da corrida.
 */
function loadCategoriesOk(data) {
	var option;
	$.each(data, function(index, course) {
		$.each(course.categories, function(index, category) {
			option = new Option(course.name + " – " + this.name, this.id + "#" + course.id);
			$(option).data("annualfee", course.annualFee);
			$(option).data("teamsize", category.teamSize);
			$("#category").append(option);
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
	var row = "";
	row = row.concat("<tr id='member-" + athlete.id + "' data-raceprice='" + athlete.racePrice + "' data-original-annualfee='" + athlete.annualFee
			+ "' data-annualfee='" + athlete.annualFee + "'>");
	row = exclude ? row.concat("<td></td>") : row.concat("<td><a href='#' class='remove' data-id='" + athlete.id
			+ "'><span class='glyphicon glyphicon-trash'/></a></td>");
	row = row.concat("<td class='footable-first-column' style='vertical-align:middle;'>" + athlete.name + "</td>");
	row = row.concat("<td class='ammount text-right' nowrap='nowrap' style='vertical-align:middle;' data-ammount='" + athlete.racePrice + "'>"
			+ numeral(athlete.racePrice).format() + "</td>");
	row = row.concat("</tr>");
	$('#members-list').data('footable').appendRow(row);
}

function updateTotal() {
	var total = 0;

	$(".ammount").each(function() {
		total += $(this).data("ammount");
	});

	$("#total").text(numeral(total).format());
}

function resetMemberFee(member) {
	var $member = $(member);
	var annualfee = 0;
	var ammount = $member.data("raceprice");
	$member.data("annualfee", annualfee);
	$member.data("ammount", ammount);
	$member.children(":nth-child(4)").html("R$ " + annualfee);
	$member.children(":nth-child(5)").data("ammount", ammount); // Verificar o
	// melhor o
	// local para
	// colocar o
	// ammount
	$member.children(":nth-child(5)").html("R$ " + ammount);
}

function recoveryMemberFee(member) {
	var $member = $(member);
	var annualfee = $member.data("original-annualfee");
	var raceprice = $member.data("raceprice");
	var ammount = annualfee + raceprice;
	$member.data("ammount", ammount);
	$member.children(":nth-child(4)").html("R$ " + annualfee);
	$member.children(":nth-child(5)").data("ammount", ammount); // Verificar o
	// melhor o
	// local para
	// colocar o
	// ammount
	$member.children(":nth-child(5)").html("R$ " + ammount);
}
