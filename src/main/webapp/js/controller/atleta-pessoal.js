$(function() {
	$("#name").focus();
	$("#user-profile-menu-item").addClass("active");

	/**
	 * Inicializa a combos Data de nascimento
	 */
	App.loadDateCombos($("#birthday"), $("#birthday-month"), $("#birthday-year"));

	/**
	 * Habilita o autocomplete no campo Cidade de residência
	 */

	// $("#city-select").select2({
	// ajax : {
	// url : App.getContextPath() + "/api/location/city",
	// dataType : 'json',
	// delay : 250,
	// data : function(params) {
	// return {
	// q : params.term, // search term
	// page : params.page
	// };
	// },
	// processResults : function(data, page) {
	// return {
	// results : data
	// };
	// },
	// cache : true
	// },
	// escapeMarkup : function(markup) {
	// return markup;
	// },
	// minimumInputLength : 3,
	// templateResult : function(item) {
	// return item.name + "/" + item.state;
	// },
	// templateSelection : function(item) {
	// return item.name + "/" + item.state;
	// }
	// });
	$("#city").autocomplete({
		source : function(request, response) {
			LocationProxy.searchCity(request.term).done(function(data) {
				response(convertToLabelValueStructure(data));
			});
		},
		minLength : 3,
		select : function(event, ui) {
			$("#city").val(ui.item.label);
			$("#city\\.id").val(ui.item.value);
			return false;
		},
		change : function(event, ui) {
			$("#city\\.id").val(ui.item ? ui.item.value : null);
			return false;
		},
		focus : function(event, ui) {
			$("#city\\.id").val(ui.item.value);
			$("#city").val(ui.item.label);
			return false;
		}
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
				"id" : $("#city\\.id").val()
			},
			'gender' : $("#gender").val(),
			'tshirt' : $("#tshirt").val(),
			'mobile' : $("#mobile").val()
		};
		UserProfileProxy.update(data).done(updateOk);
	});

	/**
	 * Carrega os dados pessoais
	 */
	UserProfileProxy.load().done(loadOk);
});

/* ---------------- Funções de Callback ---------------- */

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

	$("#city\\.id").val(data.city.id);

	if (data.city.name) {
		$("#city").val(data.city.name + "/" + data.city.state);
	}

	$("#mobile").val(data.mobile);
	$("#form-section").show();
}

function updateOk(data) {
	$("[id$='-message']").hide();
	var user = App.getLoggedInUser();
	user.pendencies.profile = null;
	user.name = $("#name").val();
	App.setLoggedInUser(user);

	if (user.pendencies.health && user.pendencies.health > 0) {
		swal({
			title : "Dados salvos",
			text : "Porém você ainda possui pendências nos dados de saúde.",
			confirmButtonText : "Resolver agora",
			type : "warning"
		}, function() {
			location.href = App.getContextPath() + "/user/health";
		});
	} else {
		App.restoreSavedLocation();
	}
}

/* ---------------- Funções Utilitárias ---------------- */

/**
 * Função utilitária que converte o objeto retornado no suggest para o formato
 * do jqueryUi.
 */
function convertToLabelValueStructure(data) {
	var newData = [];
	$.each(data, function() {
		newData.push({
			"label" : this.name + "/" + this.state,
			"value" : this.id
		});
	});
	return newData;
}
