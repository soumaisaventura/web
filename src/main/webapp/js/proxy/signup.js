var SignUpProxy = {

	url : "api/signup",

	signup : function($data) {
		return $.ajax({
			type : "POST",
			url : this.url,
			data : JSON.stringify($data),
			contentType : "application/json"
		});
	},

	unregister : function() {
		return $.ajax({
			type : "DELETE",
			url : this.url
		});
	}
};
