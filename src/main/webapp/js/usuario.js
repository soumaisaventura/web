function Usuario() {
	this.id;
	this.nome;
	this.email;
	this.nascimento;
}

User.prototype._login = function(username, password) {

	$.ajax({
		type : "POST",
		url : "api/login",
		data : {
			"username" : username,
			"password" : password
		},
		success : function() {
			window.location = "home.html";
		},
		error : function() {
			// $("#msg").text("Login e/ou Senha inválidos.");
			alert("Erro de login ... ");
		}
	});

};

User.prototype._logoff = function() {

	$.ajax({
		type : "DELETE",
		url : "api/auth",
		success : function() {
			window.location = "login.html";
		}
	});

};