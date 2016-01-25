var App = {

	tokenKey : "Token",

	userKey : "User",

	savedLocationKey : "Saved Location",

	restoreSavedLocation : function() {
		var url = sessionStorage.getItem(this.savedLocationKey);
		location.href = (url ? url : App.getContextPath() + "/inicio");
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
		return user ? user.roles.admin : false;
	},

	getGlobalMessage : function(request) {
		var json = JSON.parse(request.responseText);
		return json && json.length == 1 && !json[0].property ? json[0].message : null;
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

	loginOk : function(data, status, request) {
		App.setToken(request.getResponseHeader('Set-Token'));
		App.setLoggedInUser(data);

		var url;
		var pendencies = false;

		if (data.pendencies.profile > 0) {
			url = App.getContextPath() + "/user/profile";
			pendencies = true;

		} else if (data.health && data.pendencies.health > 0) {
			url = App.getContextPath() + "/user/health";
			pendencies = true;
		}

		if (pendencies) {
			swal({
				title : "Dados cadastrais incompletos",
				text : "Para se inscrever nas provas você precisa resolver isso. É fácil e rápido!",
				type : "warning"
			}, function() {
				window.location.href = url;
			});
		} else {
			App.restoreSavedLocation();
		}
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
		this.clearAuthentication();
		this.saveLocation(location.href);
		location.href = App.getContextPath() + "/login";
	},

	handle422 : function(request) {
		var elements = $("form input, form select, form textarea").get().reverse();

		$(elements).each(function() {
			var id = $(this).attr('id');
			var messages = [];

			$.each(request.responseJSON, function(i, value) {
				var aux = value.property ? value.property : "global";

				if (id == aux) {
					messages.push(value.message);
					return;
				}
			});

			if (!id) {
				return;
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
	},

	moment : function(date) {
		return moment(date, "YYYY-MM-DD").locale("pt-br");
	},

	parsePeriod : function(period, excludeYear) {
		var text = "";
		if (period.beginning !== period.end) {
			if (moment(period.beginning).isSame(moment(period.end), 'month')) {
				text += App.moment(period.beginning).format("D");
			} else {
				text += App.moment(period.beginning).format("D [de] MMMM");
			}

			if (App.moment(period.end).diff(App.moment(period.beginning), 'days') > 1) {
				text += " à ";
			} else {
				text += " e ";
			}
		}
		text += App.moment(period.end).format("D [de] MMMM" + (excludeYear ? "" : " [de] YYYY"));
		return text;
	},

	parseCity : function(city) {
		return city.name + "/" + city.state;
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
