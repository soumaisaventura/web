var App = {
	
	tokenKey : "Token",

	restoreLocation : function() {
		var url = sessionStorage.getItem("saved_location");
		location.href = (url ? url : App.getContextPath() + "/home");
	},

	saveLocation : function($url) {
		sessionStorage.setItem("saved_location", $url);
	},

	getToken : function() {
		return sessionStorage.getItem(this.tokenKey);
	},

	setToken : function(token) {
		sessionStorage.setItem(this.tokenKey, token);
	},

	isLoggedIn : function() {
		return App.getToken() != null;
	},

	setHeader : function($request) {
		$request.setRequestHeader("Authorization", "Token " + App.getToken());
	},

	removeToken : function() {
		sessionStorage.removeItem(this.tokenKey);
	},

	getContextPath : function() {
		return $("#contextPath").val();
	},

	getUrlParameterByName : function($name) {
		$name = $name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
		var regex = new RegExp("[\\?&]" + $name + "=([^&#]*)"), results = regex.exec(location.search);
		return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
	},

	handleValidation : function($request) {
		$($("form input, form select, form textarea").get().reverse()).each(function() {
			var id = $(this).attr('id');
			var message = null;

			$.each($request.responseJSON, function(index, value) {
				var aux = value.property ? value.property : "global";

				if (id == aux) {
					message = value.message;
					return;
				}
			});

			if (message) {
				$("#" + id.replace(".", "\\.") + "-message").html(message).show();
				$(this).focus();
			} else {
				$("#" + id.replace(".", "\\.") + "-message").hide();
			}
		});
	},

	loadDateCombos : function($day, $month, $year) {
		for (i = 1; i <= 31; i++) {
			$($day).append(new Option(i, i));
		}

		var monthNames = [ "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro",
				"Dezembro" ];
		for (i = 1; i <= 12; i++) {
			$($month).append(new Option(monthNames[i - 1], i));
		}

		var year = (new Date()).getFullYear();

		for (i = year; i >= year - 100; i--) {
			$($year).append(new Option(i, i));
		}
	}
};

$.ajaxSetup({
	error : function(request) {
		switch (request.status) {
			case 401:
				// alert(location.href);
				App.saveLocation(location.href);
				location.href = App.getContextPath() + "/login";
				break;

			case 422:
				App.handleValidation(request);
				break;
		}
	}
});
