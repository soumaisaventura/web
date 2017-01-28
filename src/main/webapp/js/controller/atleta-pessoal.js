$(function () {
    $("#user-profile-menu-item").addClass("active");
    $("#name").focus();

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
                'id': $("#city").val()
            },
            'gender': $("#gender").val(),
            'tshirt': $("#tshirt").val(),
            'mobile': $("#mobile").val(),
            'orienteering': {
                'national_id': $("#national_id").val(),
                'sicard_number': $("#sicard_number").val()
            }
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

    LocationProxy.findStates("br").done(loadUFOk);

    if (data.city && data.city.state) {
        LocationProxy.findCities("br", data.city.state.id).done(loadCitiesOk);
    }

    $("#mobile").val(data.mobile);
    $("#form-section").show();

    $("#national_id").val(data.orienteering.national_id);
    $("#sicard_number").val(data.orienteering.sicard_number);
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
    user.name = $("#name").val().split(" ")[0];
    App.setLoggedInUser(user);

    if (user.pendencies && user.pendencies.health > 0) {
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
