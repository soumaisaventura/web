$(function() {
	
	$("#name").focus();

	App.loadDateCombos($("#birthday"), $("#birthday-month"), $("#birthday-year"))

	$("form").submit(function(event) {
		event.preventDefault();
		$("[id$='-message']").hide();

		var birthday = "";
		if ($("#birthday-year").val() && $("#birthday-month").val() && $("#birthday").val()) {
			birthday = $("#birthday-year").val() + "-" + $("#birthday-month").val() + "-" + $("#birthday").val();
		}
		console.log($("#password").val());
		var data = {
			'name' : $("#name").val(),
			'password' : $("#secretkey").val(),
			'email' : $("#email").val(),
			'birthday' : birthday,
			'gender' : $("#gender").val()
		};
		SignUpProxy.signup(data).done(signupOk);
	});
});

// SignUp process
function signupOk(data, status, request) {
	$("#global-message")
		.removeClass('alert-danger')
		.addClass('alert-success')
		.text("Cadastro efetuado. Siga as instruções no seu e-mail para ativar a sua conta.")
		.show();
}
