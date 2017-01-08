$(function () {

    var eventoId = $("#event_id").val();

    EventProxy.load(eventoId).done(loadOk);

    $("#raceId").on("change", function () {
        var raceId = $(this).val();
        RaceProxy.findCategories(raceId, eventoId).done(findCategoriesOk);
    });

    $("#categoryId").on("change", function () {
        RegistrationProxy.find(eventoId).done(findOk);
    });

});

function loadOk(event) {
    $("#event-name").text(event.name);
    $("#location-city").text(App.parseCity(event.location.city));
    $("#date").text(App.parsePeriod(event.period));

    $.each(event.races, function (i, race) {
        var option = new Option(race.name, race.id);
        $("#raceId").append(option);
    });
}

function findCategoriesOk(categories) {
    $.each(categories, function (i, category) {
        var option = new Option(category.name, category.id);
        $("#categoryId").append(option);
    });
}

function findOk(data) {
    var template = $("#rank-template");
    var rendered = Mustache.render(template.html(), data);
    $('#rank-template').html(rendered);
}