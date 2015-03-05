$(function() {

	$("#race-next-menu-item").addClass("active");

	RaceProxy.findNext().done(findNextOk);

//	$('#open-races').on('mouseover', '.panel', function() {
//		$(this).unwrap("<div style='background-color: black; z-index: 2;'></div>");
//	});
	
//	$('#open-races').on('mouseout', '.panel', function() {
//		$(this).wrapInner("<div 'background-color: black; z-index: 2;'></div>");
//	});
	
	$('#open-races').on('click', '.panel', function() {
		
		location.href = App.getContextPath() + "/prova/" + $(this).data("race");
	});

	 $('#open-races').on('mouseover', '.panel', function() {
		 $(this).css('cursor', 'pointer');
//		 $("#banner-" + $(this).data("race")).fadeTo(0, 0.75);
	 });
	
//	 $('#open-races').on('mouseout', '.panel', function() {
//	 $("#banner-" + $(this).data("race")).fadeTo(0, 1);
//	 });

});

function findNextOk(data) {
	var group1 = new Riloadr({
		breakpoints : [ {
			name : '320',
			maxWidth : 320
		}, // iPhone 3
		{
			name : '640',
			maxWidth : 320,
			minDevicePixelRatio : 2
		}, // iPhone 4 Retina display
		{
			name : '640',
			minWidth : 321,
			maxWidth : 640
		}, {
			name : '1024',
			minWidth : 641
		} ]
	});

	$
			.each(
					data,
					function(index, value) {

						var day = moment(value.date, "YYYY-MM-DD");

						var race = "";
						race += "<div id='block-" + value.id + "' class='race col-md-4'>";
						race += "<div class='panel panel-default' data-race='" + value.id + "'>";
						race += "<div class='panel-heading' style='padding:0'>";

						if(value.registration.open)
						race += "<div class='box'><div class='corner'><span>Inscrições abertas</span></div>";

						race += "<img class='responsive' id='banner-" + value.id + "' data-src='" + App.getBaseUrl() + "/api/race/" + value.id
								+ "/banner/{breakpoint-name}'/>";
						race += "</div>";
						race += "<div class='panel-body' style='padding-top: 5px'>";
						race += "<div class='row'>";
						race += "<div class='col-md-12 text-right'>";
						race += "<h5 class='pull-left' style='margin-top: 10px; margin-bottom: 5px;'><span class='glyphicon glyphicon-calendar' style='font-size: 0.8em'></span> "
								+ day.locale("pt-br").format("DD [de] MMMM") + "</h5>";
						race += "<h5 class='pull-right' style='margin-top: 10px; margin-bottom: 5px;'><span class='glyphicon glyphicon-map-marker' style='font-size: 0.8em'></span> "
								+ (value.city ? value.city : 'Local não definido') + "</h5>";
						race += "</div>";
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
