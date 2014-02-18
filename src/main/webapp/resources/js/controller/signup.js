// Form events binding

$(function() {
	$(".input-group.date").datepicker({
		todayHighlight : true,
		language : "pt-BR",
		startView : 2
	});

	$("#salvar").click(function() {
		$("[id$='message']").html("");

		var form = {
			'fullName' : $("#fullName").val(),
			'password' : $("#password").val(),
			'email' : $("#email").val(),
			'birthday' : $("#birthday").val(),
			'gender' : $("#gender").val()
		};

		var proxy = new SignUpProxy("api");
		proxy.signup(form, signupOk, signupFailed);
	});
});

// SignUp process

function signupOk(data) {
	$('#myModal').modal('toggle').on('hidden.bs.modal', function() {
		window.location = 'index.html';
	});
}

function signupFailed(request) {
	$.each(request.responseJSON.reverse(), function(index, value) {
		$("#" + value.property + "-message").html(value.message);
	});
}