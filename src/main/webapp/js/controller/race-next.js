$(function() {
	RaceProxy.findNext().done(findNextOk);
});

function findNextOk(data) {
	$.each(data, function(index, value) {
		var day = moment(value.date, "YYYY-MM-DD");

		var td1 = '<td class="text-capitalize text-center"><h1>' + day.date() + '</h1>' + day.locale("pt-br").format("MMMM") + '</td>';
		var td2 = '<td><h3><a href="' + App.getContextPath() + '/race/' + value.id + '">' + value.name + '</a>';

		if (value.registration.open) {
			td2 += ' <span class="label label-danger">Insrições abertas</span>';
		}

		td2 += '</h3><small>' + (value.city ? value.city : '') + '</small></td>';

		$('#resultTable > tbody:last').append('<tr>' + td1 + td2 + '</tr>');
	});
}
