$(function() {
	
	App.loadDateCombos($("#birthday"), $("#birthday-month"), $("#birthday-year"))
	
	$("#name").focus();

	$("form").submit(function(event) {
		event.preventDefault();
		$("[id$='-message']").hide();
		
		var birthday = "";
		var year = $("#birthday-year").val();
		var month = $("#birthday-month").val();
		var day = $("#birthday").val();
		
		if (!isNaN(year) && !isNaN(month) && !isNaN(day)){
			console.log('data válida');
			birthday = new Date(year, month, day);
		}

		var data = {
			'name' : $("#name").val(),
			'password' : $("#password").val(),
			'email' : $("#email").val(),
			'birthday' : birthday,
			'gender' : $("#gender").val()
		};
		SignUpProxy.signup(data).done(signupOk);
	});
});

// SignUp process
function signupOk(data, status, request) {
	$("#global-message").addClass('alert-success')
						.text("Cadastro efetuado. Siga as instruções no seu e-mail para ativar a sua conta.")
						.show();
}
