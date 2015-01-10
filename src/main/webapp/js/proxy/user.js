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
		return $.ajax({
			url : this.url + "/search",
			dataType : "json",
			data : {
				q : $filter,
				// verificar pq não tá funcionando com excludes vazio
				excludes : $excludes.length > 0 ? $excludes : $excludes.push(0)
			},
			beforeSend : function(request) {
				App.setHeader(request);
			}
		});
	}
};
