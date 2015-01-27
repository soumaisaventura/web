$(function() {
	moment.locale("pt-br");
	var $race = $("#race").val();

	$('.footable').footable();
	
	RaceProxy.loadSummary($race).done(loadOk);
	//RaceRegistrationProxy.find($race).done(findOk);
	
	
});

function loadOk($data) {
	$("#race-name").text($data.name)
	$("#race-date").text(moment($data.date, "YYYY-MM-DD").locale("pt-br").format('LL'));
	$("#race-city").text($data.city);
}

function findOk($data, $status, $request) {
	console.log($data);
	switch ($request.status) {
		case 204:
			var message = "Nenhuma equipe se inscreveu ainda."
			$('#resultTable > tbody:last').append('<tr><td colspan="6">' + message + '</td></tr>');

			break;

		case 200:
			$.each($data, function(index, value) {
				$('#resultTable > tbody:last')
					.append('<tr>')
					.append('<td>' + value.id + " – " + value.teamName + " – " + value.status + '</td></tr>');
			});
			break;
	}
}
