$(function () {
    moment.locale("pt-br");
    numeral.language('pt-br');
    numeral.defaultFormat('0.00');

    var excludes = [];
    var user = null;
    var eventId = $("#event_id").val();
    var raceId = $("#race_id").val();
    var inscricaoId = $("#inscricao_id").val();
    var race = null;

    if (!(user = App.getLoggedInUser())) {
        App.handle401();
    }

    LogonProxy.getOAuthAppIds().done(getOAuthAppIdsOk);

    RaceProxy.load(raceId, eventId).done(function (_race) {
        race = _race;
        loadRaceOk(race, user, excludes);
        if (inscricaoId) {
            RegistrationProxy.load(inscricaoId).done(function (registration) {
                loadRegistrationOk(registration);
                $.each(registration.team.members, function (i, user) {
                    user.raceHasKit = race.hasOwnProperty('kits');
                    RaceProxy.getOrder(race.id, race.event.id, user.id).done(function (order) {
                        getOrderOk(order, user);
                    });
                });
            });
        } else {
            RaceProxy.getOrder(race.id, race.event.id, user.id).done(function (order) {
                user.amount = order;
                getOrderOk(order, user);
            });
        }
    });

    $('#members-list').footable({
        breakpoints: {
            phone: 450
        }
    });

    $("#category\\.id").on("change", function () {
        updateTable();
    });

    $("#members_ids").autocomplete({
        source: function (request, response) {
            UserProxy.search(request.term, excludes).done(function (users) {
                response(convertToLabelValueStructureFromUser(users));
            });
        },
        minLength: 3,
        select: function (event, ui) {
            var member = ui.item;
            member.raceHasKit = race.hasOwnProperty('kits');
            $("#memberId").val(member.id);
            $("#members_ids").val(member.label);

            if (memberId) {
                RaceProxy.getOrder(raceId, eventId, member.id).done(function (order) {
                    member.amount = order;
                    getOrderOk(order, member);
                });
                $("#memberId").val(""); // Limpa o campo
                $("#members_ids").val(""); // Limpa o campo
            }
            return false;
        },
        focus: function (event, ui) {
            $("#memberId").val(ui.item.value);
            $("#members_ids").val(ui.item.label);
            return false;
        }
    }).autocomplete("instance")._renderItem = function (ul, item) {
        return $("<li></li>").data("data-value", item).append("<img src='" + item.thumbnail + "'> " + item.label).appendTo(ul);
    };

    $("#members-list").on("click", ".remove", function (e) {
        e.preventDefault();
        var teamId = $(this).data("id");
        excludes.splice($.inArray(teamId, excludes), 1);
        var row = $(this).parents('tr:first');
        $('#members-list').data('footable').removeRow(row);
        updateTable();
    });

    $("#kits-modal").on('show.bs.modal', function (event) {
        var button = $(event.relatedTarget);
        var memberId = button.data('member-id');
        var memberName = button.data('name');
        var modal = $(this);
        var template = $("#kits-template");
        modal.find('.modal-title').text('Escolha o kit de ' + memberName);
        $.each(race.kits, function (index, kit) {
            kit.parsedDescription = App.parseText(kit.description);
            kit.memberId = memberId;
        });
        var rendered = Mustache.render(template.html(), {"kits": race.kits});
        modal.find('.modal-body > .row').html(rendered);
    });

    $(document).on("click", ".kit-choice", function () {
        var memberId = $(this).data("member-id");
        var kitPrice = $(this).data("kit-price");
        var memberPrice = $("#amount-" + memberId).data("original-amount");
        var orderPrice = memberPrice + kitPrice;

        $("#kit-" + memberId).data("kit-id", $(this).data("kit-id"));
        $("#kit-" + memberId).html($(this).data("kit-name").toLowerCase());

        $("#amount-" + memberId).html(numeral(orderPrice).format());
        $("#amount-" + memberId).data("amount", orderPrice);
        updateTotal();

        $('#kits-modal').modal('hide');
    });

    $("form").submit(function (event) {
        event.preventDefault();
        $("[id$='-message']").hide();

        var _members = [];

        $(".member").each(function () {
            var memberId = $(this).data("id");
            var kitId = $("#kit-" + memberId).data("kit-id");

            var member = {};
            var kit = {};

            if (memberId) {
                member.id = memberId;
            }

            if (kitId) {
                kit.id = kitId;
                member.kit = kit;
            }

            _members.push(member);
        });

        var registration = {
            category: {
                id: $("#category\\.id").val()
            },
            team: {
                name: $("#team\\.name").val(),
                members: _members
            }
        };


        if (inscricaoId) {
            RegistrationProxy.update(inscricaoId, registration).done(updateOk).fail(App.handle422Global);
        } else {
            RaceRegistrationProxy.submitRegistration(raceId, eventId, registration).done(registrationOk).fail(App.handle422Global);
        }

    });
});

