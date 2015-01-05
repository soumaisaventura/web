$(function() {
	RaceProxy.findNext().done(findNextOk);
});

// Password Reset process

function findNextOk(data) {
	$.each(data, function(index, value) {
		console.log(value);
		$('#resultTable > tbody:last').append('<tr><td>' + value.date + '</td><td>' + value.name + '</td></tr>');
	});
}
