var RegisterClient = function() {
	this.url = "api/register";
	return this;
};

RegisterClient.prototype.register = function($user, $success, $error) {
	return $.ajax({
		type : "POST",
		url : this.url,
		contentType : "application/json;charset=utf8",
		data : JSON.stringify($user),
		dataType : "json",
		success : function(response) {
			if ($success) {
				$success(response);
			}
		},
		error : function(response) {
			if ($error) {
				$error(response);
			}
		}
	});
};

RegisterClient.prototype.unregister = function($success, $error) {
	return $.ajax({
		type : "DELETE",
		url : this.url,
		dataType : "json",
		success : function(response) {
			if ($success) {
				$success(response);
			}
		},
		error : function(response) {
			if ($error) {
				$error(response);
			}
		}
	});
};