$(function() {
	
	/**
	 * Cadastro dos dados médicos
	 */
	$('#bt-save').on('click', function(e) {
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
	console.log('loadOk');
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
	console.log('updateOk');
	$("[id$='-message'").hide();
	$("#global-message").text("Dados atualizados com sucesso.").addClass("alert-success").show();
}

/**
 * Tratamento de erro dos dados básicos.
 */
function updateFail(request) {
	console.log('updateFail');
	$("#global-message").text("").removeClass("alert-success").hide();
}