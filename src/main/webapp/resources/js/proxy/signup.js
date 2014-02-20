var SignUpProxy = function() {
    this.url = "api/signup";
};

SignUpProxy.prototype.signup = function($form, $success, $error) {
    return $.ajax({
	type : "POST",
	url : this.url,
	data : JSON.stringify($form),
	contentType : "application/json",
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

SignUpProxy.prototype.unregister = function($success, $error) {
    return $.ajax({
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