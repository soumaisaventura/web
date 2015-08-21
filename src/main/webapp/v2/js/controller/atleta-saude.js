$(function() {
	$("#bloodType").focus();
	$("#user-health-menu-item").addClass("active");

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
		UserHealthProxy.update(data).done(updateOk);
	});

	/**
	 * Carrega os dados médicos
	 */
	UserHealthProxy.load().done(loadOk);

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
	$("#form-section").show();
}

/**
 * 
 */
function updateOk(data) {
	$("[id$='-message']").hide();
	var user = App.getLoggedInUser();
	user.health = null;
	App.setLoggedInUser(user);

	var content = {};
	if (App.getLoggedInUser().profile.pendencies > 0) {
		content = {
			title : "Dados salvos",
			message : "Porém você ainda possui pendências nos dados de pessoais. Deseja resolver isso logo?",
			buttons : {
				success : {
					label : "<span class='glyphicon glyphicon-thumbs-up' aria-hidden='true' style='font-size: 0.8em;'></span> Sim",
					className : "btn-success",
					callback : function() {
						location.href = App.getContextPath() + "/user/profile";
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
