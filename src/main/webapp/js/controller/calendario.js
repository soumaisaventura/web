$(function() {
	$("#race-next-menu-item").addClass("active");
	RaceProxy.find($("#ano").text()).done(findOk);

	$('#open-races').on('click', '.panel', function() {
		location.href = App.getContextPath() + "/prova/" + $(this).data("race");
	});

	$('#open-races').on('mouseover', '.panel', function() {
		$(this).css('cursor', 'pointer');
	});
});

function findOk(data) {
	var group1 = new Riloadr({
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

	var template = $('#template').html();

	$.each(data, function(index, value) {
		var day = moment(value.date, "YYYY-MM-DD");

		value.date = day.locale("pt-br").format("DD [de] MMMM");
		value.place = value.city ? value.city : "Local não definido";
		value.corner = (value.status == "open" ? "inscrições abertas" : (value.status == "closed" ? "inscrições encerradas" : ""));

		var rendered = Mustache.render(template, value);
		$('#open-races').append(rendered);
	});

}

function carregarBanner(id, data) {
	var banner = "";

	if (data) {
		banner = "data:image/png;base64," + data;
	} else {
		banner = "http://placehold.it/750x350";
	}

	$("#banner-" + id).attr("src", banner);
	$("#block-" + id).show();
}
