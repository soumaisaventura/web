$(function() {
	var id = $("#race-id").val();

	$("#registration-forms").click(function() {
		$(this).button('loading');
		RaceProxy.formDownload(id);
	});

	RaceProxy.loadSummary(id).done(loadOk);
	// RaceRegistrationProxy.find(id).done(findOk);

	RaceAnalyticsProxy.getByCategories(id).done(getByCategoriesOk);
	RaceAnalyticsProxy.getByCourses(id).done(getByCoursesOk);
	RaceAnalyticsProxy.getByStatus(id).done(getByStatusOk);
	RaceAnalyticsProxy.getByLocation(id).done(getByLocationOk);
	RaceAnalyticsProxy.getByTshirt(id).done(getByTshirtOk);
});

function loadOk(data) {
	$(".race-name").text(data.name);

	if (data.status === 'closed' || data.status === 'end') {
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

function getByCoursesOk(data) {
	fillChartData(data, $("#course-chart"));
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
