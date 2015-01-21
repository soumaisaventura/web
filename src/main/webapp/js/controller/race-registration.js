$(function() {
	
	/**
	 * Carrega os dados da corrida
	 */
	RaceProxy.load($("#race").val()).done(loadOk);
	
	/**
	 * Carrega a combo de categoria com as categorias disponíveis para a corrida
	 */
	RaceProxy.findCourses($("#race").val()).done(loadCategoriesOk);

	/**
	 * Objeto que guardará os dados da equipe
	 */
	var teamData;

	/**
	 * Array usado para armazenar os id dos membros da equipe Já inicializa com
	 * id do usuário logado
	 */
	var teamIds = [];
	UserProxy.getLoggedInUser().done(
		function(data) {
			teamIds.push(data.id);
			$("#logged-user").text(data.profile.name);
		}
	);

	/**
	 * Habilita o autocomplete no campo Atleta Ao selecionar o atleta
	 * automaticamente entra na lista de membros
	 */
	$("#user").autocomplete({
		source:	function(request, response) {
					UserProxy.search(request.term, teamIds).done(
							function(data) {
								response(convertToLabelValueStructureFromUser(data));
							}
						);
				},
		minLength: 3,
		select: function(event, ui) {
					teamIds.push(ui.item.value);
					$("#members-list").append(
						'<li class="list-group-item" id="member-'
						+ ui.item.value
						+ '">'
						+ ui.item.label
						+ '<a href="#" data-remove="'
						+ ui.item.value
						+ '"><span class="pull-right glyphicon glyphicon-remove" aria-hidden="true" title="Remover membro da equipe"></span></a></li>'
					);
					$("#user").val("");
					console.log(teamIds);
					return false;
				},
		focus : function(event, ui) {
					$("#user").val(ui.item.label);
					return false;
				}
	});

});

/* ---------------- Funções de Callback ---------------- */

/**
 * Carrega dados da corrida
 */
function loadOk(data) {
	$("#race-name").text(data.name + " - " + "Inscrição da Equipe")
	$("#race-detail").text(moment(data.date, "YYYY-MM-DD").locale("pt-br").format('LL') + " - " + data.city);
}

/**
 * Monta a caixa de seleção das categorias disponíveis da corrida. TODO Pensar
 * numa estrutura para pegar a quantidade de membros da corrida.
 */
function loadCategoriesOk(data) {
	$.each(data, function(index, value) {
		var course = value;
		$.each(course.categories, function(index, value) {
			$("#category").append(new Option(this.name + " (" + course.length + " km)", this.id + "#" + course.id));
		});

	});
}

/* ---------------- Funções Utilitárias ---------------- */

/**
 * Função utilitária que converte o objeto retornado no suggest para o formato
 * do jqueryUi.
 */
function convertToLabelValueStructureFromUser(data) {
	var newData = [];
	$.each(data, function() {
		newData.push({
			"label" : this.profile.name,
			"value" : this.id
		});
	});
	return newData;
}

