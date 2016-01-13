$(function() {
	moment.locale("pt-br");

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
	EventAnalyticsProxy.getByStatusByDay(id).done(getByStatusByDayOk);
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

function getByStatusByDayOk(data) {
	var chartData = {};

	$.each(data, function(i, value) {
		var dateMoment = App.moment(value.date);
		var date = new Date(dateMoment.year(), dateMoment.month(), dateMoment.date());

		for ( var count in value.count) {
			if (!chartData[count]) {
				chartData[count] = [];
			}

			chartData[count].push({
				x : date,
				y : value.count[count]
			});
		}

		if ($.isEmptyObject(value.count)) {
			for ( var count in chartData) {
				chartData[count].push({
					x : date
				});
			}
		}
	});

	$("#status-by-day-chart").CanvasJSChart(
			{
				animationEnabled : true,
				axisX : {
					valueFormatString : "DD/MM",
					labelAngle : -40
				},
				toolTip : {
					content : function(e) {
						return moment(e.entries[0].dataPoint.x).format("dddd, DD [de] MMMM [de] YYYY") + "<br/> <span style =' color:"
								+ e.entries[0].dataSeries.color + "';>" + e.entries[0].dataSeries.name + "</span>: <strong>"
								+ e.entries[0].dataPoint.y + " inscriçoes</strong> <br/>";
					}
				},
				data : [ {
					name : "Canceladas",
					showInLegend : true,
					legendMarkerType : "square",
					type : "stackedArea",
					color : "rgba(211,19,14,.8)",
					markerSize : 0,
					dataPoints : chartData.cancelled
				}, {
					name : "Pendentes",
					showInLegend : true,
					legendMarkerType : "square",
					type : "stackedArea",
					markerSize : 0,
					color : "#FFBB03",
					dataPoints : chartData.pendent
				}, {
					name : "Confirmadas",
					showInLegend : true,
					legendMarkerType : "square",
					type : "stackedArea",
					markerSize : 0,
					color : "#4CB788",
					dataPoints : chartData.confirmed
				} ],
				legend : {
					verticalAlign : "top",
					horizontalAlign : "center",
					cursor : "pointer",
					itemclick : function(e) {
						if (typeof (e.dataSeries.visible) === "undefined" || e.dataSeries.visible) {
							e.dataSeries.visible = false;
						} else {
							e.dataSeries.visible = true;
						}
						chart.render();
					}
				}
			});
}

function getByCategoriesOk(data) {
	fillChartData(data, $("#category-chart"));
}

function getByRacesOk(data) {
	fillChartData(data, $("#race-chart"));
}

function getByLocationOk(data) {
	var chartData = [];
	$.each(data.reverse(), function(i, value) {
		chartData.push({
			y : value.value,
			label : value.label
		// label : null,
		// indexLabel : value.value
		});
	});

	$("#city-chart").CanvasJSChart({
		animationEnabled : true,
		axisX : {
			interval : 1,
		// labelAngle : -90,
		// margin : 30,
		// labelFontSize : 15,
		// labelMaxWidth : 40
		// labelAutoFit : true
		},
		axisY2 : {
			// minimum : 1
//			interval : 1
		// valueFormatString : "0"
		// lineThickness : 0
		},
		axisY : {
		// margin : 10
		// minimum : 1
		},
		data : [ {
			type : "bar",
			// indexLabelPlacement : "inside",
			axisYType : "secondary",
			dataPoints : chartData
		} ]
	});

	// $("#city-chart .canvasjs-chart-container
	// .canvasjs-chart-canvas:nth-child(1)").attr("style", "position:
	// relative");
	// $("#city-chart .canvasjs-chart-container
	// .canvasjs-chart-canvas:nth-child(1)").css("position", "relative");
	// $(".canvasjs-chart-canvas").style("position", "relative");
	// fillChartData(data, $("#city-chart"));
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
