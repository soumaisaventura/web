var RegisterProxy = {

	url : App.getContextPath() + "/api/race",

	validate : function($data, $race) {
		return $.ajax({
			type : "POST",
			url : this.url + "/" + $race + "/register/validate",
			data : JSON.stringify($data),
			contentType : "application/json",
			beforeSend : function(request) {
				App.setHeader(request)
			}
		});
	},

	submit : function($data, $race) {
		return $.ajax({
			type : "POST",
			url : this.url + "/" + $race + "/register",
			data : JSON.stringify($data),
			contentType : "application/json",
			beforeSend : function(request) {
				App.setHeader(request)
			}
		});
	}
};
