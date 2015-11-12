var markerClusterer;

$(function() {
	$("#btn_limpar").click(function() {
		markerClusterer.clearMarkers();
	});
});

function initMap() {
	if (navigator.geolocation) {
		navigator.geolocation.getCurrentPosition(centerMap);
	} else {
		var position = {
			coords : {
				latitude : -13,
				longitude : -41
			}
		};
		centerMap(position);
	}
}

function centerMap(position) {
	var options = {
		center : {
			lat : position.coords.latitude,
			lng : position.coords.longitude
		},
		zoom : 6,
	};

	var map = new google.maps.Map($("#map")[0], options);

	var markers = [];
	for (var i = 0; i < 100; i++) {
		var latLng = new google.maps.LatLng(data.photos[i].latitude, data.photos[i].longitude);
		var marker = new google.maps.Marker({
			'position' : latLng
		});
		markers.push(marker);
	}
	markerClusterer = new MarkerClusterer(map);
	markerClusterer.addMarkers(markers, false);
}
