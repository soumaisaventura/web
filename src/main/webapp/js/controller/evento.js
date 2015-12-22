$(function() {
	moment.locale("pt-br");
	numeral.language('pt-br');
	numeral.defaultFormat('$ 0,0');

	var id = $("#id").val();
	EventProxy.load(id).done(function(event) {
		loadEventOk(event);
		loadMap(event);
	});
});

function loadMap(event) {
	var map = initMap();
	var coord = new google.maps.LatLng(event.location.coords.latitude, event.location.coords.longitude);
	var marker = new google.maps.Marker({
		map : map,
		animation : google.maps.Animation.BOUNCE,
		position : coord,
		title : event.name
	});

	marker.addListener('click', function() {
		var lat = this.getPosition().lat();
		var lng = this.getPosition().lng();
		window.open("https://maps.google.com/?q=" + lat + "," + lng, "_blank");
	});

	map.setCenter(coord);
	$(".location-section").show();
}

function initMap() {
	var options = {
		zoom : 14,
		disableDefaultUI : true,
		draggable : false,
		scrollwheel : false,
		disableDoubleClickZoom : true,
		zoomControl : false,
		scaleControl : false
	};

	return new google.maps.Map($("#map")[0], options);
}

function loadEventOk(event) {
	new Riloadr({
		breakpoints : [ {
			name : '',
			minWidth : 1
		} ]
	});

	$(".banner-section").show();

	$("#title").text(event.name);
	$("#description").text(event.description);
	$("#days-left").text(event.period.countdown);
	$("#location-city").text(App.parseCity(event.location.city));
	$("#date").text(App.parsePeriod(event.period));
	$(".info-section").show();

	var template;

	// Organizers

	template = $('#organizer-template');
	if (event.organizers) {
		$.each(event.organizers, function(i, organizer) {
			var rendered = Mustache.render(template.html(), organizer);
			$('#organizers').append(rendered);
		});
		$(".organizers-section").show();
	}
	template.remove();

	// Races

	template = $('#race-template');
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

			var rendered = Mustache.render(template.html(), race);
			$('#races').append(rendered);

		});

		$(".race:not(.open)>div:nth-child(1)").removeClass("col-md-8").addClass("col-md-12");
		$(".race:not(.open)>div:nth-child(2)").remove();
		
		$(".end").removeClass("btn-success").addClass("btn-warning");
	}
	template.remove();
}
