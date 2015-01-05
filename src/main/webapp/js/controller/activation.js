$(function() {
	$("#email").focus();

	$("form").submit(function(event) {
		event.preventDefault();
		$("[id$='-message']").hide();

		var data = {
			'email' : $("#email").val()
		};

		var token = App.getUrlParameterByName('token');
		SignUpProxy.activate(data, token).done(activateOk);
	});
});

// Activation process

function activateOk() {
	$("#global-message").html("Conta ativada com sucesso.").show();
}
