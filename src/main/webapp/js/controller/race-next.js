$(function() {
	RaceProxy.findNext().done(findNextOk);

	$('table').on('click', '.registration', function() {
		location.href = App.getContextPath() + '/race/' + $(this).data("race") + '/registration';
	});
});

function findNextOk(data) {
	
	
	
	
	$
			.each(
					data,
					function(index, value) {
						var day = moment(value.date, "YYYY-MM-DD");

						var td1 = '<td class="text-capitalize text-center" style="vertical-align:middle; padding: 20px;"><h1 style="margin-top: 0px; margin-bottom: 0px;">'
								+ day.date() + '</h1><span class="text-danger">' + day.locale("pt-br").format("MMMM") + '</span></td>';

						var td2 = '';
						td2 += '<td style="vertical-align:middle; margin-top: 10px; padding: 20px;">';
						td2 += '<div class="col-md-8">';
						td2 += '<h3 style="margin-top: 0px; margin-bottom: 0px;">';
						td2 += value.name;
						td2 += '</h3>' + (value.city ? value.city : '');
						td2 += '</div>';

						td2 += '<div class="col-md-4">';
						td2 += '<div style="width: 100px;" class="pull-right text-center">';
						if (value.registration.open) {
							td2 += "<button type='button' class='registration btn btn-success' data-race='" + value.id + "'>";
							td2 += "<span class='glyphicon glyphicon-pencil' aria-hidden='true' style='font-size: 0.8em;'></span> ";
							td2 += "Inscrições";
							td2 += "</button><br>";
						}
						td2 += '<a href="' + App.getContextPath() + '/race/' + value.id + '">ver detalhes</a>';
						td2 += '</div>';
						td2 += '</div>';

						td2 += '</td>';

						$('#resultTable > tbody:last').append('<tr>' + td1 + td2 + '</tr>');
					});
}
