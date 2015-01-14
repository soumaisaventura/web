$(function() {
	$("#email").focus();

	$("form").submit(function(event) {
		event.preventDefault();
		$("[id$='-message']").hide();

		var data = {
			'email' : $("#email").val(),
			'newPassword' : $("#newPassword").val()
		};

		var token = App.getUrlParameterByName('token');
		PasswordProxy.reset(data, token).done(resetOk);
	});
});

// Password Reset process

function resetOk(data, status, request) {
	App.setToken(request.getResponseHeader('Set-Token'));
	App.restoreLocation();
}
