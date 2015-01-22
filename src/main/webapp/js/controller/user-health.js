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
		HealthProxy.update(data).done(updateOk);
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
	$("#health-badge").text("");

	var content = {};

	if ($("#profile-badge").text().length > 0) {
		content = {
			title : "Dados salvos",
			message : "Porém você ainda possui pendências nos dados de pessoais. Deseja resolver isso logo?",
			buttons : {
				success : {
					label : "Sim",
					className : "btn-success",
					callback : function() {
						location.href = App.getContextPath() + "/user/profile";
					}
				},
				danger : {
					label : "Não",
					className : "btn-danger",
					callback : function() {
						App.restoreLocation();
					}
				}
			}

		}
	} else {
		content = {
			title : "Parabéns",
			message : "Todas as pendências cadastrais foram resolvidas.",
			buttons : {
				success : {
					label : "Ok",
					className : "btn-success",
					callback : function() {
						App.restoreLocation();
					}
				}
			}
		}
	}

	bootbox.dialog(content);
}
