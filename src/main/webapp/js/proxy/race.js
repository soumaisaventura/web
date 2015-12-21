var RaceProxy = {

	url : App.getContextPath() + "/api/event",

	load : function($race) {
		return $.ajax({
			type : "GET",
			url : this.url + "/" + $race
		});
	},

	loadSummary : function(raceId, eventId) {
		return $.ajax({
			type : "GET",
			url : this.url + "/" + eventId + "/" + raceId + "/summary",
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

	findCategories : function(raceId, eventId) {
		return $.ajax({
			type : "GET",
			url : this.url + "/" + eventId + "/" + raceId + "/categories"
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
