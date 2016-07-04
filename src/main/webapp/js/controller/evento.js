$(function () {
    moment.locale("pt-br");
    numeral.language('pt-br');
    numeral.defaultFormat('$ 0,0');

    var id = $("#id").val();
    EventProxy.load(id).done(function (event) {
        loadEventOk(event);
        loadMap(event);
    });
});

function loadMap(event) {
    if (event.location.hotspots) {
        var map = initMap();
        var bounds = new google.maps.LatLngBounds();

        var labels = "ABCDEFGHIJKL";
        var legend = "";

        var template = $('#map-legend-template');

        $.each(event.location.hotspots, function (i, value) {
            // Exemplo de ícones: http://jsfiddle.net/doktormolle/LS7Wj/show/
            // value.icon = 'http://maps.gstatic.com/mapfiles/markers2/marker' +
            // labels[value.order - 1] + '.png';
            // value.icon_legend =
            // 'http://maps.gstatic.com/mapfiles/markers2/circle' +
            // labels[value.order - 1] + '.png';
            value.link = "https://maps.google.com/?q=" + value.coord.latitude + "," + value.coord.longitude;
            value.label = labels[value.order - 1];

            var marker = new google.maps.Marker({
                map: map,
                position: {
                    lat: value.coord.latitude,
                    lng: value.coord.longitude
                },
                title: value.name,
                label: labels[value.order - 1]
                // icon : value.icon
            });

            google.maps.event.addListener(marker, 'click', function () {
                window.open(value.link, "_blank");
            });

            bounds.extend(marker.getPosition());
            // legend += "<img src='" + icon + "'>" + value.name + "<br />"

            var rendered = Mustache.render(template.html(), value);
            $('#map-legend').append(rendered);
        });

        template.remove();

        centerMap(map, bounds);
        $("#location-section").show();
    }
}

function centerMap(map, bounds) {
    map.fitBounds(bounds);

    google.maps.event.addDomListener(window, "resize", function (event) {
        map.fitBounds(bounds);
    });

    google.maps.event.addDomListener(window, "orientationchange", function (event) {
        map.fitBounds(bounds);
    });
}

function initMap() {
    var options = {
        zoom: 14,
        maxZoom: 14,
        disableDefaultUI: true,
        draggable: false,
        scrollwheel: false,
        disableDoubleClickZoom: true,
        zoomControl: false,
        scaleControl: false
    };

    return new google.maps.Map($("#map")[0], options);
}

function loadEventOk(event) {
    new Riloadr({
        breakpoints: [{
            name: '',
            minWidth: 1
        }]
    });

    var user = App.getLoggedInUser();
    var authorized = user ? user.roles.admin : false;

    if (user && user.roles && user.roles.organizer && event.organizers && event.organizers.length > 0) {
        $.each(event.organizers, function (index, value) {
            if (value.id == user.id) {
                authorized = true;
                return false;
            }
        });
    }

    // Banner section
    $("#banner").attr("src", event.banner);
    $("#banner-section").show();

    // Info section
    $("#title").text(event.name);
    $("#breadcrumb-event-name").text(event.name);
    $("#location-city").text(App.parseCity(event.location.city));
    $("#date").text(App.parsePeriod(event.period));
    $("#description").text(event.description);

    if (!event.description) {
        $("#info-section .row").remove();
    }
    $("#info-section").show();

    if (event.site) {
        $("#site").text(event.site);
        $("#site").attr("href", event.site);
        $("#site-section").show();
    }

    var template;

    // Title
    template = $('#title-template');
    event.authorized = authorized;
    event.city = App.parseCity(event.location.city);
    event.date = App.parsePeriod(event.period);
    var rendered = Mustache.render(template.html(), event);
    $('#title-column').append(rendered);
    template.remove();

    // Organizers
    template = $('#organizer-template');
    if (event.organizers) {
        $.each(event.organizers, function (i, organizer) {
            var pattern = /[\(\)\- ]/g;
            organizer.profile.mobile_link = "tel:+55" + organizer.profile.mobile.replace(pattern, "");
            var rendered = Mustache.render(template.html(), organizer);
            $('#organizers').append(rendered);
        });
        $("#organizers-section").show();
    }
    template.remove();

    // Races

    template = $('#race-template');
    if (event.races) {
        $.each(event.races, function (i, race) {
            race.idx = i + 1;

            // Periods

            race.date = App.moment(race.period.beginning).format("DD [de] MMMM");
            race.day = App.moment(race.period.beginning).format('DD');
            race.month = App.moment(race.period.beginning).format('MMM').toUpperCase();
            race.period.date = App.parsePeriod(race.period);

            if (race.current_price) {
                race.current_price.end = App.moment(race.current_price.end).format('DD [de] MMM');
            }

            // Status

            switch (race.status) {
                case 'open':
                    race.status_button = true;
                    break;

                case 'end':
                    race.status_button = false;
                    race.status_class = "danger";
                    break;

                case 'closed':
                    race.status_button = false;
                    race.status_class = "danger";
                    break;

                case 'soon':
                    race.status_button = false;
                    race.status_class = "danger";
                    break;
            }

            // Prices

            if (race.prices) {
                $.each(race.prices, function (j, price) {
                    price.beginning = App.moment(price.beginning).format('DD/MM');
                    price.end = App.moment(price.end).format('DD/MM');
                });
            }

            // Kits

            if (race.kits) {
                $.each(race.kits, function (j, kit) {
                    kit.parsedDescription = App.parseText(kit.description + "\n", {linebreak: '•&nbsp;&nbsp;$1<br>'});
                });
            }

            var rendered = Mustache.render(template.html(), race);
            $('#races-section').append(rendered);
        });

        $('[data-toggle="tooltip"]').tooltip({html: true});
        // $('[data-toggle="popover"]').popover({html: true});
        $(".end, .closed").html("Inscrições encerradas");
        $(".soon").html("Inscrições em breve");

        // $(".hint-end, .hint-closed, .hint-soon").remove();
    }
    template.remove();
}
