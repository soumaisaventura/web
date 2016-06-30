$(function () {
    $("#bloodType").focus();
    $("#user-health-menu-item").addClass("active");

    /**
     * Cadastro dos dados médicos
     */
    $("form").submit(function (event) {
        event.preventDefault();
        $(".message").hide();

        var data = {
            'bloodType': $("#bloodType").val(),
            'allergy': $("#allergy").val(),
            'healthCareName': $("#healthCareName").val(),
            'healthCareNumber': $("#healthCareNumber").val(),
            'emergencyContactName': $("#emergencyContactName").val(),
            'emergencyContactPhoneNumber': $("#emergencyContactPhoneNumber").val(),
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
    if (data.bloodType) {
        $("#bloodType").val(data.bloodType);
    }

    $("#allergy").val(data.allergy);
    $("#healthCareName").val(data.healthCareName);
    $("#healthCareNumber").val(data.healthCareNumber);
    $("#emergencyContactName").val(data.emergencyContactName);
    $("#emergencyContactPhoneNumber").val(data.emergencyContactPhoneNumber);
    $("#form-section").show();
}

function updateOk(data) {
    $(".message").hide();
    var user = App.getLoggedInUser();
    user.health.pendencies = null;
    App.setLoggedInUser(user);

    if (user.profile && user.profile.pendencies > 0) {
        swal({
            title: "Dados salvos",
            text: "Porém você ainda possui pendências nos dados pessoais.",
            confirmButtonText: "Resolver agora",
            type: "warning"
        }, function () {
            location.href = App.getContextPath() + "/user/profile";
        });
    } else {
        App.restoreSavedLocation();
    }
}
