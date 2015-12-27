$(function() {
	moment.locale("pt-br");
	numeral.language('pt-br');
	numeral.defaultFormat('0.00');

	var memberIds = [];
	var eventId = $("#event_id").val();
	var raceId = $("#race_id").val();

	$("#categoryId").focus();

	$("#categoryId").on("change", function() {
		var teamsize = $(this).find('option:selected').data("teamsize");
		if (teamsize === 1) {
			$("#pesquisa-atleta").hide();
		} else {
			$("#pesquisa-atleta").show();
		}
		updateTotal();
	});

	$('#members-list').footable({
		breakpoints : {
			phone : 450
		}
	});

	LogonProxy.getOAuthAppIds().done(getOAuthAppIdsOk);
	RaceProxy.loadSummary(raceId, eventId).done(loadSummaryOk);
	RaceProxy.findCategories(raceId, eventId).done(loadCategoriesOk);

	var user;

	if (!(user = App.getLoggedInUser())) {
		App.handle401();
	}

	RaceProxy.getOrder(raceId, eventId, user.id).done(function(order) {
		getOrderOk(order, memberIds, false);
	});

	$("#members_ids").autocomplete({
		source : function(request, response) {
			UserProxy.search(request.term, memberIds).done(function(data) {
				response(convertToLabelValueStructureFromUser(data));
			});
		},
		minLength : 3,
		select : function(event, ui) {
			$("#members-id").val(ui.item.value);
			$("#members_ids").val(ui.item.label);
			return false;
		},
		focus : function(event, ui) {
			$("#members-id").val(ui.item.value);
			$("#members_ids").val(ui.item.label);
			return false;
		}
	});

	$("#bt-add-athlete").click(function() {

		var members = $("#members_ids");
		var membersId = $("#members-id");

		$("#members_ids-message").hide();
		if (membersId) {
			RaceProxy.getOrder(raceId, eventId, membersId.val()).done(function(order) {
				getOrderOk(order, memberIds, true);

				console.log(membersId.val());
				console.log(memberIds);
			});
			membersId.val("");
			members.val("");

		} else {
			$("#members_ids-message").html("Para incluir um atleta na equipe ele precisa se cadastrar no site e ativar a conta.").show();
			members.focus();
		}
	});

	$("#members-list").on("click", ".remove", function(e) {
		e.preventDefault();

		var teamId = $(this).data("id");
		memberIds.splice($.inArray(teamId, memberIds), 1);

		var row = $(this).parents('tr:first');
		$('#members-list').data('footable').removeRow(row);
		updateTotal();
	});

	$("form").submit(function(event) {
		event.preventDefault();
		$("[id$='-message']").hide();

		var data = {
			'team_name' : $("#teamName").val(),
			'category_id' : $("#categoryId").val(),
			'members_ids' : memberIds
		};
		RaceRegistrationProxy.submitRegistration(raceId, eventId, data).done(registrationOk);
	});
});

function getOrderOk(order, memberIds, deletable) {
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
}

function getOAuthAppIdsOk(data) {
	$("#facebook-appid").val(data.facebook);
}

function loadSummaryOk(race) {
	$(".race-name").text(race.event.name)
	$("#race-description").text(race.description)
	$("#race-date").text(App.parsePeriod(race.period));
	$("#race-city").text(App.parseCity(race.event.location.city));
}

function loadCategoriesOk(categories) {
	if (categories) {
		$.each(categories, function(i, category) {
			var option = new Option(category.name, category.id);
			$(option).data("teamsize", category.teamSize);
			$(option).data("id", category.id);
			$("#categoryId").append(option);
		});
	}
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

	window.open(url, '_blank');
}

function convertToLabelValueStructureFromUser(data) {
	var newData = [];
	$.each(data, function() {
		newData.push({
			"label" : this.name,
			"value" : this.id
		});
	});
	return newData;
}

function addRowOnMemberList(athlete, deletable) {
	console.log(deletable);

	athlete.deletable = deletable;
	athlete.formmated_race_price = numeral(athlete.race_price).format();

	var template = $("#member-template");
	var rendered = Mustache.render(template.html(), athlete);
	$('#members-list').data('footable').appendRow(rendered);
}

function updateTotal() {
	var total = 0;

	$(".ammount").each(function() {
		total += $(this).data("ammount");
	});

	$("#total").text(numeral(total).format());
}
