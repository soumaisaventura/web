var RaceProxy = {

	url : App.getContextPath() + "/api/race",

	load : function($race) {
		return $.ajax({
			type : "GET",
			url : this.url + "/" + $race
		});
	},

	loadSummary : function(raceId) {
		return $.ajax({
			type : "GET",
			url : this.url + "/" + raceId + "/summary",
			beforeSend : function(request) {
				App.setHeader(request)
			}
		});
	},

	formDownload : function(raceId) {
		return $.ajax({
			type : "GET",
			async : false,
			url : this.url + "/" + raceId + "/form",
			beforeSend : function(request) {
				App.setHeader(request)
			}
		});
	},

	find : function(year) {
		return $.ajax({
			type : "GET",
			url : this.url + "/year/" + year
		});
	},

	findCourses : function(raceId) {
		return $.ajax({
			type : "GET",
			url : this.url + "/" + raceId + "/courses"
		});
	},

	getBanner : function(raceId) {
		return $.ajax({
			type : "GET",
			url : this.url + "/" + raceId + "/banner/base64"
		});
	},

	order : function(raceId, $users) {
		return $.ajax({
			type : "GET",
			url : this.url + "/" + raceId + "/order?users=" + $users
		});
	}
};
