$(function() {
	moment.locale("pt-br");
	numeral.language('pt-br');
	numeral.defaultFormat('$ 0,0');
	
	var $race = $("#race").val();
	
    $('#resultTable').footable().bind('footable_filtering', function (e) {
    	var selected = $('.filter-status').find(':selected').text();
    	if (selected && selected.length > 0) {
    		e.filter += (e.filter && e.filter.length > 0) ? ' ' + selected : selected;
    		e.clear = !e.filter;
    	}
    });
    
    $('.filter-status').change(function (e) {
    	e.preventDefault();
    	$('#resultTable').trigger('footable_filter', {filter: $('#filter').val()});
    });
    
    $('table').on('click','.approve', function(){
    	RegistrationProxy.confirm($(this).data("registration")).done(confirmOk);
    });
	
	RaceProxy.loadSummary($race).done(loadOk);
	RaceRegistrationProxy.find($race).done(findOk);
	
});

function confirmOk(){
	console.log('confirmOk');
	$(".registration").removeClass("label-warning").addClass("label-success").text("Confirmado");
	$(".approve").remove();
	$('#resultTable').trigger('footable_filter', {filter: $('#filter').val()});
}

function loadOk($data) {
	$("#race-name").text($data.name)
	$("#race-date").text(moment($data.date, "YYYY-MM-DD").locale("pt-br").format('LL'));
	$("#race-city").text($data.city);
}

function findOk($data, $status, $request) {
	
	switch ($request.status) {
		case 204:
			var message = "Nenhuma equipe se inscreveu ainda."
			$('#resultTable > tbody:last').append('<tr><td colspan="6">' + message + '</td></tr>');

			break;

		case 200:
			$.each($data, function($i, $registration){
				var amount = 0;
				var tr = "";
				tr = tr.concat("<tr>");
				tr = tr.concat("<td class='text-left' style='vertical-align: top'>");
				tr = tr.concat("<h4 style='margin: 5px'><a href='" + App.getContextPath() + "/registration/" + $registration.number + "'>#" + $registration.number + "</a></h4>");
				tr = tr.concat("<h4><span class='registration label label-warning'>" + App.translateStatus($registration.status) + "</span></h4>");
				tr = tr.concat("</td>");
				tr = tr.concat("<td style='vertical-align: top'>");
				tr = tr.concat("<h4 style='margin: 5px'>" + $registration.teamName + "</h4>");
				tr = tr.concat("<h5 style='margin: 5px'>" + $registration.category.name + ' ' + $registration.course.length + "</h5>");
				tr = tr.concat("<h6 style='margin: 5px'>");
				
				$registration.teamFormation.forEach(function($member, $i){
					amount += $member.bill.amount;
					if ($i === 0){
						tr = tr.concat($member.name);
					} else {
						tr = tr.concat(" / " + $member.name);
					}
				});
				
				tr = tr.concat("</h6>");
				tr = tr.concat("<h6 style='margin: 5px'>Data inscrição: <strong>" + moment($registration.date).format('L') + "</strong></h6>");
				tr = tr.concat("</td>");
				tr = tr.concat("<td class='text-center' style='vertical-align: top' nowrap='nowrap'>");
				tr = tr.concat("<h4 style='margin: 5px'>" + numeral(amount).format() + "</h4>");
				if ($registration.status === "pendent"){
					tr = tr.concat("<button type='button' class='approve btn btn-success btn-sm' data-registration='" + $registration.number + "'>Aprovar</button>");
				}
				tr = tr.concat("</td>");
				tr = tr.concat("</tr>");
				$('#resultTable > tbody:last').append(tr);
			});
			break;
	}
}
