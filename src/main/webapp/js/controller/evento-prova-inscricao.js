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
	$("#annual-fee-description").text(App.annualFeeDescription);

	LogonProxy.getOAuthAppIds().done(getOAuthAppIdsOk);
	RaceProxy.loadSummary(raceId, eventId).done(loadSummaryOk);
	RaceProxy.findCategories(raceId, eventId).done(loadCategoriesOk);

	var user;

	if (!(user = App.getLoggedInUser())) {
		App.handle401();
	}

	RaceProxy.order(raceId, eventId, user.id).done(function(order) {
		memberIds.push(order[0].id);
		addRowOnMemberList(order[0], true);
		updateTotal();
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
			RaceProxy.order(raceId, eventId, membersId.val()).done(function(order) {
				memberIds.push(order[0].id);
				addRowOnMemberList(order[0], false);

				/** Refatorar Ini */
				var annualfee = $("#categoryId").find('option:selected').data("annualfee");
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

function getOAuthAppIdsOk(data) {
	$("#facebook-appid").val(data.facebook);
}

function loadSummaryOk(race) {
	$(".race-name").text(race.event.name)
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

	// var url = "";
	// url += "http://www.facebook.com/sharer/sharer.php?u=";
	// url += App.getBaseUrl() + "/registration/" + $("#registration-id").val()
	// + "/public";

	// console.log(url);

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

function addRowOnMemberList(athlete, exclude) {
	console.log(athlete);

	var row = "";
	row = row.concat("<tr id='member-" + athlete.id + "' data-raceprice='" + athlete.race_price + "'>");
	row = exclude ? row.concat("<td></td>") : row.concat("<td><a href='#' class='remove' data-id='" + athlete.id
			+ "'><span class='glyphicon glyphicon-trash'/></a></td>");
	row = row.concat("<td class='footable-first-column' style='vertical-align:middle;'>");
	row = row.concat("<img class='img-rounded' src='" + athlete.picture.thumbnail + "'>&nbsp;&nbsp;&nbsp;");
	row = row.concat(athlete.name);
	row = row.concat("</td>");
	row = row.concat("<td class='ammount text-right' nowrap='nowrap' style='vertical-align:middle;' data-ammount='" + athlete.race_price + "'>"
			+ numeral(athlete.race_price).format() + "</td>");
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
