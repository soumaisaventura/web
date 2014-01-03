var persistenceService = new PersistenceService("api");

function novo(){
	location.href = "modality-edit.jsf";
}

function edit(id){
	location.href = "modality-edit.jsf?id="+id;
}

function excluir(id){
	if(confirm('Deseja realmente apagar esse registro?')){
		
	}
}

$(function(e) {
	
	var footable = $('#modality').footable().data('footable');

	// TODO Trocar o ajax pela chamada do método persistenceService
	// persistenceService.all("modality") 
	
	$.ajax({
		url : 'api/modality',
		dataType : 'json',
		success : function(result) {
			$.each(result, function(idx, obj) {
				var row = '<tr>';
				$.each(obj, function(key, value) {
					if (key != 'id') {
						row += '<td>' + value + '</td>';
					}
				});
				console.log(obj);
				row += '<td><button type="button" class="btn btn-sm btn-success" onclick="edit(' + obj.id + ')">Editar</button></td>';
				row += '<td><button type="button" class="btn btn-sm btn-danger" onclick="excluir(' + obj.id + ')">Excluir</button></td>';
				row += '</tr>';
				footable.appendRow(row);
			});
			footable.redraw();
		}
	});

});