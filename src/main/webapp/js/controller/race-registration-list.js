$(function() {
	moment.locale("pt-br");
	var $race = $("#race").val();

	RaceProxy.loadSummary($race).done(loadOk);
	RaceRegistrationProxy.find($race).done(findOk);
});

function loadOk($data) {
	$("#race-name").text($data.name)
	$("#race-date").text(moment($data.date, "YYYY-MM-DD").locale("pt-br").format('LL'));
	$("#race-city").text($data.city);
}

function findOk($data, $status, $request) {
	switch ($request.status) {
		case 204:
			var message = "Nenhuma equipe se inscreveu ainda."
			$('#resultTable > tbody:last').append('<tr><td>' + message + '</td></tr>');

			break;

		case 200:
			console.log("xxxxxxxx");
			break;
	}
}
