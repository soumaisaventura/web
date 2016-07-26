$(function () {
    var year = $("#year").val();

    $("#race-next-menu-item").addClass("active");
    $("#race-next-menu-item-" + year).addClass("active");
    EventProxy.find(year).done(findOk);

    $('#open-events').on('click', '.panel', function () {
        location.href = App.getContextPath() + "/evento/" + $(this).data("event");
    });

    $('#open-events').on('mouseover', '.panel', function () {
        $(this).css('cursor', 'pointer');
    });
});

function findOk(data) {
    new Riloadr({
        breakpoints: [{
            name: '1170',
            minWidth: 1200
        }, {
            name: '970',
            minWidth: 992
        }, {
            name: '750',
            maxWidth: 991
        }]
    });

    var template = $('#template');

    $.each(data, function (index, event) {
        var day = App.moment(event.period.beginning);

        // event.date = day.locale("pt-br").format("DD [de] MMMM");
        event.date = App.parsePeriod(event.period, true);
        event.place = App.parseCity(event.location.city);
        event.status = moment().year() !== day.year() && event.status === "end" ? null : event.status;

        switch (event.status) {
            case "open":
                event.corner = "inscrições abertas";
                break;
            case "suspended":
                event.corner = "inscrições suspensas";
                break;
            case "closed":
                event.corner = "inscrições encerradas";
                break;
            default:
                event.corner = "";
        }

        var rendered = Mustache.render(template.html(), event);
        $('#open-events').append(rendered);
    });
    template.remove();
}
