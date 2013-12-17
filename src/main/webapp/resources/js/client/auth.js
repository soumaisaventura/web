var AuthClient = function AuthClient() {
	this.url = "api/auth";
};

AuthClient.prototype.login = function(username, password) {
	$.ajax({
		type : "POST",
		url : this.url,
		data : {
			"username" : username,
			"password" : password
		},
		success : function() {
			window.location = "index.jsf";
		},
		error : function() {
			// $("#msg").text("Login e/ou Senha inválidos.");
			alert("Erro de login ... ");
		}
	});

};

AuthClient.prototype.logout = function() {
	$.ajax({
		type : "DELETE",
		url : this.url,
		success : function() {
			window.location = "index.jsf";
		}
	});
};
