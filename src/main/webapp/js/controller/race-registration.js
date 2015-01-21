$(function() {
	numeral.language('pt-br');
	var teamData;
	var $total = 0;
	var $teamIds = [];
	var $race = $("#race").val();
	
	/**
	 * Carrega os dados da corrida
	 */
	RaceProxy.load($("#race").val()).done(loadOk);
	
	/**
	 * Carrega a combo de categoria com as categorias disponíveis para a corrida
	 */
	RaceProxy.findCourses($race).done(loadCategoriesOk);

	/**
	 * Carrega o usuário logado na lista de membros da equipe
	 */
	UserProxy.getLoggedInUser().done(
		function($user) {
			console.log($user);
			RaceProxy.order($race, $user.id).done(function($order){
				console.log($order);
				$teamIds.push($order.rows[0].id);
				$total += $order.rows[0].amount;
				addRowOnMemberList($order.rows[0], true);
				showTotal($total);
			});
		}
	);

	/**
	 * Habilita o autocomplete no campo Atleta Ao selecionar o atleta
	 * automaticamente entra na lista de membros
	 */
	$("#user").autocomplete({
		source:	function(request, response) {
					UserProxy.search(request.term, $teamIds).done(
							function(data) {
								response(convertToLabelValueStructureFromUser(data));
							}
						);
				},
		minLength: 3,
		select: function(event, ui) {
					console.log(ui);
					RaceProxy.order($race, ui.item.value).done(function($order){
						$teamIds.push($order.rows[0].id);
						$total += $order.rows[0].amount;
						addRowOnMemberList($order.rows[0], false);
						showTotal($total);
					});
					$("#user").val("");
					console.log($teamIds);
					return false;
				},
		focus : function(event, ui) {
					$("#user").val(ui.item.label);
					return false;
				}
	});
	
	/**
	 * TODO Recalcular o total
	 * Adiciona um event listener para remoção nos membros inseridos na equipe
	 */
	$("#members-list").on("click", "a", function(e) {
		e.preventDefault();
		var index = $teamIds.indexOf($(this).data("remove"));
		if (index > -1) {
			$teamIds.splice(index, 1);
			$("#member-" + $(this).data("remove")).remove();
		}
	});
	
	/**
	 * TODO Ajustar método
	 * Cadastro dos dados da equipe
	 */
	$('#activate-step-4').on('click', function(e) {

		var data = {
			'teamName' : $("#teamName").val(),
			'category' : $("#category").val().split("#")[0],
			'course' : $("#category").val().split("#")[1],
			'members' : team
		};

		RaceRegistrationProxy.validateRegistration($("#race").val(), data).done(updateStep3Ok).fail(updateStep3Fail);
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
function convertToLabelValueStructureFromUser($data) {
	var newData = [];
	$.each($data, function() {
		newData.push({
			"label" : this.profile.name,
			"value" : this.id
		});
	});
	return newData;
}

function addRowOnMemberList($athlete, $exclude){
	console.log('addRowOnMemberList');
	var row = "";
	row = row.concat("<tr id='member-" + $athlete.id + "'>");
	row = $exclude ? 
			row.concat("<td></td>") : 
				row.concat("<td><a href='#' data-remove='" + $athlete.id + "'><span class='glyphicon glyphicon-trash'/></a></td>");
	row = row.concat("<td>" + $athlete.name + "</td>");
	row = row.concat("<td class='text-right'>" + numeral($athlete.racePrice).format('$ 0,0.00') + "</td>");
	row = row.concat("<td class='text-right'>" + numeral($athlete.annualFee).format('$ 0,0.00') + "</td>");
	row = row.concat("<td class='text-right text-success'><strong>" + numeral($athlete.amount).format('$ 0,0.00') + "</strong></td>");
	row = row.concat("</tr>");
	$("#members-list tbody").append(row);
}

function showTotal($total){
	$("#total").text(numeral($total).format('$ 0,0.00'));
}