$(function() {
	var id = $("#id").val();

	initMap();
	EventoProxy.loadMap(id).done(loadMapOk);

	moment.locale("pt-br");
	numeral.language('pt-br');
	numeral.defaultFormat('$ 0,0');

	EventoProxy.load(id).done(loadEventOk);
	EventoProxy.getBanner(id).done(getBannerOk);

});

var map;

function initMap() {
	var options = {
		// zoom : 7,
		disableDefaultUI : true,
		draggable : false,
		scrollwheel : false,
		disableDoubleClickZoom : true,
		zoomControl : false,
		scaleControl : false
	};

	map = new google.maps.Map($("#map")[0], options);
}

function loadMapOk(data) {
	var marker;
	var coord;
	var coords = [];

	$.each(data, function(i, event) {
		$.each(this.races, function(i, race) {
			coord = new google.maps.LatLng(race.location.coords.latitude, race.location.coords.longitude);
			coords.push(coord);
			marker = new google.maps.Marker({
				position : coord,
				map : map,
				// icon :
				// 'http://gmapsmarkergenerator.eu01.aws.af.cm/getmarker?scale=1&color='
				// + getColor(race.status),
				title : event.name
			});
		});
	});

	var bounds = new google.maps.LatLngBounds();
	$.each(coords, function() {
		bounds.extend(this);
	});
	map.setCenter(bounds.getCenter());
	map.fitBounds(bounds);
}

function getBannerOk(data) {
	if (data) {
		$("#banner").attr("src", "data:image/png;base64," + data);
	}
}

function loadEventOk(event) {
	console.log(event);

	$(".event-title").text(event.name);
	$(".event-description").text(event.description);
	// $(".event-banner .right-col").css("background-color",
	// event.layout.background_color);

	// $(".jumbotron").css("background-color", event.layout.background_color);
	//	
	// $.get( $("#contextPath").val() + '/js/controller/race.mst',
	// function(template) {
	//
	// $.each(event.races, function(i, race){
	//						
	// event.races[i].idx = i+1;
	// if (race.period.beginning === race.period.end){
	// event.races[i].date = moment(race.period.beginning,
	// "YYYY-MM-DD").locale("pt-br").format('LL');
	// } else {
	// event.races[i].date = moment(race.period.beginning).format('L') + " à " +
	// moment(race.period.end).format('L');
	// }
	//			
	// $.each(race.prices, function(j, price){
	// event.races[i].prices[j].beginning =
	// moment(price.beginning).format('DD/MM');
	// event.races[i].prices[j].end = moment(price.end).format('DD/MM');
	// });
	// });
	//		
	// event.contextPath = $("#contextPath").val();
	//		
	// var rendered = Mustache.render(template, event);
	//	    
	// $('#provas').html(rendered);
	// });
}
