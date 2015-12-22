var RaceRegistrationProxy = {

	url : App.getContextPath() + "/api/event",

	submitRegistration : function(raceId, eventId, data) {
		return $.ajax({
			type : "POST",
			url : this.url + "/" + eventId + "/" + raceId + "/registration",
			data : JSON.stringify(data),
			contentType : "application/json",
			beforeSend : function(request) {
				App.setHeader(request)
			}
		});
	},

	// find : function(eventId) {
	// return $.ajax({
	// type : "GET",
	// url : this.url + "/" + eventId + "/registration/list",
	// beforeSend : function(request) {
	// App.setHeader(request)
	// }
	// });
	// },

	formDownload : function(raceId, callback) {
		this.download(this.url + "/" + raceId + "/registration/form", callback);
	},

	exportDownload : function(raceId, callback) {
		this.download(this.url + "/" + raceId + "/registration/export", callback);
	},

	download : function(url, callback) {
		var xhr = new XMLHttpRequest();
		xhr.open("GET", url);
		xhr.responseType = 'blob';
		xhr.onreadystatechange = function() {
			if (this.readyState == 4 && this.status == 200) {
				var blob = new Blob([ this.response ], {
					type : this.response.type
				});

				var url = (window.URL || window.webkitURL).createObjectURL(blob);
				location.href = url;
				callback();
			}
		};
		App.setHeader(xhr)
		xhr.send();
	}

};
