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
function loadOk($data){
	console.log($data);
	
	var totalAmount = 0;
	
	$("#race\\.name > strong").text($data.race.name);
	$("#race\\.detail > strong").text(moment($data.race.date).format('LL') + " - " + $data.race.city.name + "/" + $data.race.city.state);
	$("#race\\.status > span").text(App.translateStatus($data.status));
	$("#race\\.team > strong").text($data.teamName + " / " + $data.category.name + " " + $data.course.length + " km");
	
	$.each($data.teamFormation, function($i, $member){
		totalAmount += $member.bill.amount;
		var row = "";
		row = row.concat("<tr>");
		row = row.concat("<td class='text-left' style='padding-left: 0px;'>");
		row = row.concat($member.name);
		row = row.concat("<br/>");
		row = row.concat($member.email);
		row = row.concat("<br/>");
		row = row.concat($member.phone);
		row = row.concat("</td>");
		row = row.concat("<td class='text-right' nowrap='nowrap'>" + numeral($member.bill.racePrice).format() + "</td>");
		row = row.concat("<td class='text-right' nowrap='nowrap'>" + numeral($member.bill.annualFee).format() + "</td>");
		row = row.concat("<td class='text-right' nowrap='nowrap'>" + numeral($member.bill.amount).format() + "</td>");
		row = row.concat("</tr>");
		$("#race\\.teamFormation > tbody:last").append(row);
	});
	
	$("#race\\.totalAmount").text(numeral(totalAmount).format());

	$("#race\\.submission").html("Inscrição feita em <strong>" + moment($data.date).format('L') + "</strong> por <strong>" + $data.submitter.name + "</strong>.");
	
	$.each($data.race.organizers, function($i, $organizer){
		console.log($organizer);
		var row = "";
		row = row.concat("<div class='col-md-6 text-center'>");
		row = row.concat("<h4>" + $organizer.name + "</h4>");
		row = $organizer.email ? row.concat("<h5>" + $organizer.email + "</h5>") : row;
		row = $organizer.phone ? row.concat("<h5>" + $organizer.phone + "</h5>") : row;
		row = row.concat("</div>");
		$("#race\\.organizers").append(row);
	})
}
