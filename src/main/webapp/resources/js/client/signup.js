var SignUpClient = function () {
	this.url = "api/signup";
	return this;
};

SignUpClient.prototype.signup = function($form, $success, $error) {
	return $.ajax({
		type : "POST",
		url : this.url,
		contentType : "application/json;charset=utf8",
		data : JSON.stringify($form),
		dataType : "json",
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

SignUpClient.prototype.unregister = function($success, $error) {
	return $.ajax({
		type : "DELETE",
		url : this.url,
		dataType : "json",
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