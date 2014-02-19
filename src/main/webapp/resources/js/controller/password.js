// Form events binding

$(function() {
	$("#message").hide();

	$("#update").click(function() {
		$("[id$='-message']").html("");

		var form = {
			'email' : $("#email").val(),
			'newPassword' : $("#newPassword").val()
		};

		var proxy = new AuthProxy();
		var token = $.url().param('token');

		proxy.performPasswordReset(form, token);
	});
});

// Password Reset process

