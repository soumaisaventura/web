var map;

$(function() {
	initMap();

	if (navigator.geolocation) {
		navigator.geolocation.getCurrentPosition(centerMap);
	}

	// $("#buscar").click(function() {
	// showMarkers();
	// });
});

function centerMap(position) {
	map.setCenter(position);
}

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
		disableDefaultUI : true,
	// draggable : false,
	// scrollwheel : false,
	// disableDoubleClickZoom : true
	// zoomControl : false,
	// scaleControl : false,
	};

	map = new google.maps.Map($("#map")[0], options);
	showMarkers();
}

function showMarkers() {
	var marker;
	var coord;
	var coords = [];

	// Cangaço
	coord = new google.maps.LatLng(-11.9778555, -39.102922);
	coords.push(coord);
	marker = new google.maps.Marker({
		position : coord,
		map : map,
		icon : 'http://gmapsmarkergenerator.eu01.aws.af.cm/getmarker?scale=1&color=' + getColor(),
		title : 'Cangaço'
	});

	// Desafio dos Sertões
	coord = new google.maps.LatLng(-9.4093282, -40.5070731);
	coords.push(coord);
	marker = new google.maps.Marker({
		position : coord,
		map : map,
		icon : 'http://gmapsmarkergenerator.eu01.aws.af.cm/getmarker?scale=1&color=' + getColor(),
		title : 'Desafio dos Sertões 2015'
	});

	// Casco de Peba
	coord = new google.maps.LatLng(-9.3932462, -40.4839331);
	coords.push(coord);
	marker = new google.maps.Marker({
		position : coord,
		map : map,
		icon : 'http://gmapsmarkergenerator.eu01.aws.af.cm/getmarker?scale=1&color=' + getColor(),
		title : 'Casco de Peba 2015'
	});

	// Noite do Perrengue
	coord = new google.maps.LatLng(-12.406472, -37.9067442);
	coords.push(coord);
	marker = new google.maps.Marker({
		position : coord,
		map : map,
		icon : 'http://gmapsmarkergenerator.eu01.aws.af.cm/getmarker?scale=1&color=' + getColor(),
		title : 'Noite do Perrengue 3'
	});

	// Corrida do CT Gantuá
	coord = new google.maps.LatLng(-13.0073514, -41.369959);
	coords.push(coord);
	marker = new google.maps.Marker({
		position : coord,
		map : map,
		icon : 'http://gmapsmarkergenerator.eu01.aws.af.cm/getmarker?scale=1&color=' + getColor(),
		title : 'Corrida do CT Gantuá 2015'
	});

	// Peleja
	coord = new google.maps.LatLng(-12.5471833, -38.7102718);
	coords.push(coord);
	marker = new google.maps.Marker({
		position : coord,
		map : map,
		icon : 'http://gmapsmarkergenerator.eu01.aws.af.cm/getmarker?scale=1&color=' + getColor(),
		title : 'Peleja 2015'
	});

	// Laskpé
	coord = new google.maps.LatLng(-9.4886588, -40.6168342);
	coords.push(coord);
	marker = new google.maps.Marker({
		position : coord,
		map : map,
		icon : 'http://gmapsmarkergenerator.eu01.aws.af.cm/getmarker?scale=1&color=' + getColor(),
		title : 'Laskpé 2015'
	});

	// Native
	coord = new google.maps.LatLng(-10.7540411, -36.9578873);
	coords.push(coord);
	marker = new google.maps.Marker({
		position : coord,
		map : map,
		icon : 'http://gmapsmarkergenerator.eu01.aws.af.cm/getmarker?scale=1&color=' + getColor(),
		title : 'Circuito Native 2015 – etapa 2'
	});

	// Mandacaru
	coord = new google.maps.LatLng(-12.1236112, -39.1098222);
	coords.push(coord);
	marker = new google.maps.Marker({
		position : coord,
		map : map,
		icon : 'http://gmapsmarkergenerator.eu01.aws.af.cm/getmarker?scale=1&color=' + getColor(),
		title : 'Mandacaru 2015'
	});

	console.log(getColor());

	// Zoom
	// var bounds = new google.maps.LatLngBounds();
	// for (var i = 0, size = coords.length; i < size; i++) {
	// bounds.extend(coords[i]);
	// }
	//
	// map.fitBounds(bounds);
}

function getColor() {
	// return Math.floor(Math.random() * 16777215).toString(16);
	return 'c5c5c5';
}

function centerMap(position) {

}
