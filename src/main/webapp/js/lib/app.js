var App = {

	tokenKey : "Token",

	userKey : "User",

	restoreLocation : function() {
		var url = sessionStorage.getItem("saved_location");
		location.href = (url ? url : App.getContextPath() + "/home");
	},

	saveLocation : function($url) {
		sessionStorage.setItem("saved_location", $url);
	},

	setLoggedInUser : function($user) {
		sessionStorage.setItem(this.userKey, JSON.stringify($user));
	},

	getLoggedInUser : function() {
		return JSON.parse(sessionStorage.getItem(this.userKey));
	},

	clearAuthentication : function() {
		sessionStorage.removeItem(this.userKey);
		sessionStorage.removeItem(this.tokenKey);
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
			var cont = 0;

			$.each($request.responseJSON, function(index, value) {
				var aux = value.property ? value.property : "global";

				if (id == aux) {
					cont++;
					message = value.message;
					return;
				}
			});

			$("." + id.replace(".", "\\.") + "-message").each(function(index, value) {
				if (index === 0) {
					$(this).hide();
				} else {
					$(this).remove();
				}
			});

			if (cont > 1) {
				$("br").remove();
				showMultipleErrorMessage(id, $request.responseJSON);
				return;
			}

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
				App.clearAuthentication();
				App.saveLocation(location.href);
				location.href = App.getContextPath() + "/login";
				break;

			case 422:
				App.handleValidation(request);
				break;
		}
	}
});

function showMultipleErrorMessage($atribute, $messages) {
	$.each($messages, function(index, value) {
		if (index === 0) {
			$("." + $atribute.replace(".", "\\.") + "-message").last().text(value.message);
		} else {
			$("." + $atribute.replace(".", "\\.") + "-message").last().clone()
					.insertAfter($("#" + $atribute.replace(".", "\\.") + "-message").last()).text(value.message);
		}
	});

	$("." + $atribute.replace(".", "\\.") + "-message").each(function() {
		$(this).after("<br/>").show();
	});
}
