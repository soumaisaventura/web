var map;

$(function() {
	$("#buscar").click(function() {
		var marker;
		var coord;
		var coords = [];

		// Cangaço
		coord = new google.maps.LatLng(-11.9778555, -39.102922);
		coords.push(coord);
		marker = new google.maps.Marker({
			position : coord,
			map : map
		});

		// Desafio dos Sertões
		coord = new google.maps.LatLng(-9.4093282, -40.5070731);
		coords.push(coord);
		marker = new google.maps.Marker({
			position : coord,
			map : map
		});

		// Casco de Peba
		coord = new google.maps.LatLng(-9.3932462, -40.4839331);
		coords.push(coord);
		marker = new google.maps.Marker({
			position : coord,
			map : map
		});

		// Noite do Perrengue
		coord = new google.maps.LatLng(-12.406472, -37.9067442);
		coords.push(coord);
		marker = new google.maps.Marker({
			position : coord,
			map : map
		});

		// Corrida do CT Gantuá
		coord = new google.maps.LatLng(-13.0073514, -41.369959);
		coords.push(coord);
		marker = new google.maps.Marker({
			position : coord,
			map : map
		});

		// Peleja
		coord = new google.maps.LatLng(-12.5471833, -38.7102718);
		coords.push(coord);
		marker = new google.maps.Marker({
			position : coord,
			map : map
		});

		// Laskpé
		coord = new google.maps.LatLng(-9.4886588, -40.6168342);
		coords.push(coord);
		marker = new google.maps.Marker({
			position : coord,
			map : map
		});

		// Native
		coord = new google.maps.LatLng(-10.7540411, -36.9578873);
		coords.push(coord);
		marker = new google.maps.Marker({
			position : coord,
			map : map
		});

		// Mandacaru
		coord = new google.maps.LatLng(-12.1236112, -39.1098222);
		coords.push(coord);
		marker = new google.maps.Marker({
			position : coord,
			map : map
		});

		var bounds = new google.maps.LatLngBounds();
		for (var i = 0, size = coords.length; i < size; i++) {
			bounds.extend(coords[i]);
		}
		map.fitBounds(bounds);
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
		zoom : 6
	};

	map = new google.maps.Map($("#map")[0], options);
}
