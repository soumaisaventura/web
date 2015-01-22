var UserProxy = {

	url : App.getContextPath() + "/api/user",

	getLoggedInUser : function() {
		return $.ajax({
			url : this.url,
			type : "GET",
			error : function() {},
			beforeSend : function(request) {
				App.setHeader(request);
			}
		});
	},

	search : function($filter, $excludes) {
		console.log($excludes);
		return $.ajax({
			url : this.url + "/search",
			dataType : "json",
			data : {
				q : $filter,
				excludes : $excludes
			},
			beforeSend : function(request) {
				App.setHeader(request);
			}
		});
	}
};
