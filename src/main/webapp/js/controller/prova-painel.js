$(function() {
	var id = $("#race-id").val();

	RaceProxy.loadSummary(id).done(loadOk);
	RaceRegistrationProxy.find(id).done(findOk);

	$("#registration-forms").click(function() {
		RaceProxy.formDownload(id);
	});
});

function loadOk(data) {
	$(".race-name").text(data.name);
}

function findOk(data) {
	var statusMetadata = {
		confirmed : {
			label : "confirmadas",
			color : "#009856",
			y : 0
		},
		pendent : {
			label : "pendentes",
			color : "#ffbb03",
			y : 0
		},
		cancelled : {
			label : "canceladas",
			color : "#dc4733",
			y : 0
		}
	}
	var categoryMetadata = {};
	var cityMetadata = {};

	fillMetadata(data, statusMetadata, categoryMetadata, cityMetadata);

	var statusData = [];
	for ( var property in statusMetadata) {
		if (statusMetadata.hasOwnProperty(property)) {
			statusData.push(statusMetadata[property]);
		}
	}

	var categoryData = createChartData(categoryMetadata);
	var cityData = createChartData(cityMetadata);

	showChart($("#status-chart"), statusData);
	showChart($("#category-chart"), categoryData);
	showChart($("#city-chart"), cityData);
}

function getRandom() {
	return Math.floor((Math.random() * 255) + 1);
}

function fillMetadata(data, statusMetadata, categoryMetadata, cityMetadata) {
	$.each(data, function(index, value) {
		statusMetadata[value.status].y++;

		if (!categoryMetadata[value.category.name]) {
			categoryMetadata[value.category.name] = 0;
		}

		if (value.status == 'confirmed') {
			categoryMetadata[value.category.name]++;
		}

		$.each(value.teamFormation, function(i, v) {
			if (value.status == 'confirmed') {
				var city = v.city + "/" + v.state;

				if (!cityMetadata[city]) {
					cityMetadata[city] = 0;
				}
				cityMetadata[city]++;
			}
		});
	});
}

function createChartData(metadata) {
	var data = [];

	for ( var property in metadata) {
		if (metadata.hasOwnProperty(property)) {
			data.push({
				label : property,
				value : metadata[property],
				y : metadata[property]
			});
		}
	}

	return data;
}

function showChart(element, data) {
	element.CanvasJSChart({
		animationEnabled : true,
		data : [ {
			type : "doughnut",
			startAngle : 120,
			toolTipContent : "<strong>#percent%</strong>",
			indexLabel : "{y} {label}",
			dataPoints : data
		} ]
	});
}
