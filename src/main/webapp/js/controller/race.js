$(function() {
	// RaceProxy.findNext().done(findNextOk);
	RaceProxy.getBanner(1).done(getBannerOk);
	RaceProxy.load(1).done(loadOk);
});

function getBannerOk(data){
	$("#race-banner").attr("src","data:image/png;base64,"+data);
}

function loadOk(data){
	$("#race-name").prepend(data.name);
	$("#race-date").prepend(moment(data.date, "YYYY-MM-DD").locale("pt-br").format('L'));
	
	if (data.registration.open) {
		$("#race-registration-open").addClass('label label-success').text("Abertas");
	} else {
		$("#race-registration-open").addClass('label label-danger').text("Encerradas");
	}
	
	$.each(data.registration.periods, function(index ,value){
		$("#race-registration-periods").append("<h4>" + moment(value.beginning, "YYYY-MM-DD").locale("pt-br").format('DD/MM') + " á " + moment(value.end, "YYYY-MM-DD").locale("pt-br").format('DD/MM') + " - R$ " + value.price + "</h4>")
	})
	
}