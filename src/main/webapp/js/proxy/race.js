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
		var xhr = new XMLHttpRequest();
		xhr.open("GET", this.url + "/" + raceId + "/form", true);
		xhr.responseType = 'blob';
		// xhr.onload = function() {
		xhr.onreadystatechange = function() {
			if (this.readyState == 4 && this.status == 200) {
				var blob = new Blob([ this.response ], {
					type : this.response.type
				});

				var url = (window.URL || window.webkitURL).createObjectURL(blob);
				location.href = url;
			}
		};
		App.setHeader(xhr)
		xhr.send();

		// return $.ajax({
		// type : "GET",
		// url : this.url + "/" + raceId + "/form",
		// beforeSend : function(request) {
		// App.setHeader(request)
		// },
		// xhrFields : {
		// responseType : "arraybuffer"
		// },
		// dataType : "binary",
		// });
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
