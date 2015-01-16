$(function() {
	RaceProxy.findNext().done(findNextOk);
});

// Password Reset process

function findNextOk(data) {
	$.each(data, function(index, value) {
		var day = moment(value.date, "YYYY-MM-DD");

		var col1 = '<td>' + day.locale("pt-br").format("L") + '</td>';
		var col2 = '<td><a href="race/' + value.id + '">' + value.name + '</a></td>';

		$('#resultTable > tbody:last').append('<tr>' + col1 + col2 + '</tr>');
	});
}
