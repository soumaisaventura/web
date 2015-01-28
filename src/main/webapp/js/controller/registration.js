$(function() {
	moment.locale("pt-br");
	numeral.language('pt-br');
	numeral.defaultFormat('$ 0,0');

	/**
	 * Carrega dados da inscrição
	 */
	RegistrationProxy.load($("#registration").val()).done(loadOk);

});

/* ---------------- Funções de Callback ---------------- */

/**
 * Carrega dados da inscrição
 */
function loadOk($data) {
	console.log($data);

	var totalAmount = 0;

	$("#registration-id").text($data.number);

	$("#race-name").text($data.race.name);
	$("#race-date").text(moment($data.race.date).format('LL'));
	$("#race-city").text($data.race.city.name + "/" + $data.race.city.state);
	$("#race-status").text(App.translateStatus($data.status));
	$("#team-name").text($data.teamName);
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

	console.log(App.annualFeeDescription);

	$("#annual-fee-description").text(App.annualFeeDescription);

	$.each($data.race.organizers, function($i, $organizer) {
		console.log($organizer);
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

	$("#registration-submitter").text($data.submitter.name);

	$("#registration-date").text(moment($data.date).format('L'));
}
