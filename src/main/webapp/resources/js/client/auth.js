var AuthClient = function AuthClient() {
	this.url = "api/auth";
};

AuthClient.prototype.login = function($credentials, $success, $error) {
	$.ajax({
		type : "POST",
		url : this.url,
		contentType : "application/json;charset=utf8",
		data : JSON.stringify($credentials),
		success : function(data) {
			if ($success) {
				$success(data);
			}
		},
		error : function(data) {
			if ($error) {
				$error(data);
			}
		}		
	});
};

// TODO Será que precisa dos parâmetros? Se sim, ajustar o successe e erro para ficar genérico similar ao login.
AuthClient.prototype.logout = function($success, $error) {
	$.ajax({
		type : "DELETE",
		url : this.url,
		success : function() {
			window.location = "index.jsf";
		}
	});
};
