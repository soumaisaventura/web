$(function() {
	moment.locale("pt-br");
	numeral.language('pt-br');
	numeral.defaultFormat('$ 0,0');

	var id = $("#id").val();
	EventProxy.load(id).done(function(event) {
		loadEventOk(event);
		loadMap(event);
	});

	EventProxy.getBanner(id).done(getBannerOk);
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

function getBannerOk(data) {
	if (data) {
		$("#banner").attr("src", "data:image/png;base64," + data);
		$(".banner-section").show();
	}
}

function loadEventOk(event) {
	$("#title").text(event.name);
	$("#description").text(event.description);
	$("#days-left").text(event.period.countdown);
	$("#location-city").text(event.location.city.name + " / " + event.location.city.state);

	// Date

	if (event.period.beginning !== event.period.end) {
		if (moment(event.period.beginning).isSame(moment(event.period.end), 'month')) {
			$("#date").append(App.moment(event.period.beginning).format("DD"));
		} else {
			$("#date").append(App.moment(event.period.beginning).format("DD [de] MMMM"));
		}

		$("#date").append(" à ");
	}
	$("#date").append(App.moment(event.period.end).format("DD [de] MMMM [de] YYYY"));
	$(".info-section").show();

	var template;

	// Organizers

	template = $('#organizer-template').html();
	if (event.organizers) {
		$.each(event.organizers, function(i, organizer) {
			var rendered = Mustache.render(template, organizer);
			$('#organizers').append(rendered);
		});

		$(".organizers-section").show();
	}

	// Races

	template = $('#race-template').html();
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
			$('#races').append(rendered);

			if (race.status !== 'open') {
				$(".race>div:nth-child(1)").addClass("change");
				$(".race>div:nth-child(2)").addClass("remove");
			}
		});

		$(".race .change").removeClass("col-md-8").addClass("col-md-12");
		$(".race .remove").remove();
	}
}
