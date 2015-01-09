var App = {

	tokenKey : "Token",

	restoreLocation : function() {
		var url = sessionStorage.getItem("saved_location");
		location.href = url ? url : App.getContextPath() + "/home";
		sessionStorage.removeItem("saved_location");
	},

	saveLocation : function() {
		sessionStorage.setItem("saved_location", location.href);
	},

	getToken : function() {
		return sessionStorage.getItem(this.tokenKey);
	},

	setToken : function(token) {
		console.log(token);
		sessionStorage.setItem(this.tokenKey, token);
	},

	setHeader : function(request) {
		request.setRequestHeader("Authorization", "Token " + App.getToken());
	},

	removeToken : function() {
		sessionStorage.removeItem(this.tokenKey);
	},

	getContextPath : function() {
		return $("#contextPath").val();
	},

	getUrlParameterByName : function(name) {
		name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
		var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"), results = regex.exec(location.search);
		return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
	},

	handleValidation : function(request) {
		$($("form input, form select, form textarea").get().reverse()).each(function() {
			var id = $(this).attr('id');
			var message = null;

			$.each(request.responseJSON, function(index, value) {
				var aux = value.property ? value.property : "global";

				if (id == aux) {
					message = value.message;
					return;
				}
			});

			if (message) {
				$("#" + id + "-message").html(message).show();
				$(this).focus();
			} else {
				$("#" + id + "-message").hide();
			}
		});
	}
};

$.ajaxSetup({
	error : function(request) {
		switch (request.status) {
			case 401:
				alert("Olá, precisamos saber quem é você")
				App.saveLocation();
				location.href = App.getContextPath() + "/login";
				break;

			case 422:
				App.handleValidation(request);
				break;
		}
	}
});
