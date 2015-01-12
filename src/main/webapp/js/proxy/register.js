var RegisterProxy = {

	url : App.getContextPath() + "/api/race",

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
