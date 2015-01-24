$(function() {
	RegistrationProxy.find().done(findOk);
});

function findOk(data) {
	$.each(data, function(index, value) {

		var td1 = "<td>";
		td1 += "<a href='" + App.getContextPath() + "/registration/" + value.number + "'>";
		td1 += value.race.name;
		td1 += "</a>";
		td1 += "</td>";

		$('#resultTable > tbody:last').append('<tr>' + td1 + '</tr>');
	});
}
