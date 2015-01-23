$(function() {
	
	moment.locale("pt-br");
	numeral.language('pt-br');
	numeral.defaultFormat('$ 0,0');
	
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
			RaceProxy.order($race, $user.id).done(function($order){
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
					$("#user-id").val(ui.item.value);
					$("#user").val(ui.item.label);
					return false;
				},
		focus : function(event, ui) {
					$("#user-id").val(ui.item.value);
					$("#user").val(ui.item.label);
					return false;
				}
	});
	
	/**
	 * 
	 * */
	$("#bt-add-athlete").click(function(){
		if($("#user").val() !== ""){
			RaceProxy.order($race, $("#user-id").val()).done(function($order){
				$teamIds.push($order.rows[0].id);
				$total += $order.rows[0].amount;
				addRowOnMemberList($order.rows[0], false);
				showTotal($total);
			});
		}
		$("#user-id").val("");
		$("#user").val("");
	});
	
	
	/**
	 * Adiciona um event listener para remoção nos membros inseridos na equipe
	 */
	$("#members-list").on("click", "a", function(e) {
		e.preventDefault();
		var index = $teamIds.indexOf($(this).data("remove"));
		if (index > -1) {
			$teamIds.splice(index, 1);
			$total -= numeral().unformat($("#member-" + $(this).data("remove") + " > td:last-child")[0].innerText);
			$("#member-" + $(this).data("remove")).remove();
			showTotal($total);
		}
	});
	
	/**
	 * TODO Ajustar método
	 * Cadastro dos dados da equipe
	 */
	/**
	 * Cadastro dos dados médicos
	 */
	$("form").submit(function(event) {
		event.preventDefault();
		
		var data = {
			'teamName' : $("#teamName").val(),
			'category' : $("#category").val().split("#")[0],
			'course' : $("#category").val().split("#")[1],
			'members' : $teamIds
		};
		
		RaceRegistrationProxy.submitRegistration($("#race").val(), data).done(registrationOk).fail(registrationFail);
	});

});

/* ---------------- Funções de Callback ---------------- */

/**
 * Carrega dados da corrida
 */
function loadOk(data) {
	$("#race-name").text(data.name)
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

function registrationOk(data){
	$("[id$='-message']").hide();
	location.href = App.getContextPath() + "/registration/" + data;
}

function registrationFail($request){
	$("#global-message").text("").removeClass("alert-success").hide();
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
	$("[id$='-message']").hide();
	if($athlete.name.length > 30){
		$athlete.name = $athlete.name.substr(0,27).concat("...");
	}
	var row = "";
	row = row.concat("<tr id='member-" + $athlete.id + "'>");
	row = $exclude ? 
			row.concat("<td></td>") : 
				row.concat("<td><a href='#' data-remove='" + $athlete.id + "'><span class='glyphicon glyphicon-trash'/></a></td>");
	row = row.concat("<td>" + $athlete.name + "</td>");
	row = row.concat("<td class='text-right' nowrap='nowrap'>" + numeral($athlete.racePrice).format() + "</td>");
	row = row.concat("<td class='text-right' nowrap='nowrap'>" + numeral($athlete.annualFee).format() + "</td>");
	row = row.concat("<td class='text-right text-success' nowrap='nowrap'><strong>" + numeral($athlete.amount).format() + "</strong></td>");
	row = row.concat("</tr>");
	$("#members-list tbody").append(row);
}

function showTotal($total){
	$("#total").text(numeral($total).format());
}