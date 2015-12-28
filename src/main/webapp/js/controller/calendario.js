$(function() {
	var year = $("#ano").text();

	$("#race-next-menu-item").addClass("active");
	$("#race-next-menu-item-" + year).addClass("active");
	EventProxy.find(year).done(findOk);

	$('#open-events').on('click', '.panel', function() {
		location.href = App.getContextPath() + "/evento/" + $(this).data("event");
	});

	$('#open-events').on('mouseover', '.panel', function() {
		$(this).css('cursor', 'pointer');
	});
});

function findOk(data) {
	new Riloadr({
		breakpoints : [ {
			name : '1170',
			minWidth : 1200
		}, {
			name : '970',
			minWidth : 992
		}, {
			name : '750',
			maxWidth : 991
		} ]
	});

	var template = $('#template');

	$.each(data, function(index, event) {
		var day = moment(event.period.beginning, "YYYY-MM-DD");

		event.date = day.locale("pt-br").format("DD [de] MMMM");
		event.place = event.location.city ? event.location.city.name + "/" + event.location.city.state : "Local não definido";
		event.corner = (event.status == "open" ? "inscrições abertas" : (event.status == "closed" ? "inscrições encerradas" : ""));
		event.status = moment().year() !== day.year() && event.status === "end" ? null : event.status;

		var rendered = Mustache.render(template.html(), event);
		$('#open-events').append(rendered);
	});
	template.remove();
}
