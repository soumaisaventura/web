// Form events binding

$(function() {
	$("#message").hide();

	$("#reset").click(function() {
		$("[id$='-message']").html("");
		
		var form = {
			'email' : $("#email").val()
		};

		var proxy = new AuthProxy();
		proxy.requestPasswordReset(form, resetOk, resetFailed);
	});
});

// Password Reset process

function resetOk(data) {
	$("[id|='erro']").html("");
	$("#message > div").html("Acesse seu e-mail e siga as instruções para redefinir sua senha.");
	$("#message").show();
}

function resetFailed(request) {
	$("#message").hide();

	switch (request.status) {
	case 412:
		$.each(request.responseJSON, function(index, value) {
			$("#" + value.property + "-message").html(value.message);
		});
	}
}
