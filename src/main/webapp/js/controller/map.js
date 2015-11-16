var map;

$(function() {
	initMap();

	// if (navigator.geolocation) {
	// navigator.geolocation.getCurrentPosition(centerMap);
	// }

	EventoMapaProxy.load().done(loadOk);
});

function initMap() {
	var position = {
		coords : {
			latitude : -11,
			longitude : -39
		}
	};

	var options = {
		center : {
			lat : position.coords.latitude,
			lng : position.coords.longitude
		},
		zoom : 7,
		disableDefaultUI : true
	// draggable : false,
	// scrollwheel : false,
	// disableDoubleClickZoom : true
	// zoomControl : false,
	// scaleControl : false,
	};

	map = new google.maps.Map($("#map")[0], options);
}

function loadOk(data) {
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
				icon : 'http://gmapsmarkergenerator.eu01.aws.af.cm/getmarker?scale=1&color=' + getColor(race.status),
				title : event.name
			});
		});
	});
}

function getColor(status) {
	var color;

	console.log('xxx' + status);

	switch (status) {
		case 'soon':
			color = 'b5bbff';
			break;
		case 'open':
			color = '36de23';
			break;
		case 'closed':
			color = 'ff5454';
			break;
		default:
			color = 'c5c5c5'
	}

	console.log('___' + color);

	return color;
}
