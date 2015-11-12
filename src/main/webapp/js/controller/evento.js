$(function() {
	moment.locale("pt-br");
	numeral.language('pt-br');
	numeral.defaultFormat('$ 0,0');

	var evento_id = $("#evento_id").val();
	
	EventoProxy.load(evento_id).done(loadOk);
	
});

function loadOk(event) {
	var i = 0;
	var now = moment("2015-03-05"); // TODO pegar data atual
	
	$.each(event.races, function( index, race ) {
		
		$.each(race.prices, function( index, value ) {
			if (now >= moment(value.beginning) && now <= moment(value.end)){
				race.price = value.price;
			} else {
				console.log('nok');
			}
		});
		
		i++;
		
		$.get('../js/controller/race.mst', function(template) {
			console.log(race);
			race.color = 'color' + i;
		    var rendered = Mustache.render(template, race);
		    $('#provas').html(rendered);
		  });
	});
}