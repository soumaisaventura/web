$(function () {
    moment.locale("pt-br");
    numeral.language('pt-br');
    numeral.defaultFormat('0.00');

    var excludes = [];
    var user = null;
    var eventId = $("#event_id").val();
    var raceId = $("#race_id").val();

    if (!(user = App.getLoggedInUser())) {
        App.handle401();
    }

    LogonProxy.getOAuthAppIds().done(getOAuthAppIdsOk);
    RaceProxy.load(raceId, eventId).done(function (_race) {
        race = _race;
        user.raceHasKit = race.hasOwnProperty('kits');
        excludes.push(user.id);
        loadOk(race, user);
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
            $("#members_ids-message").hide();

            if (memberId) {
                RaceProxy.getOrder(raceId, eventId, member.id).done(function (order) {
                    getOrderOk(order, member);
                });
                $("#memberId").val(""); // Limpa o campo
                $("#members_ids").val(""); // Limpa o campo
            } else {
                $("#members_ids-message").html("Para incluir um atleta na equipe ele precisa se cadastrar no site e ativar a conta.").show();
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
            kit.parsedDescription = "<ul>" + App.parseText(kit.description + "\n", {linebreak: "<li>$1</li>"}) + "</ul>";
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
        RaceRegistrationProxy.submitRegistration(raceId, eventId, registration).done(registrationOk).fail(App.handle422Global);
    });

});

/* CALLBACKS */
function getOAuthAppIdsOk(_data) {
    $("#facebook-appid").val(_data.facebook);
}

function loadOk(_race, _user) {
    if (_race) {
        $("#race-name").text(_race.name);
        $("#race-description").text(_race.description);
        $("#race-date").text(App.parsePeriod(_race.period));
        $("#race-city").text(App.parseCity(_race.event.location.city));

        if (_race.categories) {
            $.each(_race.categories, function (i, category) {
                var option = new Option(category.name, category.id);
                $(option).data("teamsize", category.team_size);
                $(option).data("id", category.id);
                $("#category\\.id").append(option);
            });

            if (_race.categories.length === 1 && _race.categories[0].team_size !== 1) {
                $("#category\\.id").val(_race.categories[0].id);
                $("#pesquisa-atleta").show();
            }
        }

        RaceProxy.getOrder(_race.id, _race.event.id, _user.id).done(function (order) {
            getOrderOk(order, _user);
        });

        $("#summary-section, #submit-button-section, #members-section").show();
    }
}

function getOrderOk(_order, _athlete) {
    if (_order) {
        _athlete.race_price = _order;
        _athlete.formmated_race_price = numeral(_order).format();

        var template = $("#member-template");
        var rendered = Mustache.render(template.html(), _athlete);
        $('#members-list').data('footable').appendRow(rendered);

        updateTable();

        $("#summary-section, #submit-button-section, #members-section").show();
    } else {
        var url = $("#event_link").attr("href");
        window.location.href = url;
    }
}

function updateTable() {
    var rowCount = $('#members-list tbody tr').length;
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
    $.each(data, function () {
        this.label = this.name;
        this.value = this.id;
        this.thumbnail = this.picture.thumbnail;
        newData.push(this);
    });
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
                    shareOnFacebook();
                    location.href = url;
                }
            }
        },
        onEscape: function () {
            location.href = url;
        }
    });
}

function shareOnFacebook() {
    var raceUrl = App.getBaseUrl() + "/prova/" + $("#id").val();
    var url = "";
    url += "http://www.facebook.com/dialog/feed";
    url += "?app_id=" + $("#facebook-appid").val();
    url += "&name=" + $("#team\\.name").val() + " vai para a " + $("#race-name").text() + "!";
    url += "&description=Eu inscrevi a minha equipe na prova " + $("#race-name").text() + " que acontecerá no dia " + $("#race-date").text() + " em "
        + $("#race-city").text() + ".";
    url += "&link=" + raceUrl;
    url += "&picture=" + App.getBaseUrl() + "/api/race/" + $("#id").val() + "/banner";
    url += "&redirect_uri=" + App.getBaseUrl() + "/close";
    url += "&actions=[{ name: 'Quero me inscrever agora mesmo!', link: '" + raceUrl + "/inscricao' }]";

    window.open(url, '_blank');
}