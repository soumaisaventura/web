$(function() {
	moment.locale("pt-br");
	numeral.language('pt-br');
	numeral.defaultFormat('0.00');

	/**
	 * Carrega dados da inscrição
	 */
	RegistrationProxy.load($("#registration").val()).done(loadOk);
});

/* ---------------- Funções de Callback ---------------- */

function sendPaymentOk(data, status, request) {
	openPaymentFlow(data);
	updatePaymentButton(data);
}

function sendPaymentFailed(request) {
	switch (request.status) {
		case 502:
			bootbox.alert("Ocorreu uma falha na comunicação com o PagSeguro. Espere alguns minutos e tente novamente.");
			break;
	}
}

function updateBreadcrumb(data) {
	var user = App.getLoggedInUser();

	var organizer = false;
	if (data.race.organizers) {
		$.each(data.race.organizers, function(i, value) {
			if (user.id === value.id) {
				organizer = true;
				return;
			}
		});
	}

	if (organizer || user.admin) {
		$(".breadcrumb.organizer").show();

		$("#race-link").attr("href", App.getContextPath() + "/prova/" + data.race.id);
		$("#dashboard-link").attr("href", App.getContextPath() + "/prova/" + data.race.id + "/painel");
		$("#registration-link").attr("href", App.getContextPath() + "/prova/" + data.race.id + "/painel/inscricoes");

	} else {
		$("#registration-list-menu-item").addClass("active");
		$(".breadcrumb.athlete").show();
	}
}

function loadOk(registration) {
	updateBreadcrumb(registration);

	$("#registration-id").text(registration.number);
	$("#team-name").text(registration.team.name);
	$("#race-status").html(App.translateStatus(registration.status));
	$("#race-status").data('status', registration.status);
	$("#summary-section").show();

	$(".race-name").text(registration.race.event.name);
	$("#race-date").text(moment(registration.race.period.beginning).format('LL'));
	$("#race-city").text(registration.race.event.location.city.name + "/" + registration.race.event.location.city.state);
	$("#race-section").show();

	$("#race-category").text(registration.category.name + " " + registration.race.name);

	var user = App.getLoggedInUser();
	var isMember = false;
	//
	$.each(registration.team.members, function(i, member) {
		var row = "";
		row = row.concat("<tr>");
		row = row.concat("<td class='text-left' style='padding-left: 0px;'>");
		row = row.concat("<span class='glyphicon glyphicon-user' aria-hidden='true' style='font-size: 0.8em'></span>&nbsp;");
		row = row.concat("<span style='font-size: 1.0em'>" + member.name + "</span>");
		row = row.concat("<br/>");
		row = row.concat("<span class='glyphicon glyphicon-envelope' aria-hidden='true' style='font-size: 0.8em'></span>&nbsp;");
		row = row.concat(member.email);
		row = row.concat("<br/>");
		row = row.concat("<span class='glyphicon glyphicon-phone' aria-hidden='true' style='font-size: 0.8em'></span>&nbsp;");
		row = row.concat(member.mobile);
		row = row.concat("</td>");
		row = row.concat("<td class='text-right' nowrap='nowrap' style='vertical-align:middle;'>R$ ");
		row = row.concat("<a href='#' data-pk='" + member.id + "' data-params='{property: \"racePrice\"}' class='partial editable currency'>"
				+ numeral(member.race_price).format() + "</a>");
		row = row.concat("</td>");
		row = row.concat("</tr>");
		$("#team-formation > tbody:last").append(row);

		if (user && member.id == user.id) {
			isMember = true;
		}
	});

	updateTotal();
	$("#team-section").show();

	if (registration.status == 'pendent') {
		$(".payment-type").hide();
		$(".payment-type-" + registration.race.event.payment.type).show();
		$('#registration-payment-info').html(
				registration.race.event.payment.info ? App.replaceEmailByMailTo(registration.race.event.payment.info.replace(/\n|\r/g, '<br>')) : '');
		updatePaymentButton(registration.payment.code, registration.payment.transaction);
		$('#payment-section').show();
	}

	var isAuthorized = user ? user.admin : null;

	if (registration.race.event.organizers) {
		$.each(registration.race.event.organizers, function(i, organizer) {
			var row = "";
			row = row.concat("<div class='col-md-6' style='padding-bottom: 30px;'>");
			row = row.concat("<span class='glyphicon glyphicon-user' aria-hidden='true' style='font-size: 0.8em'></span>&nbsp;");
			row = row.concat(organizer.name);
			row = row.concat("<br/>");
			row = row.concat("<span class='glyphicon glyphicon-envelope' aria-hidden='true' style='font-size: 0.8em'></span>&nbsp;");
			row = row.concat(organizer.email);

			if (organizer.mobile) {
				row = row.concat("<br/>");
				row = row.concat("<span class='glyphicon glyphicon-phone' aria-hidden='true' style='font-size: 0.8em'></span>&nbsp;");
				row = row.concat(organizer.mobile);
			}

			if (user && organizer.id == user.id) {
				isAuthorized = true;
			}

			row = row.concat("</div>");
			$("#race-organizers").append(row);
		});
		$("#organizers-section").show();
	}

	$("#registration-submitter").text(registration.submitter.name);
	$("#registration-date").text(moment(registration.date).format('L'));
	$("#footer-section").show();

	loadEditableCurrency(registration.id, isAuthorized && registration.status == 'pendent');
	loadEditableTeamName(registration.id, registration.race.status == 'open' && (isAuthorized || isMember));
}

