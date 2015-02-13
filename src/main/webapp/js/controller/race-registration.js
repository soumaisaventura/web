$(function() {
	moment.locale("pt-br");
	numeral.language('pt-br');
	numeral.defaultFormat('$ 0,0.00');

	var teamData;
	var $total = 0;
	var $teamIds = [];
	var $race = $("#race").val();

	$("#category").focus();
	$("#annual-fee-description").text(App.annualFeeDescription);

	LogonProxy.getOAuthAppIds().done(getOAuthAppIdsOk);

	/**
	 * Carrega os dados da corrida
	 */
	RaceProxy.loadSummary($("#race").val()).done(loadOk);

	/**
	 * Carrega a combo de categoria com as categorias disponíveis para a corrida
	 */
	RaceProxy.findCourses($race).done(loadCategoriesOk);

	/**
	 * Carrega o usuário logado na lista de membros da equipe
	 */
	var $user;

	if (!($user = App.getLoggedInUser())) {
		App.handle401();
	}

	RaceProxy.order($race, $user.id).done(function($order) {
		$teamIds.push($order.rows[0].id);
		$total += $order.rows[0].amount;
		addRowOnMemberList($order.rows[0], true);
		showTotal($total);
	});

	/**
	 * Habilita o autocomplete no campo Atleta Ao selecionar o atleta
	 * automaticamente entra na lista de membros
	 */
	$("#members").autocomplete({
		source : function(request, response) {
			UserProxy.search(request.term, $teamIds).done(function(data) {
				response(convertToLabelValueStructureFromUser(data));
			});
		},
		minLength : 3,
		select : function(event, ui) {
			$("#members-id").val(ui.item.value);
			$("#members").val(ui.item.label);
			return false;
		},
		focus : function(event, ui) {
			$("#members-id").val(ui.item.value);
			$("#members").val(ui.item.label);
			return false;
		}
	});

	/**
	 * 
	 */
	$("#bt-add-athlete").click(function() {
		if ($("#members").val() !== "") {
			RaceProxy.order($race, $("#members-id").val()).done(function($order) {
				$teamIds.push($order.rows[0].id);
				$total += $order.rows[0].amount;
				addRowOnMemberList($order.rows[0], false);
				showTotal($total);
			});
		}
		$("#members-id").val("");
		$("#members").val("");
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
	 * TODO Ajustar método Cadastro dos dados da equipe
	 */
	$("form").submit(function(event) {
		event.preventDefault();
		$("[id$='-message']").hide();

		var data = {
			'teamName' : $("#teamName").val(),
			'category' : $("#category").val().split("#")[0],
			'course' : $("#category").val().split("#")[1],
			'members' : $teamIds
		};

		RaceRegistrationProxy.submitRegistration($("#race").val(), data).done(registrationOk).fail(registrationFailed);
	});

});

/* ---------------- Funções de Callback ---------------- */

function getOAuthAppIdsOk($data) {
	$("#facebook-appid").val($data.facebook);
}

/**
 * Carrega dados da corrida
 */
function loadOk(data) {
	$("#race-name").text(data.name)
	$("#race-date").text(moment(data.date, "YYYY-MM-DD").locale("pt-br").format('LL'));
	$("#race-city").text(data.city);
}

/**
 * Monta a caixa de seleção das categorias disponíveis da corrida. TODO Pensar
 * numa estrutura para pegar a quantidade de membros da corrida.
 */
function loadCategoriesOk(data) {
	$.each(data, function(index, value) {
		var course = value;
		$.each(course.categories, function(index, value) {
			$("#category").append(new Option(this.name + " " + course.length + "km", this.id + "#" + course.id));
		});

	});
}

function registrationOk($data) {
	$("[id$='-message']").hide();
	var url = App.getContextPath() + "/registration/" + $data;

	bootbox.dialog({
		title : "Parabéns",
		message : "Seu pedido de inscrição <strong>#" + $data
				+ "</strong> foi registrado com sucesso. Compartilhe esta boa notícia com os seus amigos.",
		buttons : {
			main : {
				label : "Compartilhar no Facebook",
				className : "btn-primary",
				callback : function() {
					shareOnFacebook();
					location.href = url;
				}
			}
		},
		onEscape : function() {
			location.href = url;
		}
	});

	console.log(url);
}

function registrationFailed(request) {
	switch (request.status) {
		case 500:
			$("#members-message")
					.html(
							"Ocorreu um erro interno no servidor e sua inscrição não foi concluída. Informe ao administrador pelo e-mail: contato@soumaisaventura.com.br")
					.show();
			break;
	}
}

function shareOnFacebook() {
	var raceUrl = App.getBaseUrl() + "/race/" + $("#race").val();
	var url = "";
	url += "http://www.facebook.com/dialog/feed";
	url += "?app_id=" + $("#facebook-appid").val();
	url += "&name=" + $("#teamName").val() + " vai para a " + $("#race-name").text() + "!";
	url += "&description=Eu inscrevi a minha equipe na prova " + $("#race-name").text() + " que acontecerá no dia " + $("#race-date").text() + " em "
			+ $("#race-city").text() + ".";
	url += "&link=" + raceUrl;
	url += "&picture=" + App.getBaseUrl() + "/api/race/" + $("#race").val() + "/logo";
	url += "&redirect_uri=" + App.getBaseUrl() + "/close";
	url += "&actions=[{ name: 'Quero me inscrever agora mesmo!', link: '" + raceUrl + "/registration' }]";

	// var url = "";
	// url += "http://www.facebook.com/sharer/sharer.php?u=";
	// url += App.getBaseUrl() + "/registration/" + $("#registration-id").val()
	// + "/public";

	// console.log(url);

	window.open(url, '_blank');
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

function addRowOnMemberList($athlete, $exclude) {
	// if ($athlete.name.length > 30) {
	// $athlete.name = $athlete.name.substr(0, 27).concat("...");
	// }
	var row = "";
	row = row.concat("<tr id='member-" + $athlete.id + "'>");
	row = $exclude ? row.concat("<td></td>") : row.concat("<td><a href='#' data-remove='" + $athlete.id
			+ "'><span class='glyphicon glyphicon-trash'/></a></td>");
	row = row.concat("<td style='vertical-align:middle;'>" + $athlete.name + "</td>");
	row = row.concat("<td class='text-right' nowrap='nowrap' style='vertical-align:middle;'>" + numeral($athlete.racePrice).format() + "</td>");
	row = row.concat("<td class='text-right' nowrap='nowrap' style='vertical-align:middle;'>" + numeral($athlete.annualFee).format() + "</td>");
	row = row.concat("<td class='text-right' nowrap='nowrap' style='vertical-align:middle;'><em>" + numeral($athlete.amount).format() + "</em></td>");
	row = row.concat("</tr>");
	$("#members-list > tbody:last").append(row);
}

function showTotal($total) {
	$("#total").text(numeral($total).format());
}
