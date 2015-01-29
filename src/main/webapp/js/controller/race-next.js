$(function() {
	RaceProxy.findNext().done(findNextOk);
});

function findNextOk(data) {
	$.each(data, function(index, value) {
		var day = moment(value.date, "YYYY-MM-DD");

		var td1 = '<td class="text-capitalize text-center" style="vertical-align:middle; padding: 20px;"><h1 style="margin-top: 0px; margin-bottom: 0px;">' + day.date() + '</h1><span class="text-danger">' + day.locale("pt-br").format("MMMM") + '</span></td>';
		var td2 = '<td style="vertical-align:middle; margin-top: 10px; padding: 20px;"><h3 style="margin-top: 0px; margin-bottom: 0px;"><a href="' + App.getContextPath() + '/race/' + value.id + '">' + value.name + '</a>';

		if (value.registration.open) {
			td2 += '&nbsp;&nbsp;&nbsp;<span class="label label-success pull-right">Inscrições abertas</span>';
		}

		td2 += '</h3>' + (value.city ? value.city : '') + '</td>';

		$('#resultTable > tbody:last').append('<tr>' + td1 + td2 + '</tr>');
	});
}
