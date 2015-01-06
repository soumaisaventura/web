$(function() {
	RaceProxy.findNext().done(findNextOk);
});

// Password Reset process

function findNextOk(data) {
	$.each(data, function(index, value) {
		var day = moment(value.date, "YYYY-MM-DD");
		$('#resultTable > tbody:last').append('<tr><td>' + day.locale("pt-br").format("L") + '</td><td>' + value.name + '</td></tr>');
	});
}


