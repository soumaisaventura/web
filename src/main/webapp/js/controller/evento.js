$(function() {
	var id = $("#id").val();

	var map = initMap();
	EventProxy.loadMap(id).done(function(data) {
		loadMapOk(data, map, id)
	});

	moment.locale("pt-br");
	numeral.language('pt-br');
	numeral.defaultFormat('$ 0,0');

	EventProxy.load(id).done(loadEventOk);
	EventProxy.getBanner(id).done(getBannerOk);

});

function initMap() {
	var options = {
		zoom : 12,
		scrollwheel : false
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

function loadEventOk(event) {
	$(".event-title").text(event.name);
	$(".event-description").text(event.description);
	$("#days-left").text(event.period.countdown);
	$("#event-location").text(event.location.city.name + " / " + event.location.city.state);

	var b = moment(event.period.beginning, "YYYY-MM-DD").locale("pt-br").format("L");
	var e = moment(event.period.end, "YYYY-MM-DD").locale("pt-br").format("L");
	$("#period").text(b + " à " + e);

	var organizerTemplate = $('#event-organizer-template').html();
	var renderedOrganizers = Mustache.render(organizerTemplate, event);
	$('#organizers').append(renderedOrganizers);

	var template = $('#event-race-template').html();
	$.each(event.races, function(i, race) {
		console.log(race.name + ": " + race.internal_id);

		race.idx = i + 1;
		race.day = moment(race.period.beginning, "YYYY-MM-DD").locale("pt-br").format('DD');
		race.month = moment(race.period.beginning, "YYYY-MM-DD").locale("pt-br").format('MMM');

		race.moreThanOneDay = !(race.period.beginning === race.period.end);
		race.period.beginning = moment(race.period.beginning, "YYYY-MM-DD").locale("pt-br").format('L');
		race.period.end = moment(race.period.end, "YYYY-MM-DD").locale("pt-br").format('L');

		race.current_period.end = moment(race.current_period.end, "YYYY-MM-DD").locale("pt-br").format('DD/MM');

		$.each(race.prices, function(j, price) {
			race.prices[j].beginning = moment(price.beginning).format('DD/MM');
			race.prices[j].end = moment(price.end).format('DD/MM');
		});

		$.each(race.modalities, function(j, modality) {
			modality.icon = App.getContextPath() + "/images/modalidade-" + modality.id + "-icon-black.png";
		});

		race.hasChampionship = (race.championships != null && race.championships.length > 0);

		var rendered = Mustache.render(template, race);
		$('#event-races').append(rendered);
	});
}
