// Form events binding

$(function() {
	$(".input-group.date").datepicker({
		todayHighlight : true,
		language : "pt-BR",
		startView : 2
	});

	$("#salvar").click(function() {
		$("[id$='-message']").hide();

		var form = {
			'name' : $("#name").val(),
			'password' : $("#password").val(),
			'email' : $("#email").val(),
			'birthday' : $("#birthday").val(),
			'gender' : $("#gender").val()
		};

		new SignUpProxy().signup(form, signupOk, signupFailed);
	});
});

// SignUp process

function signupOk(data) {
	$('#myModal').modal('toggle').on('hidden.bs.modal', function() {
		location.href = './';
	});
}

function signupFailed(request) {
	switch (request.status) {
		case 422:
			App.handleValidation(request);
			break;

		default:
			break;
	}

	// $.each(request.responseJSON.reverse(), function(index, value) {
	// $("#" + value.property + "-message").html(value.message).show();
	// });
}
