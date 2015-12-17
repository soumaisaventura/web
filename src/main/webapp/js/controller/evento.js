$(function() {
	var id = $("#id").val();

	// var map = initMap();
	// EventProxy.loadMap(id).done(function(data) {
	// loadMapOk(data, map, id)
	// });

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
	$("#event-location-city").text(event.location.city.name + " / " + event.location.city.state);

	if (event.period.beginning !== event.period.end) {
		if (moment(event.period.beginning).isSame(moment(event.period.end), 'month')) {
			$("#event-date").append(App.moment(event.period.beginning).format("DD"));
		} else {
			$("#event-date").append(App.moment(event.period.beginning).format("DD [de] MMMM"));
		}

		$("#event-date").append(" à ");
	}
	$("#event-date").append(App.moment(event.period.end).format("DD [de] MMMM [de] YYYY"));

	// var b = App.moment(event.period.beginning).format("DD [de] MMMM");
	// var e = App.moment(event.period.end).format("DD [de] MMMM [de] YYYY");
	// $("#event-date").text(b + " à " + e);

	// var organizerTemplate = $('#event-organizer-template').html();
	// var renderedOrganizers = Mustache.render(organizerTemplate, event);
	// $('#organizers').append(renderedOrganizers);

	var template;

	console.log(event.organizers);

	// Organizers

	template = $('#event-organizer-template').html();
	if (event.organizers) {
		$.each(event.organizers, function(i, organizer) {
			var rendered = Mustache.render(template, organizer);
			$('#event-organizers').append(rendered);
		});

		// $(".event-organizers").show();
	}

	// Races

	template = $('#event-race-template').html();
	if (event.races) {
		$.each(event.races, function(i, race) {
			race.idx = i + 1;

			// Periods

			race.date = App.moment(race.period.beginning).format("DD [de] MMMM");
			race.day = App.moment(race.period.beginning).format('DD');
			race.month = App.moment(race.period.beginning).format('MMM').toUpperCase();
			race.more_than_one_day = race.period.beginning !== race.period.end;
			race.period.beginning = App.moment(race.period.beginning).format('L');
			race.period.end = App.moment(race.period.end).format('L');

			if (race.current_period) {
				race.current_period.end = App.moment(race.current_period.end).format('DD [de] MMM');
			}

			// Prices

			if (race.prices) {
				$.each(race.prices, function(j, price) {
					race.prices[j].beginning = App.moment(price.beginning).format('DD/MM');
					race.prices[j].end = App.moment(price.end).format('DD/MM');
				});
			}

			var rendered = Mustache.render(template, race);
			$('#event-races').append(rendered);
		});
	}

	$(".soon, .end, .closed").detach();
}
