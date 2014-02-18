// Form events binding

$(function() {
	$("#message").hide();

	$("#reset").click(function() {
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
	$("#message > div").html("A senha foi enviada para o e-mail solicitado.");
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
