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

	getAppId : function() {
		return $.ajax({
			type : "GET",
			url : this.url + "/appid"
		});
	},
	
	facebookLogin : function($data) {
		return $.ajax({
			type : "POST",
			url : this.url + "/facebook",
			data : JSON.stringify($data),
			contentType : "application/json",
			processData : false,
		});
	},

	googleLogin : function($data) {
		return $.ajax({
			type : "POST",
			url : this.url + "/google",
			data : JSON.stringify($data),
			contentType : "application/json",
			processData : false,
		});
	},
};
