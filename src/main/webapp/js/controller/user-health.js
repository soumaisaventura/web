$(function() {
	$("#bloodType").focus();

	/**
	 * Cadastro dos dados médicos
	 */
	$("form").submit(function(event) {
		event.preventDefault();
		$("[id$='-message']").hide();

		var data = {
			'bloodType' : $("#bloodType").val(),
			'allergy' : $("#allergy").val(),
			'healthCareName' : $("#healthCareName").val(),
			'healthCareNumber' : $("#healthCareNumber").val(),
			'emergencyContactName' : $("#emergencyContactName").val(),
			'emergencyContactPhoneNumber' : $("#emergencyContactPhoneNumber").val(),
		};
		HealthProxy.update(data).done(updateOk).fail(updateFail);
	});

	/**
	 * Carrega os dados médicos
	 */
	HealthProxy.load().done(loadOk);

});

/* ---------------- Funções de Callback ---------------- */

/**
 * Funçao que carrega os dados médicos do usuário.
 */
function loadOk(data) {
	$("#bloodType").val(data.bloodType);
	$("#allergy").val(data.allergy);
	$("#healthCareName").val(data.healthCareName);
	$("#healthCareNumber").val(data.healthCareNumber);
	$("#emergencyContactName").val(data.emergencyContactName);
	$("#emergencyContactPhoneNumber").val(data.emergencyContactPhoneNumber);
}

/**
 * 
 */
function updateOk(data) {
	$("[id$='-message']").hide();
	$("#global-message").text("Dados atualizados com sucesso.").addClass("alert-success").show();
	$("#health-badge").text("");
}

/**
 * Tratamento de erro dos dados básicos.
 */
function updateFail(request) {
	$("#global-message").text("").removeClass("alert-success").hide();
}
