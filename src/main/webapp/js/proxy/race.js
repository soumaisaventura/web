var RaceProxy = {

	url : App.getContextPath() + "/api/race",

	load : function($race) {
		return $.ajax({
			type : "GET",
			url : this.url + "/" + $race
		});
	},

	loadSummary : function($race) {
		return $.ajax({
			type : "GET",
			url : this.url + "/" + $race + "/summary",
			beforeSend : function(request) {
				App.setHeader(request)
			}
		});
	},

	findNext : function() {
		return $.ajax({
			type : "GET",
			url : this.url + "/next"
		});
	},

	findCourses : function($race) {
		return $.ajax({
			type : "GET",
			url : this.url + "/" + $race + "/courses"
		});
	},

	getBanner : function($race) {
		return $.ajax({
			type : "GET",
			url : this.url + "/" + $race + "/banner/base64"
		});
	},
	
	order : function($race, $users) {
		return $.ajax({
			type : "GET",
			url : this.url + "/" + $race + "/order?users=" + $users
		});
	}
};
