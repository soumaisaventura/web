$(function() {

	App.loadDateCombos($("#birthday"), $("#birthday-month"), $("#birthday-year"))

	$("#name").focus();

	$("form").submit(function(event) {
		event.preventDefault();
		$("[id$='-message']").hide();

		var data = {
			'name' : $("#name").val(),
			'password' : $("#password").val(),
			'email' : $("#email").val(),
			'birthday' : $("#birthday-year").val() + "-" + $("#birthday-month").val() + "-" + $("#birthday").val(),
			'gender' : $("#gender").val()
		};
		SignUpProxy.signup(data).done(signupOk);
	});
});

// SignUp process
function signupOk(data, status, request) {
	$("#global-message").addClass('alert-success').text("Cadastro efetuado. Siga as instruções no seu e-mail para ativar a sua conta.").show();
}
