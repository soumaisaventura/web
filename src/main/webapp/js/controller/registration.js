$(function() {
	moment.locale("pt-br");
	numeral.language('pt-br');
	numeral.defaultFormat('0,0.00');

	/**
	 * Carrega dados da inscrição
	 */
	RegistrationProxy.load($("#registration").val()).done(loadOk);
});

/* ---------------- Funções de Callback ---------------- */

function sendPaymentOk(data, status, request) {
	window.open(request.getResponseHeader('Location'), "_bank");
	$("#transaction").val(data);
	updatePaymentButton();
}

function sendPaymentFailed(request) {
	switch (request.status) {
		case 502:
			bootbox.alert("Ocorreu uma falha na comunicação com o PagSeguro. Espere alguns minutos e tente novamente.");
			break;
	}
}

/**
 * Carrega dados da inscrição
 */
function loadOk(data) {
	$("#registration-id").text(data.number);
	$("#team-name").text(data.teamName);
	$("#race-status").html(App.translateStatus(data.status));
	$("#race-status").data('status', data.status);
	$("#summary-section").show();

	$("#race-name").text(data.race.name);
	$("#race-date").text(moment(data.race.date).format('LL'));
	$("#race-city").text(data.race.city.name + "/" + data.race.city.state);
	$("#race-section").show();

	$("#race-category").text(data.category.name + " " + data.course.length + " km");
	$.each(data.teamFormation, function(i, member) {
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
		row = row.concat(member.phone);
		row = row.concat("</td>");
		row = row.concat("<td class='text-right' nowrap='nowrap' style='vertical-align:middle;'>");
		row = row.concat("<a href='#' data-pk='" + member.id + "' data-params='{property: \"racePrice\"}' class='editable'>"
				+ numeral(member.bill.racePrice).format() + "</a>");
		row = row.concat("</td>");
		row = row.concat("<td class='text-right' nowrap='nowrap' style='vertical-align:middle;'>");
		row = row.concat("<a href='#' data-pk='" + member.id + "' data-params='{property: \"annualFee\"}' class='editable'>"
				+ numeral(member.bill.annualFee).format() + "</a>");
		row = row.concat("</td>");
		row = row.concat("<td class='text-right' nowrap='nowrap' style='vertical-align:middle;'>");
		row = row.concat("<em class='partial' data-pk='" + member.id + "'>" + numeral(member.bill.amount).format() + "</em>");
		row = row.concat("</td>");
		row = row.concat("</tr>");
		$("#team-formation > tbody:last").append(row);
	});

	updateTotal();
	$("#annual-fee-description").text(App.annualFeeDescription);
	$("#team-section").show();

	$("#transaction").val(data.transaction);
	if (data.status == 'pendent') {
		$('#payment-section').show();
	}
	updatePaymentButton();

	var user = App.getLoggedInUser();
	var authorized = user ? user.admin : null;

	$.each(data.race.organizers, function(i, organizer) {
		var row = "";
		row = row.concat("<div class='col-md-6'>");
		row = row.concat("<span class='glyphicon glyphicon-user' aria-hidden='true' style='font-size: 0.8em'></span>&nbsp;");
		row = row.concat(organizer.name);
		row = row.concat("<br/>");
		row = row.concat("<span class='glyphicon glyphicon-envelope' aria-hidden='true' style='font-size: 0.8em'></span>&nbsp;");
		row = row.concat(organizer.email);

		if (organizer.phone) {
			row = row.concat("<br/>");
			row = row.concat("<span class='glyphicon glyphicon-phone' aria-hidden='true' style='font-size: 0.8em'></span>&nbsp;");
			row = row.concat(organizer.phone);
		}

		if (user && organizer.email == user.email) {
			authorized = true;
		}

		row = row.concat("</div>");
		$("#race-organizers").append(row);
	});
	$("#organizers-section").show();

	$("#registration-submitter").text(data.submitter.name);
	$("#registration-date").text(moment(data.date).format('L'));
	$("#footer-section").show();

	loadEditable(data.id, authorized && data.status == 'pendent');
}

function loadEditable(registerId, enabled) {
	$(".editable").editable({
		// type : 'text',
		// defaultValue : '0,00',
		// emptytext : '0,00',
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

				$("#transaction").val('');
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
				RegistrationProxy.updateRacePrice(registerId, params.pk, value).done(updateValuesOk).fail(updateValuesFailed);
			} else if (params.property === 'annualFee') {
				RegistrationProxy.updateAnnualFee(registerId, params.pk, value).done(updateValuesOk).fail(updateValuesFailed);
			}

			return d.promise();
		}
	});

	$('.editable').on('shown', function() {
		var input = $(this).data('editable').input.$input;

		input.mask('#.##0,00', {
			selectOnFocus : true,
			placeholder : "0,00",
			reverse : true
		});
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
	$("#payment-ammount").text(numeral(total).format());
}

function updatePaymentButton() {
	var transaction = $("#transaction").val();

	if ($("#payment-ammount").data("value") == 0 || $("#race-status").data('status') != 'pendent') {
		$("#payment-section").hide();
	} else {
		$("#payment-section").show();
	}

	if (transaction) {
		$("#payment-description").text('Continuar o pagamento');
		$("#payment").click(function() {
			window.open('https://pagseguro.uol.com.br/v2/checkout/payment.html?code=' + transaction, '_blank').show();
		});
	} else {
		$("#payment-description").text('Pagar com PagSeguro');
		$("#payment").click(function() {
			RegistrationProxy.sendPayment($("#registration").val()).done(sendPaymentOk).fail(sendPaymentFailed);
		});
	}
}