function loadEditableCurrency(registrationId, enabled) {
	$(".editable").editable({
		type : 'text',
		disabled : !enabled,
		emptyclass : '',
		display : function(value, sourceData) {
			$(this).text(numeral(value).format())
		},
		url : function(params) {
			var d = new $.Deferred;
			var value = JSON.stringify(numeral().unformat(params.value));

			var updateValuesOk = function(data) {
				d.resolve();
				updateTotal();
				updatePaymentButton();
			};

			var updateValuesFailed = function(request) {
				var message = null;
				if (request.status == 422) {
					message = request.responseJSON[0].message;
				}

				d.reject(message);
			};

			if (params.property === 'racePrice') {
				RegistrationProxy.updateRacePrice(registrationId, params.pk, value).done(updateValuesOk).fail(updateValuesFailed);
			}

			return d.promise();
		}
	});

	$('.editable').on('shown', function() {
		var input = $(this).data('editable').input.$input;

		input.mask('##0,00', {
			selectOnFocus : true,
			placeholder : "0",
			reverse : true
		});
	});
}

function loadEditableTeamName(registrationId, enabled) {
	$('#team-name').editable({
		type : 'text',
		disabled : !enabled,
		emptyclass : '',
		url : function(params) {
			var d = new $.Deferred;

			var updateValuesOk = function(data) {
				d.resolve();
			};

			var updateValuesFailed = function(request) {
				var message = null;
				if (request.status == 422) {
					message = request.responseJSON[0].message;
				}

				d.reject(message);
			};

			RegistrationProxy.updateTeamName(registrationId, params.value).done(updateValuesOk).fail(updateValuesFailed);

			return d.promise();
		}
	});
}

function updateTotal() {
	var partials = {};
	$(".editable").each(function() {
		var key = $(this).data("pk");
		var value = numeral().unformat($(this).text());
		partials[key] = (partials[key] ? partials[key] : 0.0) + value;
	});

	var total = 0;
	$(".partial").each(function() {
		var key = $(this).data("pk");
		var value = partials[key];
		$(this).text(numeral(value).format());
		total += value;
	});

	$("#payment-ammount").data("value", total);
	$("#payment-ammount").text("R$ " + numeral(total).format());
}

function updatePaymentButton(code, transaction) {
	if ($("#payment-ammount").data("value") == 0 || $("#race-status").data('status') != 'pendent') {
		$("#payment-section").hide();
	} else {
		$("#payment-section").show();
	}

	$("#payment").unbind('click');
	if (transaction) {
		$("#payment-alert").show();
		$("#payment").attr("disabled", true);

	} else if (code) {
		$("#payment-alert").hide();
		$("#payment").attr("disabled", false);

		$("#payment").click(function() {
			openPaymentFlow(code);
		});
	} else {
		$("#payment-alert").hide();
		$("#payment").attr("disabled", false);

		$("#payment").click(function() {
			RegistrationProxy.sendPayment($("#registration").val()).done(sendPaymentOk).fail(sendPaymentFailed);
		});
	}
}

function openPaymentFlow(code) {
	// window.open('https://pagseguro.uol.com.br/v2/checkout/payment.html?code='
	// + code);
	location.href = 'https://pagseguro.uol.com.br/v2/checkout/payment.html?code=' + code;
}
