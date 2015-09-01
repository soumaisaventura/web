$(function() {
	moment.locale("pt-br");
	numeral.language('pt-br');
	numeral.defaultFormat('0.00');

	/**
	 * Carrega dados da inscrição
	 */
	RegistrationProxy.load($("#registration").val()).done(loadOk);
});

/* ---------------- Funções de Callback ---------------- */

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

	var organizer = false;
	$.each(data.race.organizers, function(i, value) {
		if (user.id === value.id) {
			organizer = true;
			return;
		}
	});

	if (organizer || user.admin) {
		$(".breadcrumb.organizer").show();

		$("#race-link").attr("href", App.getContextPath() + "/prova/" + data.race.id);
		$("#dashboard-link").attr("href", App.getContextPath() + "/prova/" + data.race.id + "/painel");
		$("#registration-link").attr("href", App.getContextPath() + "/prova/" + data.race.id + "/painel/inscricoes");

	} else {
		$("#registration-list-menu-item").addClass("active");
		$(".breadcrumb.athlete").show();
	}
}

function loadOk(data) {
	updateBreadcrumb(data);
	var template = $('#template').html();
	
	$("#registration-id").text(data.number);
	$("#team-name").text(data.teamName);
	$("#race-status").html(App.translateStatus(data.status));
	//$("#summary-section").show();

	$("#race-name").text(data.race.name);
	$("#race-date").text(moment(data.race.date).format('LL'));
	$("#race-city").text(data.race.city.name + "/" + data.race.city.state);
	//$("#race-section").show();

	$("#race-category").text(data.category.name + " " + data.course.name);

	var user = App.getLoggedInUser();
	var member = false;

	$.each(data.teamFormation, function(i, value) {
		value.racePrice = numeral(value.bill.racePrice).format();
		$('#atletas').append(Mustache.render(template, value));
		if (user && value.id == user.id) {
			member = true;
		}
	});

	updateTotal();
	$("#team-section").show();

	if (data.status == 'pendent') {
		$(".payment-type").hide();
		$(".payment-type-" + data.payment.type).show();
		$('#registration-payment-info').html(data.payment.info ? App.replaceEmailByMailTo(data.payment.info.replace(/\n|\r/g, '<br>')) : '');

		updatePaymentButton(data.payment.code, data.payment.transaction);

		$('#payment-section').show();
	}

	var authorized = user ? user.admin : null;
	$.each(data.race.organizers, function(i, organizer) {
		$('#race-organizers').append(Mustache.render(template, organizer));
		if (user && organizer.id == user.id) {
			authorized = true;
		}
	});
	$("#organizers-section").show();

	$("#registration-submitter").text(data.submitter.name);
	$("#registration-date").text(moment(data.date).format('L'));
	//$("#footer-section").show();

	loadEditableCurrency(data.id, authorized && data.status == 'pendent');
	loadEditableTeamName(data.id, data.race.status == 'open' && (authorized || member));
}

function loadEditableCurrency(registrationId, enabled) {
	$(".editable").editable({
		type : 'text',
		disabled : !enabled,
		emptyclass : '',
		display : function(value, sourceData) {
			$(this).text(numeral(value).format())
		},
		url : function(params) {
			var d = new $.Deferred;
			var value = JSON.stringify(numeral().unformat(params.value));

			var updateValuesOk = function(data) {
				d.resolve();
				updateTotal();
				updatePaymentButton();
			};

			var updateValuesFailed = function(request) {
				var message = null;
				if (request.status == 422) {
					message = request.responseJSON[0].message;
				}

				d.reject(message);
			};

			if (params.property === 'racePrice') {
				RegistrationProxy.updateRacePrice(registrationId, params.pk, value).done(updateValuesOk).fail(updateValuesFailed);
			}

			return d.promise();
		}
	});

	$('.editable').on('shown', function() {
		var input = $(this).data('editable').input.$input;

		input.mask('##0,00', {
			selectOnFocus : true,
			placeholder : "0",
			reverse : true
		});
	});
}

function loadEditableTeamName(registrationId, enabled) {
	$('#team-name').editable({
		type : 'text',
		disabled : !enabled,
		emptyclass : '',
		url : function(params) {
			var d = new $.Deferred;

			var updateValuesOk = function(data) {
				d.resolve();
			};

			var updateValuesFailed = function(request) {
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
	$(".editable").each(function() {
		var key = $(this).data("pk");
		var value = numeral().unformat($(this).text());
		partials[key] = (partials[key] ? partials[key] : 0.0) + value;
	});

	var total = 0;
	$(".partial").each(function() {
		var key = $(this).data("pk");
		var value = partials[key];
		$(this).text(numeral(value).format());
		total += value;
	});

	$("#payment-ammount").data("value", total);
	$("#payment-ammount").text("R$ " + numeral(total).format());
}

function updatePaymentButton(code, transaction) {
	if ($("#payment-ammount").data("value") == 0 || $("#race-status").data('status') != 'pendent') {
		$("#payment-section").hide();
	} else {
		$("#payment-section").show();
	}

	$("#payment").unbind('click');
	if (transaction) {
		$("#payment-alert").show();
		$("#payment").attr("disabled", true);

	} else if (code) {
		$("#payment-alert").hide();
		$("#payment").attr("disabled", false);

		$("#payment").click(function() {
			openPaymentFlow(code);
		});
	} else {
		$("#payment-alert").hide();
		$("#payment").attr("disabled", false);

		$("#payment").click(function() {
			RegistrationProxy.sendPayment($("#registration").val()).done(sendPaymentOk).fail(sendPaymentFailed);
		});
	}
}

function openPaymentFlow(code) {
	// window.open('https://pagseguro.uol.com.br/v2/checkout/payment.html?code='
	// + code);
	location.href = 'https://pagseguro.uol.com.br/v2/checkout/payment.html?code=' + code;
}
