$(function() {
	var id = $("#id").val();

	var map = initMap();
	EventoProxy.loadMap(id).done(function(data) {
		loadMapOk(data, map, id)
	});

	moment.locale("pt-br");
	numeral.language('pt-br');
	numeral.defaultFormat('$ 0,0');

	EventoProxy.load(id).done(loadEventOk);
	EventoProxy.getBanner(id).done(getBannerOk);

});

function initMap() {
	var options = {
		zoom : 12,
	};

	return new google.maps.Map($("#map")[0], options);
}

function loadMapOk(data, map, id) {
	var marker;
	var coord;
	var coords = [];

	$.each(data, function(i, event) {
		coord = new google.maps.LatLng(event.location.coords.latitude, event.location.coords.longitude);
		coords.push(coord);

		marker = new google.maps.Marker({
			map : map,
			animation : id === event.id ? google.maps.Animation.DROP : null,
			position : coord,
			icon : 'http://gmapsmarkergenerator.eu01.aws.af.cm/getmarker?scale=1&color=' + (id === event.id ? 'ff1000' : 'f7f4f4'),
			title : event.name
		});

		if (id === event.id) {
			map.setCenter(coord);
		}
	});
}

function getBannerOk(data) {
	if (data) {
		$("#banner").attr("src", "data:image/png;base64," + data);
	}
}

function loadEventOk(data) {
	$(".event-title").text(data.name);
	$(".event-description").text(data.description);

	console.log(data);

	var template = $('#race-template').html();

	$.each(data.races, function(i, race) {
		race.idx = i + 1;
		race.day = moment(race.period.beginning, "YYYY-MM-DD").locale("pt-br").format('DD');
		race.month = moment(race.period.beginning, "YYYY-MM-DD").locale("pt-br").format('MMM');

		$.each(race.prices, function(j, price) {
			race.prices[j].beginning = moment(price.beginning).format('DD/MM');
			race.prices[j].end = moment(price.end).format('DD/MM');
		});

		var rendered = Mustache.render(template, race);
		$('#races').append(rendered);
	});
}
