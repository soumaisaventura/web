var AuthProxy = function AuthProxy() {
	this.url = "api/auth";
};

AuthProxy.prototype.login = function($credentials, $success, $error) {
	$.ajax({
		type : "POST",
		url : this.url,
		contentType : "application/json",
		data : JSON.stringify($credentials),
		success : function(data) {
			if ($success) {
				$success(data);
			}
		},
		error : function(request) {
			if ($error) {
				$error(request);
			}
		}
	});
};

AuthProxy.prototype.requestPasswordReset = function($form, $success, $error) {
	$.ajax({
		type : "POST",
		url : this.url + "/reset",
		contentType : "application/json",
		data : JSON.stringify($form),
		success : function(data) {
			if ($success) {
				$success(data);
			}
		},
		error : function(request) {
			if ($error) {
				$error(request);
			}
		}
	});
};

AuthProxy.prototype.logout = function($success, $error) {
	$.ajax({
		type : "DELETE",
		url : this.url,
		success : function(data) {
			if ($success) {
				$success(data);
			}
		},
		error : function(request) {
			if ($error) {
				$error(request);
			}
		}
	});
};

AuthProxy.prototype.facebookLogin = function(code, $success, $error) {
	$.ajax({
		type : "POST",
		url : this.url + "/facebook",
		data : "code=" + code,
		processData : false,
		success : function(data) {
			if ($success) {
				$success(data);
			}
		},
		error : function(request) {
			if ($error) {
				$error(request);
			}
		}
	});
};

AuthProxy.prototype.googleLogin = function(code, $success, $error) {
	$.ajax({
		type : "POST",
		url : this.url + "/google",
		data : "code=" + code,
		processData : false,
		success : function(data) {
			if ($success) {
				$success(data);
			}
		},
		error : function(request) {
			if ($error) {
				$error(request);
			}
		}
	});
};
