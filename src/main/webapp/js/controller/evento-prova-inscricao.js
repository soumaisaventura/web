$(function(){
	
	/**
	 * id da corrida
	 */
	//var prova_id = $("#prova_id").val();
	
	/**
	 * Carrega a combo de categoria com as categorias disponíveis para a corrida
	 */
	//RaceProxy.findCourses(prova_id).done(loadCategoriesOk);
});


/**
 * Monta a caixa de seleção das categorias disponíveis da corrida. 
 */
function loadCategoriesOk(data) {
	var option;
	$.each(data, function(index, course) {
		$.each(course.categories, function(index, category) {
			option = new Option(course.name + " – " + this.name, this.id + "#" + course.id);
			$(option).data("annualfee", course.annualFee);
			$(option).data("teamsize", category.teamSize);
			$("#category").append(option);
		});
	});
}