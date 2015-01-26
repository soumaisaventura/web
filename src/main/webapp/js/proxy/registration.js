var RegistrationProxy = {

	url : App.getContextPath() + "/api/registration",

	find : function() {
		return $.ajax({
			type : "GET",
			url : this.url,
			beforeSend : function(request) {
				App.setHeader(request)
			}
		});
	},
	
	load : function($id) {
		return $.ajax({
			type : "GET",
			url : this.url + "/" + $id,
			beforeSend : function(request) {
				App.setHeader(request)
			}
		});
	}
};
