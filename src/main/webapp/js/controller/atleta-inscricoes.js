$(function () {
    moment.locale("pt-br");
    $("#registration-list-menu-item").addClass("active");

    RegistrationProxy.find().done(findOk);
});

function findOk($data, $status, $request) {
    switch ($request.status) {
        case 204:
            var message = "Você não se inscreveu em nenhuma prova ainda.";
            $('#resultTable > tbody:last').append('<tr><td>' + message + '</td></tr>');

            break;

        case 200:
            $.each($data, function (index, value) {
                var td1;
                td1 += "<td class='col-md-4 text-center' style='vertical-align:middle; padding: 0px; padding-top: 20px; padding-bottom: 20px;'>";
                td1 += "<div >";
                td1 += "<a href='" + App.getContextPath() + "/inscricao/" + value.number + "'>";
                td1 += "<h3 style='margin: 0px;'>#" + value.number + "</h3>";
                td1 += "</a>";
                td1 += "<h4 style='margin: 0px;'>";
                td1 += "<span>" + App.translateStatus(value.status) + "</span>";
                td1 += "</h4>";
                td1 += "</div>";
                td1 += "</td>";

                td1 += "<td class='text-left' style='vertical-align:middle; padding: 20px;'>";
                td1 += "<div>";
                td1 += "<h3 style='margin: 0px;'>";
                td1 += value.race.event.name;
                td1 += "</h3>";
                // td1 += "<h5>";
                td1 += "<span class='glyphicon glyphicon-calendar' style='font-size: 0.8em'></span> ";
                td1 += moment(value.race.period.beginning, "YYYY-MM-DD").locale("pt-br").format('LL');
                td1 += "&nbsp;&nbsp;";
                td1 += "<span class='glyphicon glyphicon-map-marker' style='font-size: 0.8em'></span> ";
                td1 += value.race.event.location.city.name + "/" + value.race.event.location.city.state;
                // td1 += "</h5>";
                td1 += "<h5 style='padding-top: 5px; margin: 0px;'>";
                td1 += value.team.name;
                td1 += "</h5>";
                td1 += "</div>";

                td1 += "</td>";
                $('#resultTable > tbody:last').append('<tr>' + td1 + '</tr>');
            });
            break;
    }
}
