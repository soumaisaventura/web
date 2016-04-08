var EventProxy = {

	url : App.getContextPath() + "/api/event",

	find : function(year) {
		return $.ajax({
			type : "GET",
			url : this.url + "/year/" + year
		});
	},

	load : function(id) {
		return $.ajax({
			type : "GET",
			url : this.url + "/" + id
		});
	},

	getRegistrarions : function(id) {
		return $.ajax({
			type : "GET",
			url : this.url + "/" + id + "/registrations",
			beforeSend : function(request) {
				App.setHeader(request)
			}
		});
	},

	loadSummary : function(id) {
		return $.ajax({
			type : "GET",
			url : this.url + "/" + id + "?summary=true"
		});
	}
};
