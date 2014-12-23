// Form events binding

$(function() {
	$("#name").focus();

	$(".input-group.date").datepicker({
		todayHighlight : true,
		language : "pt-BR",
		startView : 2
	});

	$("form").submit(function(event) {
		event.preventDefault();
		$("[id$='-message']").hide();

		var data = {
			'name' : $("#name").val(),
			'password' : $("#password").val(),
			'email' : $("#email").val(),
			'birthday' : $("#birthday").val(),
			'gender' : $("#gender").val()
		};

		SignUpProxy.signup(data).done(signupOk); //.fail(signupFailed);
	});
});

// SignUp process

function signupOk(data, status, request) {
	App.setToken(request.getResponseHeader('Set-Token'));

	$('#myModal').modal('toggle').on('hidden.bs.modal', function() {
		location.href = './';
	});
}

//function signupFailed(request) {
//	switch (request.status) {
//		case 422:
//			App.handleValidation(request);
//			break;
//
//		default:
//			break;
//	}
//
//	// $.each(request.responseJSON.reverse(), function(index, value) {
//	// $("#" + value.property + "-message").html(value.message).show();
//	// });
//}
