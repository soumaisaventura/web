$(function () {
    $("#name").focus();
    $("#user-profile-menu-item").addClass("active");

    App.loadDateCombos($("#birthday"), $("#birthday-month"), $("#birthday-year"));

    $("#uf").change(function () {
        // $(".loaded-cities").remove();
        LocationProxy.findCities("br", this.value).done(loadCitiesOk);
    });

    $("form").submit(function (event) {
        event.preventDefault();
        $("[id$='-message']").hide();

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
        $("#uf").append($('<option>', {
            value: data.city.state.id,
            text: data.city.state.name,
            class: "loaded-ufs",
            selected: true
        }));

        $("#city").append($('<option>', {
            value: data.city.id,
            text: data.city.name,
            class: "loaded-cities",
            selected: true
        }));

        LocationProxy.findCities("br", data.city.state.id).done(loadCitiesOk);
    }

    LocationProxy.findStates("br").done(loadUFOk);

    $("#mobile").val(data.mobile);
    $("#form-section").show();
}

function loadUFOk(data) {
    loadLocation(data, $("#uf"));
}

function loadCitiesOk(data) {
    loadLocation(data, $("#city"));
}

function loadLocation(data, $element) {
    var selected;

    if ($element.val() != "") {
        selected = $element.find(":selected");
    }

    $.each(data, function (i, value) {
        $element.append($('<option>', {
            value: value.id,
            text: value.name,
            class: "loaded",
            selected: value.id == $element.val()
        }));
    });

    if (selected) {
        selected.remove();
    }
}

function updateOk(data) {
    $("[id$='-message']").hide();
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
