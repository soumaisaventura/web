$(function() {
	moment.locale("pt-br");
	numeral.language('pt-br');
	numeral.defaultFormat('$ 0,0.00');

	/**
	 * Carrega dados da inscrição
	 */
	RegistrationProxy.load($("#registration").val()).done(loadOk);
});

/* ---------------- Funções de Callback ---------------- */

function sendPaymentOk($data, $status, $request) {
	window.open($request.getResponseHeader('Location'), "_bank");
	$("#transaction").val($data);
	refreshPaymentButton();
}

function sendPaymentFailed($request) {
	switch ($request.status) {
		case 502:
			bootbox.alert("Ocorreu uma falha na comunicação com o PagSeguro. Espere alguns minutos e tente novamente.");
			break;
	}
}

/**
 * Carrega dados da inscrição
 */
function loadOk($data) {
	var totalAmount = 0;

	$("#registration-id").text($data.number);
	$("#team-name").text($data.teamName);
	$("#race-status").html(App.translateStatus($data.status));
	$("#summary-section").show();

	$("#race-name").text($data.race.name);
	$("#race-date").text(moment($data.race.date).format('LL'));
	$("#race-city").text($data.race.city.name + "/" + $data.race.city.state);
	$("#race-section").show();

	$("#race-category").text($data.category.name + " " + $data.course.length + " km");
	$.each($data.teamFormation, function($i, $member) {
		totalAmount += $member.bill.amount;
		var row = "";
		row = row.concat("<tr>");
		row = row.concat("<td class='text-left' style='padding-left: 0px;'>");
		row = row.concat("<span class='glyphicon glyphicon-user' aria-hidden='true' style='font-size: 0.8em'></span>&nbsp;");
		row = row.concat("<span style='font-size: 1.0em'>" + $member.name + "</span>");
		row = row.concat("<br/>");
		row = row.concat("<span class='glyphicon glyphicon-envelope' aria-hidden='true' style='font-size: 0.8em'></span>&nbsp;");
		row = row.concat($member.email);
		row = row.concat("<br/>");
		row = row.concat("<span class='glyphicon glyphicon-phone' aria-hidden='true' style='font-size: 0.8em'></span>&nbsp;");
		row = row.concat($member.phone);
		row = row.concat("</td>");
		row = row.concat("<td class='text-right' nowrap='nowrap' style='vertical-align:middle;'>" + numeral($member.bill.racePrice).format()
				+ "</td>");
		row = row.concat("<td class='text-right' nowrap='nowrap' style='vertical-align:middle;'>" + numeral($member.bill.annualFee).format()
				+ "</td>");
		row = row.concat("<td class='text-right' nowrap='nowrap' style='vertical-align:middle;'><em>" + numeral($member.bill.amount).format()
				+ "</em></td>");
		row = row.concat("</tr>");
		$("#team-formation > tbody:last").append(row);
	});
	$("#bill-total-amount").text(numeral(totalAmount).format());
	$("#annual-fee-description").text(App.annualFeeDescription);
	$("#team-section").show();

	$("#transaction").val($data.transaction);
	if ($data.status == 'pendent') {
		$('#payment-section').show();
	}
	refreshPaymentButton();

	$.each($data.race.organizers, function($i, $organizer) {
		var row = "";
		row = row.concat("<div class='col-md-6'>");
		row = row.concat("<span class='glyphicon glyphicon-user' aria-hidden='true' style='font-size: 0.8em'></span>&nbsp;");
		row = row.concat($organizer.name);
		row = row.concat("<br/>");
		row = row.concat("<span class='glyphicon glyphicon-envelope' aria-hidden='true' style='font-size: 0.8em'></span>&nbsp;");
		row = row.concat($organizer.email);

		if ($organizer.phone) {
			row = row.concat("<br/>");
			row = row.concat("<span class='glyphicon glyphicon-phone' aria-hidden='true' style='font-size: 0.8em'></span>&nbsp;");
			row = row.concat($organizer.phone);
		}

		row = row.concat("</div>");
		$("#race-organizers").append(row);
	});
	$("#organizers-section").show();

	$("#registration-submitter").text($data.submitter.name);
	$("#registration-date").text(moment($data.date).format('L'));
	$("#footer-section").show();
}

function refreshPaymentButton() {
	var transaction = $("#transaction").val();

	if (transaction) {
		$("#payment-description").text('Continuar o pagamento');
		$("#payment").click(function() {
			window.open('https://pagseguro.uol.com.br/v2/checkout/payment.html?code=' + transaction, '_blank').show();
		});

	} else {
		$("#payment-description").text('Pagar com PagSeguro');
		$("#payment").click(function() {
			RegistrationProxy.sendPayment($("#registration").val()).done(sendPaymentOk).fail(sendPaymentFailed).show();
		});
	}
}
