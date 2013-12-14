function Usuario() {
	this.id;
	this.nome;
	this.email;
	this.nascimento;
}

Usuario.prototype._login = function(username, password) {

	$.ajax({
		type : "POST",
		url : "api/login",
		data : {
			"username" : username,
			"password" : password
		},
		success : function() {
			window.location = "home.jsf";
		},
		error : function() {
			// $("#msg").text("Login e/ou Senha inválidos.");
			alert("Erro de login ... ");
		}
	});

};

Usuario.prototype._logoff = function() {

	$.ajax({
		type : "DELETE",
		url : "api/login",
		success : function() {
			window.location = "index.jsf";
		}
	});

};