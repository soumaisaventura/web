$(function() {
	$("#email").focus();
	
	$("form").submit(function(event) {
		event.preventDefault();
		$("[id$='-message']").parent().removeClass("has-error");
		$("[id$='-message']").hide();

		var data = {
			'email' : $("#email").val()
		};

		PasswordProxy.recovery(data).done(resetOk);
	});
});

// Password Reset process

function resetOk(data) {
	swal("", "Acesse seu e-mail e siga as instruções para redefinir sua senha.", "success");
}
