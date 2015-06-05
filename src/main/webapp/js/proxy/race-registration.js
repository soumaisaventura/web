var RaceRegistrationProxy = {

	url : App.getContextPath() + "/api/race",

	validateRegistration : function(race, data) {
		return $.ajax({
			type : "POST",
			url : this.url + "/" + race + "/registration/validate",
			data : JSON.stringify(data),
			contentType : "application/json",
			beforeSend : function(request) {
				App.setHeader(request)
			}
		});
	},

	submitRegistration : function(race, data) {
		return $.ajax({
			type : "POST",
			url : this.url + "/" + race + "/registration",
			data : JSON.stringify(data),
			contentType : "application/json",
			beforeSend : function(request) {
				App.setHeader(request)
			}
		});
	},

	find : function(race) {
		return $.ajax({
			type : "GET",
			url : this.url + "/" + race + "/registration/list",
			beforeSend : function(request) {
				App.setHeader(request)
			}
		});
	},

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
