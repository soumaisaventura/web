// Form events binding

$(function(e) {
	var proxy = new EventProxy();
	proxy.search(searchOk);

	$(document).on("click", ".register", function() {
		/*
		 * 1. Verificar se está logado 1.1 Sim - redirecionar para a página de
		 * registro 1.2 Não - mensagem de que é preciso estar logado para
		 * inscriçaõ em evento
		 */
		location.href = "register.jsf";
	});
});

// Event search process

function searchOk(data) {
	var footable = $('#evento').footable().data('footable');
	console.log(data);

	$
			.each(
					data,
					function(idx, obj) {
						var row = '<tr>';
						$.each(obj, function(key, value) {
							if (key != 'id' && key != 'link') {
								if (key == 'data') {
									value = change(value);
								}
								row += '<td>' + value + '</td>';
							}
						});
						row += '<td><img src="images/orienteering.png" title="Orientação"/> <img src="images/mountainbiking.png" title="Mountain Bike"/></td>';
						row += '<td><button type="button" class="btn btn-sm btn-info">Detalhes</button></td>';
						row += '<td><button type="button" class="btn btn-sm btn-success register" data-id="'
								+ obj.id + '">Inscrição</button></td>';
						row += '</tr>';
						footable.appendRow(row);
					});
	footable.redraw();
}

function change(data) {
	var r = data.match(/^\s*([0-9]+)\s*-\s*([0-9]+)\s*-\s*([0-9]+)$/);
	return r[3] + "/" + r[2] + "/" + r[1];
}
