$(function() {
	var id = $("#evento_id").val();

	// $("#registration-forms").click(function() {
	// var $this = $(this);
	// $this.button('loading');
	//
	// RaceRegistrationProxy.formDownload(id, function() {
	// $this.button('reset');
	// });
	// });

	$("#registration-export").click(function() {
		var $this = $(this);

		$this.button('loading');
		RaceRegistrationProxy.exportDownload(id, function() {
			$this.button('reset');
		});
	});

	EventProxy.loadSummary(id).done(loadSummaryOk);

	EventAnalyticsProxy.getByCategories(id).done(getByCategoriesOk);
	EventAnalyticsProxy.getByRaces(id).done(getByRacesOk);
	EventAnalyticsProxy.getByStatus(id).done(getByStatusOk);
	EventAnalyticsProxy.getByLocation(id).done(getByLocationOk);
	EventAnalyticsProxy.getByTshirt(id).done(getByTshirtOk);
});

function loadSummaryOk(event) {
	$(".race-name").text(event.name);

	if (event.status === 'closed' || event.status === 'end') {
		$("#registration-forms-div").show();
	}
}

function getByStatusOk(data) {
	var metadata = {
		confirmed : {
			label : "confirmadas",
			color : "#009856",
		},
		pendent : {
			label : "pendentes",
			color : "#ffbb03",
		},
		cancelled : {
			label : "canceladas",
			color : "#dc4733",
		}
	}

	var chartData = [];
	$.each(data, function(index, value) {
		chartData.push({
			label : metadata[value.label].label,
			color : metadata[value.label].color,
			y : value.value
		});
	});

	showChart($("#status-chart"), chartData);
}

function getByCategoriesOk(data) {
	fillChartData(data, $("#category-chart"));
}

function getByRacesOk(data) {
	fillChartData(data, $("#race-chart"));
}

function getByLocationOk(data) {
	fillChartData(data, $("#city-chart"));
}

function getByTshirtOk(data) {
	fillChartData(data, $("#tshirt-chart"));
}

function fillChartData(data, element) {
	var chartData = [];
	$.each(data, function(index, value) {
		chartData.push({
			label : value.label,
			y : value.value
		});
	});

	showChart(element, chartData);
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
