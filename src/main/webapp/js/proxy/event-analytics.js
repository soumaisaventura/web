var EventAnalyticsProxy = {

	url : App.getContextPath() + "/api/event",

	getByCategories : function(id) {
		return $.ajax({
			type : "GET",
			url : this.url + "/" + id + "/analytics/category",
			beforeSend : function(request) {
				App.setHeader(request)
			}
		});
	},

	getByRaces : function(id) {
		return $.ajax({
			type : "GET",
			url : this.url + "/" + id + "/analytics/race",
			beforeSend : function(request) {
				App.setHeader(request)
			}
		});
	},

	getByStatus : function(id) {
		return $.ajax({
			type : "GET",
			url : this.url + "/" + id + "/analytics/status",
			beforeSend : function(request) {
				App.setHeader(request)
			}
		});
	},

	getByLocation : function(id) {
		return $.ajax({
			type : "GET",
			url : this.url + "/" + id + "/analytics/location",
			beforeSend : function(request) {
				App.setHeader(request)
			}
		});
	},

	getByTshirt : function(id) {
		return $.ajax({
			type : "GET",
			url : this.url + "/" + id + "/analytics/tshirt",
			beforeSend : function(request) {
				App.setHeader(request)
			}
		});
	}
};
