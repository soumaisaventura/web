var UserHealthProxy = {

	url : App.getContextPath() + "/api/user/health",

	load : function() {
		return $.ajax({
			type : "GET",
			url : this.url,
			beforeSend : function(request) {
				App.setHeader(request)
			}
		});
	},

	update : function($data) {
		return $.ajax({
			type : "PUT",
			url : this.url,
			data : JSON.stringify($data),
			contentType : "application/json",
			beforeSend : function(request) {
				App.setHeader(request)
			}
		});
	}
};
