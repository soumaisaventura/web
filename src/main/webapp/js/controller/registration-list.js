$(function() {
	moment.locale("pt-br");

	RegistrationProxy.find().done(findOk);
});

function findOk($data, $status, $request) {
	switch ($request.status) {
		case 204:
			var message = "Você não se inscreveu em nenhuma prova ainda."
			$('#resultTable > tbody:last').append('<tr><td>' + message + '</td></tr>');

			break;

		case 200:
			$.each($data, function(index, value) {
				var td1;
				td1 += "<td>";
				td1 += "<h4>#" + value.number + "</h4>";
				td1 += "</td>";

				td1 += "<td>";

				td1 += "<h2><a href='" + App.getContextPath() + "/registration/" + value.number + "'>" + value.race.name + "</a> <small>"
						+ parseStatus(value.status) + "</small></h2>";
				td1 += "<h5>";
				td1 += "<span class='glyphicon glyphicon-calendar' style='font-size: 0.8em'></span> ";
				td1 += moment(value.race.date, "YYYY-MM-DD").locale("pt-br").format('LL');
				td1 += "&nbsp;&nbsp;&nbsp;";
				td1 += "<span class='glyphicon glyphicon-map-marker' style='font-size: 0.8em'></span> ";
				td1 += value.race.city.name + "/" + value.race.city.state;
				td1 += "</h5>";
				td1 += "<h4>";
				td1 += "<span class='glyphicon glyphicon-user' style='font-size: 0.8em'></span> ";
				td1 += value.teamName;
				td1 += "<h4>";

				td1 += "</td>";
				$('#resultTable > tbody:last').append('<tr>' + td1 + '</tr>');
			});
			break;
	}
}

function parseStatus($status) {
	var result;

	switch ($status) {
		case "confirmed":
			result = '<span class="label label-success">Confirmada</span>';
			break;

		case "pendent":
			result = '<span class="label label-warning">Aguardando pagamento</span>';
			break;

		default:
			result = '<span class="label label-danger">Cancelada</span>';
	}

	return result;
}
