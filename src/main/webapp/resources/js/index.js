function change(data) {
	var r = data.match(/^\s*([0-9]+)\s*-\s*([0-9]+)\s*-\s*([0-9]+)$/);
	return r[3]+"/"+r[2]+"/"+r[1];
}

$(function(e) {

	var footable = $('#evento').footable().data('footable');
	
	console.log(footable);

	$.ajax({
		url : 'api/evento',
		success : function(result) {
			$.each(result, function(idx, obj) {
				var row = '<tr>';
				$.each(obj, function(key, value) {
					if (key != 'id' && key != 'link') {
						if(key == 'data'){
							value = change(value);
						}
						row += '<td>' + value + '</td>';
					}
				});
				row += '<td><img src="images/orienteering.png" title="Orientação"/> <img src="images/mountainbiking.png" title="Mountain Bike"/></td>';
				row += '<td><button type="button" class="btn btn-sm btn-info">Detalhes</button></td>'
				row += '<td><button type="button" class="btn btn-sm btn-success">Inscrição</button></td>';
				row += '</tr>';
				footable.appendRow(row);
			});
		}
	});
});