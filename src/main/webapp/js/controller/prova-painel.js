$(function() {

	var data = [ {
		value : 30,
		color : "#F7464A"
	}, {
		value : 50,
		color : "#46BFBD"
	}, {
		value : 100,
		color : "#FDB45C"
	}, {
		value : 40,
		color : "#949FB1"
	}, {
		value : 120,
		color : "#4D5360"
	}

	];

	var options = {
		segmentShowStroke : true,
		segmentStrokeColor : "#fff",
		segmentStrokeWidth : 2,
		percentageInnerCutout : 50,
		animation : true,
		animationSteps : 100,
		animationEasing : "easeOutBounce",
		animateRotate : true,
		animateScale : false,
		onAnimationComplete : null,
		labelFontFamily : "Arial",
		labelFontStyle : "normal",
		labelFontSize : 24,
		labelFontColor : "#666"
	};

	var ctx = $("#donut-chart").get(0).getContext("2d");
	var myDoughnut = new Chart(ctx).Doughnut(data, options);
});
