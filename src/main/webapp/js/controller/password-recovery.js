$(function() {
	$("#email").focus();
	
	$("form").submit(function(event) {
		event.preventDefault();
		$("[id$='-message']").hide();

		var data = {
			'email' : $("#email").val()
		};

		PasswordProxy.recovery(data).done(resetOk);
	});
});

// Password Reset process

function resetOk(data) {
	$("#global-message").addClass("alert-success").text("Acesse seu e-mail e siga as instruções para redefinir sua senha.").show();
}
