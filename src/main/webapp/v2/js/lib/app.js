var App = {

	tokenKey : "Token",

	userKey : "User",

	savedLocationKey : "Saved Location",

	restoreSavedLocation : function() {
		var url = sessionStorage.getItem(this.savedLocationKey);
		return url;
	},

	saveLocation : function(url) {
		sessionStorage.setItem(this.savedLocationKey, url);
	},

	clearSavedLocation : function() {
		sessionStorage.removeItem(this.savedLocationKey);
	},

	setLoggedInUser : function(user) {
		sessionStorage.setItem(this.userKey, JSON.stringify(user));
	},

	isAdmin : function() {
		var user = this.getLoggedInUser();
		return user ? user.admin : false;
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

	setHeader : function(request) {
		request.setRequestHeader("Authorization", "Token " + App.getToken());
	},

	getBaseUrl : function() {
		return location.protocol + "//" + location.host + this.getContextPath();
	},

	getContextPath : function() {
		return $("#contextPath").val();
	},

	getUrlParameterByName : function(name) {
		name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
		var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"), results = regex.exec(location.search);
		return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
	},

	replaceEmailByMailTo : function(text) {
		if (!text) {
			return text;
		}

		var email_regex = /([a-zA-Z0-9._-]+@[a-zA-Z0-9._-]+\.[a-zA-Z0-9._-]+)/gi;
		return text.replace(email_regex, '<a href="mailto:$1">$1</a>');
	},

	handle401 : function(request) {
		console.log('xx');
		this.clearAuthentication();
		this.saveLocation(location.href);
		location.href = App.getContextPath() + "/login";
	},

	handle422 : function(request, mapper) {
		
		var mapperExist = typeof mapper !== 'undefined' ? true : false;
		
		var elements = $("form input, form select, form textarea").get().reverse();

		console.log(elements);
		
		$(elements).each(function() {
			var id = $(this).attr('id');
			var messages = [];

			$.each(request.responseJSON, function(index, value) {
				var aux = value.property ? value.property : "global";

				if(mapperExist){
					$.each(mapper, function(index, map){
						aux = aux === map.service ? map.screen : aux;
						return false;
					});
				}

				if (id === aux) {
					messages.push(value.message);
					return false;
				}
			});

			if (!id) {
				return false;
			}
			
			var message = $("#" + id.replace(".", "\\.") + "-message");

			if (messages.length > 1) {
				message.empty();
				var ul = message.append("<ul></ul>")

				while (messages.length > 0) {
					ul.append("<li>" + messages.pop() + "</li>");
				}

				message.show();
				$(this).focus();

			} else if (messages.length == 1) {
				message.html(messages.pop()).show();
				$(this).focus();

			} else {
				message.hide();
			}
		});
	},

	handle500 : function(request) {
		alert("Ocorreu um erro interno no servidor e o processamento não foi concluído. Informe ao administrador pelo e-mail: contato@soumaisaventura.com.br");
	},

	loadDateCombos : function(day, month, year) {
		for (i = 1; i <= 31; i++) {
			day.append(new Option(i, i));
		}

		var monthNames = [ "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro",
				"Dezembro" ];
		for (i = 1; i <= 12; i++) {
			month.append(new Option(monthNames[i - 1], i));
		}

		var fullYear = (new Date()).getFullYear();

		for (i = fullYear; i >= fullYear - 100; i--) {
			year.append(new Option(i, i));
		}
	},
	
	loadStateCombos : function(data, comboState) {
		$.each(data, function(index, state){
			comboState.append(new Option(state.abbreviation, state.id));
		});
	},

	translateStatus : function(status) {
		switch (status) {
			case "pendent":
				return '<span class="registration label label-warning">Pendente</span>';

			case "confirmed":
				return '<span class="registration label label-success">Confirmada</span>';

			case "cancelled":
				return '<span class="registration label label-danger">Cancelada</span>';

			default:
				return '<span class="registration label label-danger">Status desconhecido</span>';
		}
	}
};

$.ajaxSetup({
	error : function(request) {
		switch (request.status) {
			case 401:
				App.handle401(request);
				break;

			case 422:
				App.handle422(request);
				break;

			case 500:
				App.handle500(request);
				break;
		}
	}
});

$('document').ready(function() {
	var inputs = $('input[type="tel"]');

	if (inputs.length > 0) {
		var maskBehavior = function(val) {
			return val.replace(/\D/g, '').length === 11 ? '(00) 00000-0000' : '(00) 0000-00009';
		}, options = {
			onKeyPress : function(val, e, field, options) {
				field.mask(maskBehavior.apply({}, arguments), options);
			}
		};

		try {
			inputs.mask(maskBehavior, options);
		} catch (err) {
			console.log("você esqueceu de importar jquery.mask.min.js nesta página.");
		}
	}
});
