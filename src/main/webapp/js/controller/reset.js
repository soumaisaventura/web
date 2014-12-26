$(function() {
	$("#email").focus();
	
	$("form").submit(function(event) {
		event.preventDefault();
		$("[id$='-message']").hide();

		var data = {
			'email' : $("#email").val()
		};

		ResetProxy.requestPasswordReset(data).done(resetOk);
	});
});

// Password Reset process

function resetOk(data) {
	$("#global-message").html("Acesse seu e-mail e siga as instruções para redefinir sua senha.").show();
}
