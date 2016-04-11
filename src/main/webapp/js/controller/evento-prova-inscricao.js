$(function() {
	moment.locale("pt-br");
	numeral.language('pt-br');
	numeral.defaultFormat('0.00');

	var registration = new Object();
	
	var user;
	var memberIds = [];
	var eventId = $("#event_id").val();
	var raceId = $("#race_id").val();
	var isOrganizer = false;

	LogonProxy.getOAuthAppIds().done(getOAuthAppIdsOk);
	RaceProxy.loadSummary(raceId, eventId).done(loadSummaryOk);
	RaceProxy.findCategories(raceId, eventId).done(loadCategoriesOk);
	
	if (!(user = App.getLoggedInUser())) {
		App.handle401();
	}

	EventProxy.load(eventId).done(function(event) {
		if (user && user.roles && user.roles.organizer && event.organizers && event.organizers.length > 0) {
			$.each(event.organizers, function(index, value) {
				if (value.id == user.id) {
					isOrganizer = true;
					return false;
				}
			});
		}
		RaceProxy.getOrder(raceId, eventId, user.id).done(function(order) {
			getOrderOk(order, memberIds, isOrganizer);
		});
	});

	$('#members-list').footable({
		breakpoints : {
			phone : 450
		}
	});

	$("#categoryId").focus();
	$("#categoryId").on("change", function() {
		updateTable();
	});

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

	$("#members-list").on("click", ".remove", function(e) {
		e.preventDefault();
		var teamId = $(this).data("id");
		memberIds.splice($.inArray(teamId, memberIds), 1);
		var row = $(this).parents('tr:first');
		$('#members-list').data('footable').removeRow(row);
		updateTable();
	});

	$("form").submit(function(event) {
		event.preventDefault();
		$("[id$='-message']").hide();
		
		registration.category.id = $("#categoryId").val();
		registration.team.name = $("#teamName").val();
		registration.team.members = memberIds; 
		
		RaceRegistrationProxy.submitRegistration(raceId, eventId, data).done(registrationOk).fail(App.handle422Global);
	});
	
	
	$("#kits-modal").on('show.bs.modal', function (event) {
		var button = $(event.relatedTarget);
	    var memberId = button.data('id');
	    var memberName = button.data('name');
	    var modal = $(this);
	    var template = $("#kits-template");
	    modal.find('.modal-title').text('Escolha o kit de ' + memberName);
		RaceProxy.findKits(raceId, eventId).done(function(kits){
			$.each(kits, function(index, kit){
				kit.memberId = memberId;
			});
			console.log(kits);
			var rendered = Mustache.render(template.html(), { "kits" : kits });
			modal.find('.modal-body > .row').html(rendered);
		});
	});
	
	
	$(document).on("click",".kit-choice",function(){
		var kitChoice = $(this).data("kit-id");
		var kitName = $(this).data("kit-name");
		var memberId = $(this).data("member-id");
		
		console.log("kitChoice: " + kitChoice);
		console.log("kitName: " + kitName);
		console.log("memberId: " + memberId);
		
		$("#kit-" + memberId).html(kitName);
		$('#kits-modal').modal('hide');
		
	});
	
});

/* ============================CALLBACK============================ */
function getOAuthAppIdsOk(data) {
	$("#facebook-appid").val(data.facebook);
}

function loadSummaryOk(race) {
	$("#race-name").text(race.event.name);
	$("#race-description").text(race.description);
	$("#race-date").text(App.parsePeriod(race.period));
	$("#race-city").text(App.parseCity(race.event.location.city));
}

function loadCategoriesOk(categories) {
	if (categories) {
		$.each(categories, function(i, category) {
			var option = new Option(category.name, category.id);
			$(option).data("teamsize", category.team_size);
			$(option).data("id", category.id);
			$("#categoryId").append(option);
		});

		if (categories.length === 1 && categories[0].team_size !== 1) {
			$("#categoryId").val(categories[0].id);
			$("#pesquisa-atleta").show();
		}
	}
}

function getOrderOk(order, memberIds, deletable) {
	if (order) {
		memberIds.push(order[0].id);
		addRowOnMemberList(order[0], deletable);
		
		updateTable();
		
		$("#summary-section, #submit-button-section, #members-section").show();
	} else {
		var url = $("#event_link").attr("href");
		window.location.href = url;
	}
}

function addRowOnMemberList(athlete, deletable) {
	athlete.deletable = deletable;
	athlete.formmated_race_price = numeral(athlete.race_price).format();

	var template = $("#member-template");
	var rendered = Mustache.render(template.html(), athlete);
	$('#members-list').data('footable').appendRow(rendered);
}

function updateTable(){
	var rowCount = $('#members-list tbody tr').length;
	var teamsize = $('#categoryId').find('option:selected').data("teamsize");
	if(!rowCount) {
		$("#pesquisa-atleta").show();
	} else {
		if (teamsize > 1 && rowCount < teamsize) {
			$("#pesquisa-atleta").show();
		} else {
			$("#pesquisa-atleta").hide();
		}
	}
	updateTotal();
}

function updateTotal() {
	var total = 0;
	$(".ammount").each(function() {
		total += $(this).data("ammount");
	});
	$("#total").text(numeral(total).format());
}

function convertToLabelValueStructureFromUser(data) {
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