$(function() {
	RaceProxy.findNext().done(findNextOk);
});

// Password Reset process

function findNextOk(data) {
	$.each(data, function(index, value) {
		var day = moment(value.date, "YYYY-MM-DD");

		var col1 = '<td>' + day.locale("pt-br").format("L") + '</td>';
		var col2 = '<td>' + value.name + '</td>';
		var col3;

		if (value.registration.open) {
			col3 = '<td><a href="race/' + value.id + '/registration">Inscrição</a></td>';
		} else {
			col3 = '<td> </td>';
		}

		$('#resultTable > tbody:last').append('<tr>' + col1 + col2 + col3 + '</tr>');
	});
}
