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
		PasswordProxy.reset(data, token).done(App.loginOk).fail(resetFail);
	});
});

function resetFail(request) {
	switch (request.status) {
		case 422:
			var message = App.getGlobalMessage(request);

			if (message) {
				swal({
					title : "",
					text : message,
					confirmButtonClass : "btn-danger",
					type : "error"
				});
			} else {
				App.handle422(request);
			}
			break;
	}
}
