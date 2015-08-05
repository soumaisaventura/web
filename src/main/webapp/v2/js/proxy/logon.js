var LogonProxy = {

	url : App.getContextPath() + "/api/logon",

	login : function($data) {
		return $.ajax({
			type : "POST",
			url : this.url,
			data : JSON.stringify($data),
			contentType : "application/json",
			error : function() {}
		});
	},

	getOAuthAppIds : function() {
		return $.ajax({
			type : "GET",
			url : this.url + "/oauth"
		});
	},

	facebookLogin : function($data) {
		return $.ajax({
			type : "POST",
			url : this.url + "/oauth/facebook",
			data : JSON.stringify($data),
			contentType : "application/json",
			processData : false,
		});
	}
};
