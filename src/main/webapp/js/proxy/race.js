var RaceProxy = {

	url : App.getContextPath() + "/api/race",

	load : function($race) {
		return $.ajax({
			type : "GET",
			url : this.url + "/" + $race
		});
	},

	findNext : function() {
		return $.ajax({
			type : "GET",
			url : this.url + "/next"
		});
	},

	findCategories : function($race) {
		return $.ajax({
			type : "GET",
			url : this.url + "/" + $race + "/categories"
		});
	},

	getBanner : function($race) {
		return $.ajax({
			type : "GET",
			url : this.url + "/" + $race + "/banner"
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
