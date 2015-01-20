var RaceRegistrationProxy = {

	url : App.getContextPath() + "/api/race",

	validateRegistration : function($race, $data) {
		return $.ajax({
			type : "POST",
			url : this.url + "/" + $race + "/registration/validate",
			data : JSON.stringify($data),
			contentType : "application/json",
			beforeSend : function(request) {
				App.setHeader(request)
			}
		});
	},

	submitRegistration : function($race, $data) {
		return $.ajax({
			type : "POST",
			url : this.url + "/" + $race + "/registration",
			data : JSON.stringify($data),
			contentType : "application/json",
			beforeSend : function(request) {
				App.setHeader(request)
			}
		});
	}
};
