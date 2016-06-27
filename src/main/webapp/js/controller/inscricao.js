$(function () {
    moment.locale("pt-br");
    numeral.language('pt-br');
    numeral.defaultFormat('0.00');

    $("#facebook-share").click(function (event) {
        App.shareOnFacebook({
            appId: $("#facebook-share").data("app-id"),
            teamName: $("#team-name").text(),
            race: $(this).data("race")
        })
    });

    LogonProxy.getOAuthAppIds().done(getOAuthAppIdsOk);
    RegistrationProxy.load($("#registration").val()).done(loadOk);
});

function getOAuthAppIdsOk(data) {
    $("#facebook-share").data("app-id", data.facebook);
}

function sendPaymentOk(data, status, request) {
    openPaymentFlow(data);
    updatePaymentButton(data);
}

function sendPaymentFailed(request) {
    switch (request.status) {
        case 502:
            bootbox.alert("Ocorreu uma falha na comunicação com o PagSeguro. Espere alguns minutos e tente novamente.");
            break;
    }
}

function updateBreadcrumb(data) {
    var user = App.getLoggedInUser();
    var authorized = user ? user.roles.admin : false;

    if (user && user.roles && user.roles.organizer && data.race.event.organizers && data.race.event.organizers.length > 0) {
        $.each(data.race.event.organizers, function (i, value) {
            if (user.id === value.id) {
                authorized = true;

            }
        });
    }

    if (authorized) {
        $(".breadcrumb.organizer").show();
    } else {
        $("#registration-list-menu-item").addClass("active");
        $(".breadcrumb.athlete").show();
    }
}

function loadOk(registration) {
    updateBreadcrumb(registration);

    $("#registration-id").text(registration.number);
    $("#team-name").text(registration.team.name);
    $("#race-status").html(App.translateStatus(registration.status));
    $("#race-status").data('status', registration.status);
    $("#summary-section").show();

    $(".race-name").text(registration.race.event.name);
    $("#race-description").text(registration.race.description);
    $("#race-date").text(moment(registration.race.period.beginning).format('LL'));
    $("#race-city").text(App.parseCity(registration.race.event.location.city));
    $("#race-section").show();

    $("#race-category").text(registration.category.name + " " + registration.race.name);
    $("#facebook-share").data("race", registration.race);

    var memberTemplate = $('#member-template');
    var user = App.getLoggedInUser();
    var isMember = false;

    if (App.isAdmin() || App.isOrganizer(registration.race.event.organizers)) {
        $("#edit").attr("href", App.getContextPath() + "/evento/" + registration.race.event.id + "/" + registration.race.id + "/inscricao/" + registration.number);
        $("#edit").parent().show();
    }

    $.each(registration.team.members, function (i, member) {
        member.formattedAmount = numeral(member.amount).format();

        var rendered = Mustache.render(memberTemplate.html(), member);
        $("#team-formation > tbody:last").append(rendered);

        if (user && member.id == user.id) {
            isMember = true;
        }
    });

    updateTotal();
    $("#team-section").show();

    if (registration.status == 'pendent') {
        $(".payment-type").hide();
        $(".payment-type-" + registration.race.event.payment.type).show();
        $('#registration-payment-info').html(
            registration.race.event.payment.info ? App.parseText(registration.race.event.payment.info) : '');

        var checkout_code, transaction_code;
        if (registration.payment) {
            checkout_code = registration.payment.checkout_code;
            transaction_code = registration.payment.transaction_code;
        }
        updatePaymentButton(checkout_code, transaction_code);

        $('#payment-section').show();
    }

    var isAuthorized = user ? App.isAdmin() : null;
    if (registration.race.event.organizers) {
        $.each(registration.race.event.organizers, function (i, organizer) {
            var row = "";
            row = row.concat("<div class='col-md-6' style='padding-bottom: 30px;'>");
            row = row.concat("<span class='glyphicon glyphicon-user' aria-hidden='true' style='font-size: 0.8em'></span>&nbsp;");
            row = row.concat(organizer.profile.name);
            row = row.concat("<br/>");
            row = row.concat("<span class='glyphicon glyphicon-envelope' aria-hidden='true' style='font-size: 0.8em'></span>&nbsp;");
            row = row.concat(organizer.email);

            if (organizer.profile.mobile) {
                row = row.concat("<br/>");
                row = row.concat("<span class='glyphicon glyphicon-phone' aria-hidden='true' style='font-size: 0.8em'></span>&nbsp;");
                row = row.concat(organizer.profile.mobile);
            }

            if (user && organizer.id == user.id) {
                isAuthorized = true;
            }

            row = row.concat("</div>");
            $("#race-organizers").append(row);
        });
        $("#organizers-section").show();
    }

    $("#registration-submitter").text(registration.submitter.profile.name);
    $("#registration-date").text(moment(registration.date).format('L'));
    $("#footer-section").show();

    loadEditableCurrency(registration.id, isAuthorized && registration.status == 'pendent');
    loadEditableTeamName(registration.id, registration.race.status == 'open' && (isAuthorized || isMember));
}

