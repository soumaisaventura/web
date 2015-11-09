$(function() {
	moment.locale("pt-br");
	numeral.language('pt-br');
	numeral.defaultFormat('$ 0,0');

	var race = $("#race").val();

	$("form").submit(function(event) {
		event.preventDefault();
	});

	$('#resultTable').footable().bind('footable_filtering', function(e) {
		var selected = $('#filter-status').find(':selected').attr('value');
		if (selected && selected.length > 0) {
			e.filter += (e.filter && e.filter.length > 0) ? ' ' + selected : selected;
			e.clear = !e.filter;
		}
	});

	$('#resultTable').on('footable_filter', function(e) {
		updateTotal($('.footable tbody tr:not(.footable-filtered)').length);
	});

	$('#filter-text').keyup(function(event) {
		applyFilter();
	});

	$('#filter-status').change(function(event) {
		applyFilter();
	});

	$('table').on(
			'click',
			'.approve',
			function() {
				var button = $(this);
				bootbox.confirm("Confirma que a equipe <strong>" + button.data("team-name") + "</strong> efetuou o pagamento da inscrição <strong>#"
						+ button.data("registration") + "</strong> no valor de <strong>" + button.data("ammount") + "</strong> ?", function(result) {
					if (result) {
						button.addClass("confirmed");
						RegistrationProxy.confirm(button.data("registration")).done(confirmOk);
						confirmOk();
					}
				});
			});

	$('table').on(
			'click',
			'.cancel',
			function() {
				var button = $(this);
				bootbox.confirm("Tem certeza que você quer cancelar a inscrição <strong>#" + button.data("registration")
						+ "</strong> da equipe <strong>" + button.data("team-name") + "</strong> no valor de <strong>" + button.data("ammount")
						+ "</strong> ?", function(result) {
					if (result) {
						button.addClass("cancelled");
						RegistrationProxy.cancel(button.data("registration")).done(cancelOk);
						confirmOk();
					}
				});
			});

	RaceProxy.loadSummary(race).done(loadOk);
	RaceRegistrationProxy.find(race).done(findOk);

});

function confirmOk() {
	var registration = $(".confirmed").data("registration");
	$("#registration-status-" + registration).html(App.translateStatus('confirmed'));
	$(".confirmed").remove();
	applyFilter();
}

function cancelOk() {
	var registration = $(".cancelled").data("registration");
	$("#registration-status-" + registration).html(App.translateStatus('cancelled'));
	$(".cancelled").prev(".approve").remove();
	$(".cancelled").remove();
	applyFilter();
}

function applyFilter() {
	$('#resultTable').trigger('footable_filter', {
		filter : $('#filter-text').val()
	});
}

function loadOk(data) {
	$(".race-name").text(data.name)
	$("#race-date").text(moment(data.date, "YYYY-MM-DD").locale("pt-br").format('LL'));
	$("#race-city").text(data.city);
}

function findOk(data, status, request) {
	switch (request.status) {
		case 204:
			var message = "Nenhuma equipe se inscreveu ainda."
			$('#resultTable > tbody:last').append('<tr><td colspan="6">' + message + '</td></tr>');

			break;

		case 200:
			$.each(data, function(i, registration) {
				var amount = 0;
				var tr = "";
				tr = tr.concat("<tr>");
				tr = tr.concat("<td class='text-left' style='vertical-align: top'>");
				tr = tr.concat("<h4 style='margin: 5px'><a href='" + App.getContextPath() + "/inscricao/" + registration.number + "'>#"
						+ registration.number + "</a></h4>");
				tr = tr.concat("<span id='registration-status-" + registration.number + "'>" + App.translateStatus(registration.status)) + "</span>";
				tr = tr.concat("</td>");
				tr = tr.concat("<td style='vertical-align: top'>");
				tr = tr.concat("<h4 style='margin: 5px'>" + registration.teamName + "</h4>");
				tr = tr.concat("<h5 style='margin: 5px'>" + registration.category.name + ' ' + registration.course.name + "</h5>");
				tr = tr.concat("<h6 style='margin: 5px'>");

				registration.teamFormation.forEach(function(member, i) {
					amount += member.bill.racePrice;
					if (i === 0) {
						tr = tr.concat(member.name);
					} else {
						tr = tr.concat(" / " + member.name);
					}

					tr = tr.concat("<span class='email' hidden='true'>" + member.email + "</span>");
				});

				tr = tr.concat("</h6>");
				tr = tr.concat("<h6 style='margin: 5px'>Data inscrição: <strong>" + moment(registration.date).format('L') + "</strong></h6>");
				tr = tr.concat("</td>");
				tr = tr.concat("<td class='text-center' style='vertical-align: top' nowrap='nowrap'>");
				tr = tr.concat("<h4 style='margin: 5px'>" + numeral(amount).format() + "</h4>");
				if (registration.status === "pendent") {
					tr = tr.concat("<button type='button' class='approve btn btn-xs btn-success' data-registration='" + registration.number
							+ "' data-team-name='" + registration.teamName + "' data-ammount='" + numeral(amount).format()
							+ "' title='Confirmar inscrição'><span class='glyphicon glyphicon-ok' /></button>");
					// }
					// if (registration.status === "pendent" &&
					// registration.status !== "cancelled") {
					tr = tr.concat(" ");
					// }
					// if (registration.status !== "cancelled") {
					// tr = tr.concat("<button type='button' class='cancel btn
					// btn-xs btn-danger' data-registration='" +
					// registration.number
					// + "' data-team-name='" + registration.teamName + "'
					// data-ammount='" + numeral(amount).format()
					// + "' title='Cancelar inscrição'><span class='glyphicon
					// glyphicon-remove' /></button>");
				}

				if (registration.status == "pendent" || (App.isAdmin() && registration.status == "confirmed")) {
					tr = tr.concat("<button type='button' class='cancel btn btn-xs btn-danger' data-registration='" + registration.number
							+ "' data-team-name='" + registration.teamName + "' data-ammount='" + numeral(amount).format()
							+ "' title='Cancelar inscrição'><span class='glyphicon glyphicon-remove' /></button>");
				}

				tr = tr.concat("</td>");
				tr = tr.concat("</tr>");
				$('#resultTable > tbody:last').append(tr);
			});

			updateTotal(data.length);
			break;
	}
}

function updateTotal(length) {
	var text;
	switch (length) {
		case 0:
			text = 'nenhum registro encontrado';
			$("#email-all").hide();
			break;

		case 1:
			text = length + ' registro encontrado';
			$("#email-all").show();
			break;

		default:
			text = length + ' registros encontrados';
			$("#email-all").show();
	}

	var email = "?bcc=";
	$(".footable tbody tr:not(.footable-filtered) .email").each(function(i, value) {
		email += $(value).text() + ",";
	});

	$("#email-all").attr("href", "mailto:" + email);
	$("#total").text(text);
}
