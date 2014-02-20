var ResetProxy = function ResetProxy() {
    this.url = "api/reset";
};

ResetProxy.prototype.requestPasswordReset = function($form, $success, $error) {
    $.ajax({
	type : "POST",
	url : this.url,
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

ResetProxy.prototype.performPasswordReset = function($form, $token, $success,
	$error) {
    $.ajax({
	type : "POST",
	url : this.url + "/" + $token,
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
