$(function() {

	/**
	 * Coloca o foco no campo com id = name
	 * */
	$("#name").focus();
	
	/**
	 * Adiciona a classe css ao item com o id = user-profile-menu-item
	 */
	$("#user-profile-menu-item").addClass("active");

	/**
	 * Inicializa a combos Data de nascimento
	 */
	App.loadDateCombos($("#birthday"), $("#birthday-month"), $("#birthday-year"));

	/**
	 * Carrega o combo de estado e os campos de dados pessoais
	 */
	$.when(LocationProxy.loadStates(), UserProfileProxy.load()).done(function(states, user){
		App.loadStateCombos(states[0], $("#uf"));
		loadEventOk(user[0]);
	});
	
	/**
	 * Carrega combo de cidade ao selecionar um estado
	 */
	$("#uf").on("change", function(){
		LocationProxy.searchCity($(this).val()).done(searchCityOk);
	});
	
	/**
	 * Cadastro dos dados pessoais
	 */
	$("form").submit(function(event) {
		event.preventDefault();
		$("[id$='-message']").hide();

		var birthday = "";

		if ($("#birthday-year").val() && $("#birthday-month").val() && $("#birthday").val()) {
			birthday = $("#birthday-year").val() + "-" + $("#birthday-month").val() + "-" + $("#birthday").val();
		}

		var data = {
			'name' : $("#name").val(),
			'birthday' : birthday,
			'rg' : $("#rg").val(),
			'cpf' : $("#cpf").val(),
			'city' : {
				"id" : $("#city").val()
			},
			'gender' : $("#gender").val(),
			'tshirt' : $("#tshirt").val(),
			'mobile' : $("#mobile").val()
		};

		UserProfileProxy.update(data).done(updateOk);
	});


});

/* ---------------- Funções de Callback ---------------- */

function searchCityOk(data){
	App.loadCityCombos(data, $("#city"));
}

/**
 * Função que carrega os dados pessoais do usuário.
 */
function loadOk(data) {
	$("#name").val(data.name);
	$("#rg").val(data.rg);
	$("#cpf").val(data.cpf);

	if (data.birthday) {
		$("#birthday-year").val(parseInt(data.birthday.split("-")[0]));
		$("#birthday-month").val(parseInt(data.birthday.split("-")[1]));
		$("#birthday").val(parseInt(data.birthday.split("-")[2]));
	}

	$("#gender").val(data.gender);

	if (data.tshirt) {
		$("#tshirt").val(data.tshirt);
	}

	$("#uf").val(data.city.state);
	
	$.when(LocationProxy.searchCity(data.city.state).done(searchCityOk)).then(
			function(){
				$("#city").val(data.city.id);
			}
	);

	$("#mobile").val(data.mobile);
	$("#form-section").show();
}

/**
 * 
 */
function updateOk(data) {
	$("[id$='-message']").hide();
	var user = App.getLoggedInUser();
	user.profile.pendencies = null;
	user.profile.name = $("#name").val();
	;
	App.setLoggedInUser(user);

	var content = {};
	if (user.health && user.health.pendencies > 0) {
		content = {
			title : "Dados salvos",
			message : "Porém você ainda possui pendências nos dados de saúde. Deseja resolver isso logo?",
			buttons : {
				success : {
					label : "<span class='glyphicon glyphicon-thumbs-up' aria-hidden='true' style='font-size: 0.8em;'></span> Sim",
					className : "btn-success",
					callback : function() {
						location.href = App.getContextPath() + "/user/health";
					}
				},
				danger : {
					label : "<span class='glyphicon glyphicon-thumbs-down' aria-hidden='true' style='font-size: 0.8em;'></span> Não",
					className : "btn-danger",
					callback : function() {
						App.restoreSavedLocation();
					}
				}
			}

		}
	} else {
		content = {
			title : "Parabéns",
			message : "Você não possui pendências cadastrais.",
			buttons : {
				success : {
					label : "<span class='glyphicon glyphicon-ok' aria-hidden='true' style='font-size: 0.8em;'></span> Ok",
					className : "btn-success",
					callback : function() {
						App.restoreSavedLocation();
					}
				}
			}
		}
	}

	bootbox.dialog(content);
}
