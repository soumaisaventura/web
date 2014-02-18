var GoogleAuthProxy = function GoogleAuthProxy() {
	this.url = "api/auth/google";
};

GoogleAuthProxy.prototype.login = function(code, $success, $error) {
	$.ajax({
		type : "POST",
		url : this.url,
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
