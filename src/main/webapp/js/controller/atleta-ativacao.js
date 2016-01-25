$(function() {
	$("#email").focus();

	$("form").submit(function(event) {
		event.preventDefault();
		$("[id$='-message']").hide();

		var data = {
			'email' : $("#email").val()
		};

		var token = App.getUrlParameterByName('token');
		UserProxy.activate(data, token).done(App.loginOk).fail(activateFailed);
	});
});

function activateFailed(request) {
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
