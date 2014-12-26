var ResetProxy = {

	url : "api/reset",

	requestPasswordReset : function($data) {
		return $.ajax({
			type : "POST",
			url : this.url,
			data : JSON.stringify($data),
			contentType : "application/json"
		});
	},

	performPasswordReset : function($data, $token) {
		return $.ajax({
			type : "POST",
			url : this.url + "/" + $token,
			data : JSON.stringify($data),
			contentType : "application/json"
		});
	}
};
