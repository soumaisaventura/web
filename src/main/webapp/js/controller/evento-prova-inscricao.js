$(function() {

	/**
	 * Configurações iniciais
	 */
	moment.locale("pt-br");
	numeral.language('pt-br');
	numeral.defaultFormat('0.00');

	/**
	 * Variáveis
	 */
	var user;
	var memberIds = [];
	var eventId = $("#event_id").val();
	var raceId = $("#race_id").val();

	/**
	 * Carrega o id do facebook para poder compartilhar a inscrição
	 */
	LogonProxy.getOAuthAppIds().done(getOAuthAppIdsOk);

	/**
	 * Carrega os dados da corrida
	 */
	RaceProxy.loadSummary(raceId, eventId).done(loadSummaryOk);

	/**
	 * Carrega a combo de categorias
	 */
	RaceProxy.findCategories(raceId, eventId).done(loadCategoriesOk);

	/**
	 * Carrega o usuário que está logado
	 */
	if (!(user = App.getLoggedInUser())) {
		App.handle401();
	}

	console.log('--------------- user --------------- ');
	console.log(user);

	/**
	 * Coloca o usuário logado na lista de membros da equipe
	 */
	RaceProxy.getOrder(raceId, eventId, user.id).done(function(order) {
		console.log('--------------- order --------------- ');
		console.log(order);
		console.log(memberIds);
		getOrderOk(order, memberIds, false);
	});

	/**
	 * Ajusta a tabela para quando for visualizada em celulares
	 */
	$('#members-list').footable({
		breakpoints : {
			phone : 450
		}
	});

	/**
	 * Coloca o foco na combo de categoria Trigger que verifica se o tamanho da
	 * equipe é igual a 1
	 */
	$("#categoryId").focus();
	$("#categoryId").on("change", function() {
		var teamsize = $(this).find('option:selected').data("teamsize");

		console.log('teamsize = ' + teamsize);

		if (teamsize === 1) {
			$("#pesquisa-atleta").hide();
		} else {
			$("#pesquisa-atleta").show();
		}
		updateTotal();
	});

	/**
	 * Transforma o campo para inserir membro para o tipo autocomplete A função
	 * _renderItem está sendo usada para mostrar a imagem do atleta na busca
	 */
	$("#members_ids").autocomplete({
		source : function(request, response) {
			UserProxy.search(request.term, memberIds).done(function(data) {
				response(convertToLabelValueStructureFromUser(data));
			});
		},
		minLength : 3,
		select : function(event, ui) {
			$("#memberId").val(ui.item.value);
			$("#members_ids").val(ui.item.label);
			addAthlete(raceId, eventId, memberIds);
			return false;
		},
		focus : function(event, ui) {
			$("#memberId").val(ui.item.value);
			$("#members_ids").val(ui.item.label);

			return false;
		}
	}).autocomplete("instance")._renderItem = function(ul, item) {
		return $("<li></li>").data("data-value", item).append("<img src='" + item.thumbnail + "'> " + item.label).appendTo(ul);
	};

	/**
	 * Remove um membro da equipe
	 */
	$("#members-list").on("click", ".remove", function(e) {
		e.preventDefault();
		var teamId = $(this).data("id");
		console.log('Removendo atleta = ' + teamId);
		memberIds.splice($.inArray(teamId, memberIds), 1);
		var row = $(this).parents('tr:first');
		$('#members-list').data('footable').removeRow(row);
		updateTotal();
	});

	/**
	 * Confirma a inscrição
	 */

	$("form").submit(function(event) {
		event.preventDefault();
		$("[id$='-message']").hide();

		console.log('memberIds');
		console.log(memberIds);

		var data = {
			'team_name' : $("#teamName").val(),
			'category_id' : $("#categoryId").val(),
			'members_ids' : memberIds
		};
		RaceRegistrationProxy.submitRegistration(raceId, eventId, data).done(registrationOk);
	});
});

/* ============================CALLBACK============================ */

function getOAuthAppIdsOk(data) {
	console.log('1: getOAuthAppIdsOk');
	$("#facebook-appid").val(data.facebook);
}

function loadSummaryOk(race) {
	console.log('2: loadSummaryOk');
	$("#race-name").text(race.event.name)
	$("#race-description").text(race.description)
	$("#race-date").text(App.parsePeriod(race.period));
	$("#race-city").text(App.parseCity(race.event.location.city));
}

function loadCategoriesOk(categories) {
	console.log('3: loadCategoriesOk');
	if (categories) {
		$.each(categories, function(i, category) {
			var option = new Option(category.name, category.id);
			$(option).data("teamsize", category.team_size);
			$(option).data("id", category.id);
			$("#categoryId").append(option);
		});

		if (categories.length === 1) {
			$("#categoryId").val(categories[0].id);
			$("#pesquisa-atleta").hide();
		}
	}
}

function getOrderOk(order, memberIds, deletable) {
	console.log('4: getOrderOk');
	console.log('--- antes ---');
	console.log(memberIds);
	if (order) {
		memberIds.push(order[0].id);
		addRowOnMemberList(order[0], deletable);
		updateTotal();
		$("#summary-section, #submit-button-section, #members-section").show();
	} else {
		console.log("Fora do período de inscrição!");
		var url = $("#event_link").attr("href");
		window.location.href = url;
	}
	console.log('--- depois--');
	console.log(memberIds);
}

function addRowOnMemberList(athlete, deletable) {
	console.log('5: addRowOnMemberList');
	athlete.deletable = deletable;
	athlete.formmated_race_price = numeral(athlete.race_price).format();

	var template = $("#member-template");
	var rendered = Mustache.render(template.html(), athlete);
	$('#members-list').data('footable').appendRow(rendered);
}

function updateTotal() {
	console.log('6: updateTotal');
	var total = 0;
	$(".ammount").each(function() {
		total += $(this).data("ammount");
	});
	$("#total").text(numeral(total).format());
}

function convertToLabelValueStructureFromUser(data) {
	console.log('7: convertToLabelValueStructureFromUser');
	var newData = [];
	$.each(data, function() {
		newData.push({
			"label" : this.name,
			"value" : this.id,
			"thumbnail" : this.picture.thumbnail
		});
	});
	return newData;
}

function addAthlete(raceId, eventId, memberIds) {
	console.log('8: addAthlete');
	var memberId = $("#memberId");
	var members_ids = $("#members_ids");
	$("#members_ids-message").hide();

	if (memberId) {
		RaceProxy.getOrder(raceId, eventId, memberId.val()).done(function(order) {
			getOrderOk(order, memberIds, true);
		});
		memberId.val(""); // Limpa o campo
		members_ids.val(""); // Limpa o campo
	} else {
		$("#members_ids-message").html("Para incluir um atleta na equipe ele precisa se cadastrar no site e ativar a conta.").show();
		members.focus();
	}
}

function registrationOk(data) {
	console.log('9: registrationOk');
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

function shareOnFacebook() {
	console.log('10: shareOnFacebook');
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

	window.open(url, '_blank');
}