function loadEditableCurrency(registrationId, enabled) {
    $(".editable").editable({
        type: 'text',
        disabled: !enabled,
        emptyclass: '',
        display: function (value, sourceData) {
            $(this).text(numeral(value).format())
        },
        url: function (params) {
            var d = new $.Deferred;
            var value = JSON.stringify(numeral().unformat(params.value));

            var updateValuesOk = function (data) {
                d.resolve();
                updateTotal();
                updatePaymentButton();
            };

            var updateValuesFailed = function (request) {
                var message = null;
                if (request.status == 422) {
                    message = request.responseJSON[0].message;
                }

                d.reject(message);
            };

            if (params.property === 'amount') {
                RegistrationProxy.updateAmount(registrationId, params.pk, value).done(updateValuesOk).fail(updateValuesFailed);
            }

            return d.promise();
        }
    });

    $('.editable').on('shown', function () {
        var input = $(this).data('editable').input.$input;

        input.mask('##0,00', {
            selectOnFocus: true,
            placeholder: "0",
            reverse: true
        });
    });
}

function loadEditableTeamName(registrationId, enabled) {
    $('#team-name').editable({
        type: 'text',
        disabled: !enabled,
        emptyclass: '',
        url: function (params) {
            var d = new $.Deferred;

            var updateValuesOk = function (data) {
                d.resolve();
            };

            var updateValuesFailed = function (request) {
                var message = null;
                if (request.status == 422) {
                    message = request.responseJSON[0].message;
                }
                d.reject(message);
            };

            RegistrationProxy.updateTeamName(registrationId, params.value).done(updateValuesOk).fail(updateValuesFailed);
            return d.promise();
        }
    });
}

function updateTotal() {
    var partials = {};
    $(".editable").each(function () {
        var key = $(this).data("pk");
        var value = numeral().unformat($(this).text());
        partials[key] = (partials[key] ? partials[key] : 0.0) + value;
    });

    var total = 0;
    $(".partial").each(function () {
        var key = $(this).data("pk");
        var value = partials[key];
        $(this).text(numeral(value).format());
        total += value;
    });

    $("#payment-ammount").data("value", total);
    $("#payment-ammount").text("R$ " + numeral(total).format());
}

function updatePaymentButton(checkoutCode, transactionCode) {
    if ($("#payment-ammount").data("value") == 0 || $("#race-status").data('status') != 'pendent') {
        $("#payment-section").hide();
    } else {
        $("#payment-section").show();
    }

    $("#payment").unbind('click');
    if (transactionCode) {
        $("#payment-alert").show();
        $("#payment").attr("disabled", true);

    } else if (checkoutCode) {
        $("#payment-alert").hide();
        $("#payment").attr("disabled", false);

        $("#payment").click(function () {
            $(this).button('loading');
            openPaymentFlow(checkoutCode);
        });
    } else {
        $("#payment-alert").hide();
        $("#payment").attr("disabled", false);

        $("#payment").click(function () {
            var $this = $(this);
            $this.button('loading');
            RegistrationProxy.sendPayment($("#registration").val()).done(sendPaymentOk).fail(sendPaymentFailed).always(function () {
                $this.button('reset');
            });
        });
    }
}

function openPaymentFlow(checkoutCode) {
    location.href = 'https://pagseguro.uol.com.br/v2/checkout/payment.html?code=' + checkoutCode;
}
