var UserProxy = {

	url : App.getContextPath() + "/api/user",

	getLoggedInUser : function() {
		return $.ajax({
			url : this.url,
			type : "GET",
			error : function() {},
			beforeSend : function(request) {
				App.setHeader(request)
			}
		});
	}
};
