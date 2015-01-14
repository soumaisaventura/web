var PasswordProxy = {

	url : App.getContextPath() + "/api/password",

	requestPasswordReset : function($data) {
		return $.ajax({
			type : "POST",
			url : this.url + "/recovery",
			data : JSON.stringify($data),
			contentType : "application/json"
		});
	},

	performPasswordReset : function($data, $token) {
		return $.ajax({
			type : "POST",
			url : this.url + "/reset/" + $token,
			data : JSON.stringify($data),
			contentType : "application/json"
		});
	}
};