/* CALLBACKS */
function getOAuthAppIdsOk(appIds) {
    $("body").data("facebook-app-id", appIds.facebook);
}

function loadComboCategories(categories) {
    if (categories) {
        $.each(categories, function (i, category) {
            var option = new Option(category.name, category.id);
            $(option).data("teamsize", category.team_size);
            $(option).data("id", category.id);
            $("#category\\.id").append(option);
        });

        if (categories.length === 1 && categories[0].team_size !== 1) {
            $("#category\\.id").val(categories[0].id);
            $("#pesquisa-atleta").show();
        }
    }
}

function loadRegistrationOk(registration) {
    $("#team\\.name").val(registration.team.name);
    $("#category\\.id").val(registration.category.id);
    $("#summary-section, #submit-button-section, #members-section").show();
}

function loadRaceOk(race, user, excludes) {
    if (race) {
        updateBreadcrumb(race);

        user.raceHasKit = race.hasOwnProperty('kits');
        excludes.push(user.id);

        $("#race-name").text(race.name);
        $("#race-description").text(race.description);

        $("#race-date").text(App.parsePeriod(race.period));
        $("#race-city").text(App.parseCity(race.event.location.city));
        $("body").data("race", race);

        loadComboCategories(race.categories);
    }
}

function updateBreadcrumb(data) {
    var authorized = App.isAdmin() || App.isOrganizer(data.event.organizers);

    if (authorized) {
        $(".breadcrumb.organizer").show();
    } else {
        $(".breadcrumb.athlete").show();
    }
}

function getOrderOk(order, athlete) {
    if (order) {
        athlete.valor_atual_da_prova = order;

        if (athlete.hasOwnProperty("amount")) {
            athlete.valor_formatado = numeral(athlete.amount).format();
        } else {
            athlete.valor_formatado = numeral(order).format();
        }

        if (athlete.hasOwnProperty("kit")) {
            athlete.kit.name = athlete.kit.name.toLowerCase();
        }

        var template = $("#member-template");
        var rendered = Mustache.render(template.html(), athlete);
        $('#members-list > tbody').append(rendered);

        updateTable();

        $("#summary-section, #submit-button-section, #members-section").show();

    } else {

        var url = $("#event_link").attr("href");
        window.location.href = url;

    }
}

function updateTable() {
    var rowCount = $('#members-list>tbody>tr').length;
    var teamsize = $('#category\\.id').find('option:selected').data("teamsize");
    if (!rowCount) {
        $("#pesquisa-atleta").show();
    } else {
        if (teamsize > 1 && rowCount < teamsize) {
            $("#pesquisa-atleta").show();
        } else {
            $("#pesquisa-atleta").hide();
        }
    }
    updateTotal();
}

function updateTotal() {
    var total = 0;
    $(".amount").each(function () {
        total += $(this).data("amount");
    });
    $("#total").text(numeral(total).format());
}

function convertToLabelValueStructureFromUser(data) {
    var newData = [];

    if (data) {
        $.each(data, function () {
            this.label = this.profile.name;
            this.value = this.id;
            this.thumbnail = this.picture.thumbnail;
            newData.push(this);
        });
    }

    return newData;
}

function registrationOk(data) {
    $("[id$='-message']").hide();
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
                        teamName: $("#team\\.name").val(),
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
    alert("sucesso!");
}