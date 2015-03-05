$(function() {
	$("#race-next-menu-item").addClass("active");

	RaceProxy.findNext().done(findNextOk);

	$('#open-races').on('click', '.panel', function() {
		location.href = App.getContextPath() + "/prova/" + $(this).data("race");
	});

	// $('#open-races').on('mouseover', '.panel', function() {
	// $(this).css('cursor', 'pointer');
	// $("#banner-" + $(this).data("race")).fadeTo(0, 0.75);
	// });
	//
	// $('#open-races').on('mouseout', '.panel', function() {
	// $("#banner-" + $(this).data("race")).fadeTo(0, 1);
	// });

});

function findNextOk(data) {
	$
			.each(
					data,
					function(index, value) {

						RaceProxy.getBanner(value.id).done(function(data) {
							carregarBanner(value.id, data);
						});

						var day = moment(value.date, "YYYY-MM-DD");

						var race = "";
						race += "<div id='block-" + value.id + "' class='race col-md-4' hidden='true'>";
						race += "<div class='panel panel-default' data-race='" + value.id + "'>";
						race += "<div class='panel-heading' style='padding:0'>";

						race += "<a href='#' class='darken'>";
						race += "<img id='banner-" + value.id + "' src='' style='width: 100%;' />";
						race += "</a>";

						race += "</div>";
						race += "<div class='panel-body' style='padding-top: 5px'>";

						race += "<div class='row'>";
						race += "<div class='col-md-12 text-right'>";

						race += "<h5 class='pull-left' style='margin-top: 10px; margin-bottom: 5px;'><span class='glyphicon glyphicon-calendar' style='font-size: 0.8em'></span> "
								+ day.date() + ' ' + day.locale("pt-br").format("MMMM") + "</h5>";

						race += "<h5 class='pull-right' style='margin-top: 10px; margin-bottom: 5px;'><span class='glyphicon glyphicon-map-marker' style='font-size: 0.8em'></span> "
								+ (value.city ? value.city : 'Local não definido') + "</h5>";
						race += "</div>";
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
		banner = "http://placehold.it/750x350";
	}
	
	$("#banner-" + id).attr("src", banner);
	$("#block-" + id).show();
}
