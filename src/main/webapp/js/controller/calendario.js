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
			name : '288',
			maxWidth : 320
		}, // iPhone 3
		{
			name : '400',
			maxWidth : 320,
			minDevicePixelRatio : 2
		}, // iPhone 4 Retina display
		{
			name : '320',
			minWidth : 321,
			maxWidth : 640
		}, {
			name : '358',
			minWidth : 641
		} ]
	});

	var template = $('#template');
	$.each(data, function(index, value) {
		var day = moment(value.period.beginning, "YYYY-MM-DD");

		value.date = day.locale("pt-br").format("DD [de] MMMM");
		value.place = value.location.city ? value.location.city.name + "/" + value.location.city.state : "Local não definido";
		value.corner = (value.status == "open" ? "inscrições abertas" : (value.status == "closed" ? "inscrições encerradas" : ""));

		var rendered = Mustache.render(template.html(), value);
		$('#open-events').append(rendered);
	});
	template.remove();
}
