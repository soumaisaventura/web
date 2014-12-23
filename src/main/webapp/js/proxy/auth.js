var AuthProxy = {

	url : "api/auth",

	login : function($data) {
		return $.ajax({
			type : "POST",
			url : this.url,
			data : JSON.stringify($data),
			contentType : "application/json",
			error : function() {}
		});
	},

	logout : function() {
		return $.ajax({
			type : "DELETE",
			url : this.url,
			beforeSend : function(request) {
				App.setHeader(request)
			}
		});
	},

	getUser : function() {
		return $.ajax({
			url : this.url,
			type : "GET",
			error : function() {},
			beforeSend : function(request) {
				App.setHeader(request)
			}
		});
	},

	facebookLogin : function(code) {
		return $.ajax({
			type : "POST",
			url : this.url + "/facebook",
			data : "code=" + code,
			processData : false,
		});
	},

	googleLogin : function(code) {
		return $.ajax({
			type : "POST",
			url : this.url + "/google",
			data : "code=" + code,
			processData : false,
		});
	}
};
