$(function () {
    moment.locale("pt-br");
    numeral.language('pt-br');
    numeral.defaultFormat('0.00');

    setRegistrationId($("#inscricao_id").val());

    LogonProxy.getOAuthAppIds().done(getOAuthAppIdsOk);
    RaceProxy.load($("#race_id").val(), getEventId()).done(loadRaceOk);

    $('#members-list').footable({
        breakpoints: {
            phone: 450
        }
    });

    $("#members_ids").autocomplete({
        source: function (request, response) {
            UserProxy.search(request.term, getMembersIds()).done(function (users) {
                response(users);
            });
        },
        minLength: 3,
        select: function (event, ui) {
            addMember(ui.item, getRace().current_price.price, null);
            $(event.target).val("");
            return false;
        },
        focus: function (event, ui) {
            $("#memberId").val(ui.item.profile.id);
            $("#members_ids").val(ui.item.profile.name);
            return false;
        }
    }).autocomplete("instance")._renderItem = function (ul, item) {
        return $("<li></li>").append("<li><img src='" + item.picture.thumbnail + "'> " + item.profile.name).appendTo(ul);
    };

    $("#category-id").change(updateSearchSection);
    $("#members-list").on("click", ".remove", removeMember);
    $("#kits-modal").on('show.bs.modal', showKitsModal);
    $(document).on("click", ".kit-item .btn", selectKit);
    $("form").submit(submit);
});

function submit(event) {
    event.preventDefault();
    $(".message").hide();

    var registration = {
        category: {
            id: $("#category-id").val()
        },
        team: {
            name: $("#team-name").val(),
            members: getMembers()
        }
    };

    var $button = $("button[type='submit']").attr("disabled", "disabled");
    var registrationId = getRegistrationId();
    if (registrationId) {
        RegistrationProxy.update(registrationId, registration).done(updateOk).fail(App.handle422Global).always(function () {
            $button.removeAttr("disabled");
        });
    } else {
        RaceRegistrationProxy.submitRegistration(getRace().id, getEventId(), registration).done(registrationOk).fail(App.handle422Global).always(function () {
            $button.removeAttr("disabled");
        });
    }
}

function showKitsModal(event) {
    var member = $(event.relatedTarget).parents(".member");
    var memberId = $(member).data('id');
    var memberName = $(member).data('name');

    var modal = $(event.currentTarget);
    modal.find('.modal-title').text('Escolha o kit de ' + memberName);

    var race = getRace();
    $.each(race.kits, function (index, kit) {
        kit.parsed_description = App.parseText(kit.description);
        kit.member_id = memberId;
    });

    var template = $("#kits-template");
    var rendered = Mustache.render(template.html(), race.kits);
    modal.find('.modal-body > .row').html(rendered);
}

function selectKit(event) {
    var kit_item = $(event.currentTarget).parents(".kit-item");
    var $kit_item = $(kit_item);

    var member = $(".member[data-id='" + $kit_item.data("member-id") + "']");
    var $member = $(member);

    var kit = $member.find(".kit");
    var $kit = $(kit);

    var amount = $member.find(".amount");
    var $amount = $(amount);
    var newAmount = getRace().current_price.price + $kit_item.data("kit-price");

    $member.data("kit-id", $kit_item.data("kit-id"));
    $member.data("amount", newAmount);
    $kit.html($kit_item.data("kit-name").toLowerCase());
    $amount.html(numeral(newAmount).format());

    updateTotal();
    $('#kits-modal').modal('hide');
}

function loadRaceOk(data) {
    updateBreadcrumb(data);
    setRace(data);

    $("#race-name").text(data.name);
    $("#race-description").text(data.description);

    $("#race-date").text(App.parsePeriod(data.period));
    $("#race-city").text(App.parseCity(data.event.location.city));

    $.each(data.categories, function (i, value) {
        var option = $('<option>', {
            value: value.id,
            text: value.name + (value.vacant ? "" : " (ESGOTADO)"),
            disabled: !value.vacant
        });
        $("#category-id").append(option.data("team-size", value.team_size));
    });

    $("#summary-section").show();

    var inscricaoId = getRegistrationId();
    if (inscricaoId) {
        RegistrationProxy.load(inscricaoId).done(loadRegistrationOk);
    } else {
        loadNewRegistration(data);
    }
}

