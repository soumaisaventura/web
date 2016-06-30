$(function () {
    $("#name").focus();
    $("#user-profile-menu-item").addClass("active");

    App.loadDateCombos($("#birthday"), $("#birthday-month"), $("#birthday-year"));

    $("#uf").change(function () {
        var $city = $("#city");
        $city.find(":disabled").prop('selected', true);
        $city.find(":enabled").remove();
        LocationProxy.findCities("br", this.value).done(loadCitiesOk);
    });

    $("form").submit(function (event) {
        event.preventDefault();
        $(".message").hide();

        var birthday = "";

        if ($("#birthday-year").val() && $("#birthday-month").val() && $("#birthday").val()) {
            birthday = $("#birthday-year").val() + "-" + $("#birthday-month").val() + "-" + $("#birthday").val();
        }

        var data = {
            'name': $("#name").val(),
            'birthday': birthday,
            'rg': $("#rg").val(),
            'cpf': $("#cpf").val(),
            'city': {
                "id": $("#city").val()
            },
            'gender': $("#gender").val(),
            'tshirt': $("#tshirt").val(),
            'mobile': $("#mobile").val()
        };
        
        UserProfileProxy.update(data).done(updateOk);
    });

    UserProfileProxy.load().done(loadOk);
});

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

    if (data.city) {
        addOption($("#uf"), data.city.state.id, data.city.state.id, true);
        addOption($("#city"), data.city.id, data.city.name, true);
    }

    LocationProxy.findCities("br", data.city.state.id).done(loadCitiesOk);
    LocationProxy.findStates("br").done(loadUFOk);

    $("#mobile").val(data.mobile);
    $("#form-section").show();
}

function addOption($element, value, text, selected) {
    $element.append($('<option>', {
        value: value,
        text: text,
        selected: selected
    }));
}

function loadUFOk(data) {
    $.each(data, function (i, value) {
        value.name = value.id;
    });

    loadLocation(data, $("#uf"));
}

function loadCitiesOk(data) {
    loadLocation(data, $("#city"));
}

function loadLocation(data, $element) {
    $.each(data, function (i, value) {
        var selected = value.id == $element.val();

        if (selected) {
            $element.find(":selected").remove();
        }

        addOption($element, value.id, value.name, selected);
    });
}

function updateOk(data) {
    $(".message").hide();
    var user = App.getLoggedInUser();
    user.profile.pendencies = null;
    user.name = $("#name").val();
    App.setLoggedInUser(user);

    if (user.health && user.health.pendencies > 0) {
        swal({
            title: "Dados salvos",
            text: "Porém você ainda possui pendências nos dados de saúde.",
            confirmButtonText: "Resolver agora",
            type: "warning"
        }, function () {
            location.href = App.getContextPath() + "/user/health";
        });
    } else {
        App.restoreSavedLocation();
    }
}

function convertToLabelValueStructure(data) {
    var newData = [];
    $.each(data, function () {
        newData.push({
            "label": this.name + "/" + this.state,
            "value": this.id
        });
    });
    return newData;
}
