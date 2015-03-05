$(function() {
	$("#race-next-menu-item").addClass("active");

	RaceProxy.findNext().done(findNextOk);

	$('#open-races').on('click', '.registration', function() {
		location.href = App.getContextPath() + '/prova/' + $(this).data("race") + '/inscricao';
	});
});

function findNextOk(data) {
	$.each(data, function(index, value) {

		RaceProxy.getBanner(value.id).done(function(data) {
			carregarBanner(value.id, data);
		});

		console.log(value);

		var day = moment(value.date, "YYYY-MM-DD");

		var race = "";
		race += "<div class='col-md-4'>";
		race += "<div class='panel panel-default'>";
		race += "<div class='panel-heading' style='padding:0'>";
		race += "<img id='banner-" + value.id + "' src='' style='width: 100%;' />";
		race += "</div>";
		race += "<div class='panel-body' style='padding-top: 5px'>";
		race += "<h3 style='margin-top: 10px; margin-bottom: 5px;'><span class='glyphicon glyphicon-calendar' style='font-size: 0.8em'></span> " + day.date() + ' ' + day.locale("pt-br").format("MMMM") + "</h3>";
		race += "<h3 style='margin-top: 10px; margin-bottom: 5px;'><span class='glyphicon glyphicon-map-marker' style='font-size: 0.8em'></span> " + (value.city ? value.city : 'Local não definido') + "</h3>";
		race += "</div>";
		race += "<div class='panel-footer' style='background-color: white;'>";
		race += "<div class='row'>";
		race += "<div class='col-md-6'>";
		race += "<a href='" + App.getContextPath() + "/prova/" + value.id + "' class='registration btn btn-block btn-default'>";
		race += "<span class='glyphicon glyphicon-eye-open' aria-hidden='true' style='font-size: 0.8em;'></span>";
		race += " Ver detalhes";
		race += "</a>";
		race += "</div>";
		race += "<div class='col-md-6'>";
		if (value.registration.open) {
			race += "<button type='button' class='registration btn btn-block btn-success' data-race='" + value.id + "'>";
			race += "<span class='glyphicon glyphicon-pencil' aria-hidden='true' style='font-size: 0.8em;'></span>";
			race += " Inscrições";
			race += "</button>";
		}
		race += "</div>";
		race += "</div>";
		race += "</div>";

		$('#open-races').append(race);

	});
}

function carregarBanner(id, data) {
	var banner = "";
	if (data) {
		banner = "data:image/png;base64," + data;
	} else {
		banner = "http://placehold.it/350x150";
	}
	$("#banner-" + id).attr("src", banner);
}
