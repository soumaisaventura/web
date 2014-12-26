$(function() {
	$("#name").focus();

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

		SignUpProxy.signup(data).done(signupOk);
	});
});

// SignUp process

function signupOk(data, status, request) {
	App.setToken(request.getResponseHeader('Set-Token'));
	location.href = './';
}
