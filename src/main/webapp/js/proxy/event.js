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

	loadSummary : function(id) {
		return $.ajax({
			type : "GET",
			url : this.url + "/" + id + "/summary"
		});
	}
};
