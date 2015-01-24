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
};
