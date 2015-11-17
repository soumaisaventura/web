$(function() {
	moment.locale("pt-br");
	numeral.language('pt-br');
	numeral.defaultFormat('$ 0,0');

	var evento_id = $("#evento_id").val();
	
	EventoProxy.load(evento_id).done(loadOk);
	EventoProxy.getBanner(evento_id).done(getBannerOk);
	
});

function getBannerOk(data) {
	if (data) {
		$("#banner").attr("src", "data:image/png;base64," + data);
	}
}

function loadOk(event) {
	
	console.log(event);
	$(".jumbotron").css("background-color", event.layout.background_color);
	
	$.get( $("#contextPath").val() + '/js/controller/race.mst', function(template) {

		$.each(event.races, function(i, race){
						
			event.races[i].idx = i+1;
			if (race.period.beginning === race.period.end){
				event.races[i].date = moment(race.period.beginning, "YYYY-MM-DD").locale("pt-br").format('LL'); 
			} else {
				event.races[i].date = moment(race.period.beginning).format('L') + " à " + moment(race.period.end).format('L');
			}
			
			$.each(race.prices, function(j, price){
				event.races[i].prices[j].beginning = moment(price.beginning).format('DD/MM');
				event.races[i].prices[j].end = moment(price.end).format('DD/MM');
			});
		});
		
		event.contextPath = $("#contextPath").val();
		
		var rendered = Mustache.render(template, event);
	    
	    $('#provas').html(rendered);
	});
}