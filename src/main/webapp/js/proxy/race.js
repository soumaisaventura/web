var RaceProxy = {

	url : App.getContextPath() + "/api/race",

	findNext : function() {
		return $.ajax({
			type : "GET",
			url : this.url + "/next",
			contentType : "application/json"
		});
	},

	findCategories : function($race) {
		return $.ajax({
			type : "GET",
			url : this.url + "/" + $race + "/categories",
			contentType : "application/json"
		});
	},

	searchAvailableUsers : function($race, $filter, $excludes) {
		return $.ajax({
			url : this.url + "/" + $race + "/users",
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
