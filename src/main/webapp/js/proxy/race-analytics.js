var RaceAnalyticsProxy = {

	url : App.getContextPath() + "/api/race",

	getByCategories : function($race) {
		return $.ajax({
			type : "GET",
			url : this.url + "/" + $race + "/analytics/category",
			beforeSend : function(request) {
				App.setHeader(request)
			}
		});
	},

	getByCourses : function($race) {
		return $.ajax({
			type : "GET",
			url : this.url + "/" + $race + "/analytics/course",
			beforeSend : function(request) {
				App.setHeader(request)
			}
		});
	},

	getByStatus : function($race) {
		return $.ajax({
			type : "GET",
			url : this.url + "/" + $race + "/analytics/status",
			beforeSend : function(request) {
				App.setHeader(request)
			}
		});
	},

	getByLocation : function($race) {
		return $.ajax({
			type : "GET",
			url : this.url + "/" + $race + "/analytics/location",
			beforeSend : function(request) {
				App.setHeader(request)
			}
		});
	},

	getByTshirt : function($race) {
		return $.ajax({
			type : "GET",
			url : this.url + "/" + $race + "/analytics/tshirt",
			beforeSend : function(request) {
				App.setHeader(request)
			}
		});
	}
};