function loadNewRegistration(race) {
    addMember(App.getLoggedInUser(), race.current_price.price, null);
    $("#members-section").show();
    $("#submit-button-section").show();
}

function loadRegistrationOk(registration) {
    $("#team-name").val(registration.team.name);
    $("#category-id").val(registration.category.id);

    $.each(registration.team.members, function (i, value) {
        addMember(value, value.amount, value.kit);
    });

    $("#members-section").show();
    $("#submit-button-section").show();
}

function addMember(member, amount, kit) {
    member.formmated_ammount = numeral(amount).format();
    member.kit_selection = isKitEnabledForRace();
    member.amount = amount;

    if (kit) {
        member.kit = kit;
        member.kit.name = kit.name.toLowerCase();
    }

    var template = $("#member-template");
    var rendered = Mustache.render(template.html(), member);
    $('#members-list > tbody').append(rendered);

    updateTotal();
    updateSearchSection();
}

function removeMember(event) {
    event.preventDefault();
    $(event.currentTarget).parents(".member").remove();
    updateTotal();
    updateSearchSection();
}

function updateBreadcrumb(data) {
    var authorized = App.isAdmin() || App.isOrganizer(data.event.organizers);

    if (authorized) {
        $(".breadcrumb.organizer").show();
    } else {
        $(".breadcrumb.athlete").show();
    }
}

function updateTotal() {
    var total = 0;
    $(".member").each(function () {
        total += $(this).data("amount");
    });
    $("#total").text(numeral(total).format());
}

function updateSearchSection() {
    var categoryTeamSize = $("#category-id").find(":selected").data("team-size");
    var currentTeamSize = $(".member").length;
    var $section = $("#search-member-section");

    if (currentTeamSize == 0 || categoryTeamSize > currentTeamSize) {
        $section.show();
    } else {
        $section.hide();
    }
}

function registrationOk(data) {
    $(".message").hide();
    var url = App.getContextPath() + "/inscricao/" + data;

    bootbox.dialog({
        title: "Parabéns",
        message: "Seu pedido de inscrição <strong>#" + data
        + "</strong> foi registrado com sucesso. Compartilhe esta boa notícia com os seus amigos.",
        buttons: {
            main: {
                label: "Compartilhar no Facebook",
                className: "btn-primary",
                callback: function () {
                    App.shareOnFacebook({
                        appId: $("body").data("facebook-app-id"),
                        teamName: $("#team-name").val(),
                        race: $("body").data("race")
                    });
                    location.href = url;
                }
            }
        },
        onEscape: function () {
            location.href = url;
        }
    });
}

function updateOk(data) {
    swal({
        title: "Inscrição atualizada com sucesso!",
        // text: "",
        type: "success"
    }, function () {
        window.location.href = App.getContextPath() + "/inscricao/" + $("#inscricao_id").val();
    });
}

function getEventId() {
    return $("#event_id").val();
}

function getMembers() {
    var result = [];
    var member;
    var kit_id;

    $(".member").each(function (i, value) {
        member = {
            id: $(value).data("id")
        };

        if (kit_id = $(value).data("kit-id")) {
            member.kit = {
                id: kit_id
            }
        }

        result.push(member);
    });

    return result;
}

function getMembersIds() {
    var result = [];

    $.each(getMembers(), function (i, value) {
        result.push(value.id);
    });

    return result;
}

function setRegistrationId(registrationId) {
    $("body").data("inscricao-id", registrationId);
}

function getRegistrationId() {
    return $("body").data("inscricao-id");
}

function setRace(race) {
    $("body").data("race", race);
}

function getRace() {
    return $("body").data("race");
}

function isKitEnabledForRace() {
    return getRace().kits != null;
}

function getOAuthAppIdsOk(appIds) {
    $("body").data("facebook-app-id", appIds.facebook);
}