$(function() {

	RaceRegistrationProxy.find($("#race-id").val()).done(findOk);
});

function findOk(data) {
	var statusMetadata = {
		confirmed : {
			label : "confirmadas",
			color : "#009856",
			y : 0,
			value : 0
		},
		pendent : {
			label : "pendentes",
			color : "#ffbb03",
			y : 0,
			value : 0
		},
		cancelled : {
			label : "canceladas",
			color : "#dc4733",
			y : 0,
			value : 0
		}
	}
	var categoryMetadata = {};
	var cityMetadata = {};

	$.each(data, function(index, value) {
		statusMetadata[value.status].value++;
		statusMetadata[value.status].y++;

		if (!categoryMetadata[value.category.name]) {
			categoryMetadata[value.category.name] = 0;
		}

		if (value.status == 'confirmed') {
			categoryMetadata[value.category.name]++;

			$.each(value.teamFormation, function(i, v) {
				var city = v.city + "/" + v.state;
				if (!cityMetadata[city]) {
					cityMetadata[city] = 0;
				}
				cityMetadata[city]++;
			});
		}
	});

	var statusData = [];
	for ( var property in statusMetadata) {
		if (statusMetadata.hasOwnProperty(property)) {
			statusData.push(statusMetadata[property]);
		}
	}

	var categoryData = [];
	for ( var property in categoryMetadata) {
		if (categoryMetadata.hasOwnProperty(property)) {
			categoryData.push({
				label : property,
				value : categoryMetadata[property],
				y : categoryMetadata[property]
			// ,
			// color : "rgb(" + getRandom() + "," + getRandom() + "," +
			// getRandom() + ")"
			});
		}
	}

	var cityData = [];
	for ( var property in cityMetadata) {
		if (cityMetadata.hasOwnProperty(property)) {
			cityData.push({
				label : property,
				value : cityMetadata[property],
				y : cityMetadata[property]
			// ,
			// color : "rgb(" + getRandom() + "," + getRandom() + "," +
			// getRandom() + ")"
			});
		}
	}

	console.log(cityData);

	// var canvas, chart;
	//
	// canvas = $("#status-chart").get(0);
	// chart = new Chart(canvas.getContext("2d")).Doughnut(statusData);
	// $('#status-legend').html(chart.generateLegend());
	//
	// canvas = $("#category-chart").get(0);
	// chart = new Chart(canvas.getContext("2d")).Doughnut(categoryData);
	// $('#category-legend').html(chart.generateLegend());
	//
	// canvas = $("#city-chart").get(0);
	// chart = new Chart(canvas.getContext("2d")).Doughnut(cityData);
	// $('#city-legend').html(chart.generateLegend());

	$("#status-chart").CanvasJSChart({
		animationEnabled : true,
		data : [ {
			type : "doughnut",
			startAngle : 20,
			toolTipContent : "<strong>#percent%</strong>",
			// showInLegend : true,
			indexLabel : "{y} {label}",
			// legendText : "{label}",
			dataPoints : statusData
		} ]
	});

	$("#category-chart").CanvasJSChart({
		animationEnabled : true,
		data : [ {
			type : "doughnut",
			startAngle : 20,
			toolTipContent : "{label}: {y} - <strong>#percent%</strong>",
			indexLabel : "{y} {label}",
			dataPoints : categoryData
		} ]
	});

	$("#city-chart").CanvasJSChart({
		animationEnabled : true,
		data : [ {
			type : "doughnut",
			startAngle : 20,
			toolTipContent : "{label}: {y} - <strong>#percent%</strong>",
			indexLabel : "{y} {label}",
			dataPoints : cityData
		} ]
	});
}

function getRandom() {
	return Math.floor((Math.random() * 255) + 1);
}
