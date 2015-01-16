$(function() {
	// RaceProxy.findNext().done(findNextOk);
	RaceProxy.getBanner(1).done(getBannerOk);
	RaceProxy.load(1).done(loadOk);
	$("#bt-registration").click(function(){
		location.href = "3/registration";
	});
});

function getBannerOk(data){
	$("#race-banner").attr("src","data:image/png;base64,"+data);
}

function loadOk(data){
	$("#race-name").prepend(data.name);
	$("#race-description").prepend(data.description);
	$("#race-date").prepend(moment(data.date, "YYYY-MM-DD").locale("pt-br").format('L'));
	$("#race-city").prepend(data.city);
	
	if (data.registration.open) {
		$("#race-registration-open").addClass('label label-success').text("Abertas");
	} else {
		$("#race-registration-open").addClass('label label-danger').text("Encerradas");
	}
	
	$.each(data.registration.periods, function(index, value){
		$("#race-registration-periods").append("<h4>" + moment(value.beginning, "YYYY-MM-DD").locale("pt-br").format('DD/MM') + " á " + moment(value.end, "YYYY-MM-DD").locale("pt-br").format('DD/MM') + " - R$ " + value.price + " <span style='font-size:0.8em; color:green'>(+anuidade)</span></h4>")
	});
	
	var category = "";
	$.each(data.courses, function(index, value){
		category += "<h4>" + value.length + " km</h4>";
		category += "<ul>";
		$.each(value.categories, function(index, value){
			category += "<li>" + value.name + " <span title='" + value.description + "'><span></li>";
		});
		category += "</ul>";
	});
	$("#race-categories").after(category);
}