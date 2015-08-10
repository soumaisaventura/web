$(function() {
	
	// Coloca o foco no campo username quando o modal é aberto
	$('#login').on('shown.bs.modal', function () {
		$("#username").focus();
	});
	
	$("form").submit(function(event) {
		event.preventDefault();
		$("[id$='-message']").hide();

		var data = {
			'username' : $("#username").val().trim(),
			'password' : $("#password").val()
		};

		LogonProxy.login(data).done(loginOk).fail(loginFailed);
	});

	LogonProxy.getOAuthAppIds().done(getOAuthAppIdsOk);
});

function getOAuthAppIdsOk(data) {
	setTimeout(loadFacebook, 0, data.facebook);
}

function loadFacebook(appId) {
	window.fbAsyncInit = function() {
		FB.init({
			appId : appId,
			status : true,
			cookie : false,
			xfbml : true,
			version : 'v2.0'
		});

		FB.getLoginStatus(loadFacebookOk);
	};

	(function(d, s, id) {
		var js, fjs = d.getElementsByTagName(s)[0];
		if (d.getElementById(id)) {
			return;
		}
		js = d.createElement(s);
		js.id = id;
		js.src = "//connect.facebook.net/pt_BR/all.js";
		fjs.parentNode.insertBefore(js, fjs);
	}(document, 'script', 'facebook-jssdk'));
}

function loadFacebookOk(response) {
	$("#facebook-login").removeAttr("disabled");

	$("#facebook-login").click(function() {
		$("[id$='-message']").hide();
		showModal();

		FB.login(facebookLogin, {
			scope : 'email,user_birthday'
		});
	});
}

// Processo regular de autenticação
function loginOk(data, status, request) {
	App.setToken(request.getResponseHeader('Set-Token'));
	App.setLoggedInUser(data);

	var url;
	var pendencies = false;

	if (data.profile.pendencies > 0) {
		url = App.getContextPath() + "/user/profile";
		pendencies = true;

	} else if (data.health && data.health.pendencies > 0) {
		url = App.getContextPath() + "/user/health";
		pendencies = true;
	}

	if (pendencies) {
		bootbox.dialog({
			title : "Atenção!",
			message : "Seus dados cadastrais estão incompletos. Para se inscrever nas provas você precisa resolver isso. É fácil e rápido!",
			buttons : {
				main : {
					label : "<span class='glyphicon glyphicon-edit' aria-hidden='true' style='font-size: 0.8em;'></span> Resolver agora",
					className : "btn-success",
					callback : function() {
						location.href = url;
					}
				}
			},
			onEscape : function() {
				// TODO: Verificar se está jogando para a página correta. 
				location.href = App.restoreSavedLocation();
			}
		});

	} else {
		$('#login').modal('hide');
		// O método loadMenu está no controller menu.js
		loadMenu(App.getLoggedInUser());
	}
}

function loginFailed(request) {
	switch (request.status) {
		case 422:
			App.handle422(request);
			break;

		case 401:
			$("#login-message").html(request.responseText).show();
			break;
	}
}

// Processo de login com Facebook
function facebookLogin(response) {
	if (response.authResponse) {
		$("form input").attr("disabled", true);

		var data = {
			'token' : response.authResponse.accessToken
		};

		LogonProxy.facebookLogin(data).done(facebookLoginOk).fail(facebookLoginFailed);
	} else {
		hideModal();
	}
}

function facebookLoginOk(data, status, request) {
	// FB.logout();
	loginOk(data, status, request);
}

function facebookLoginFailed(request) {
	console.log("login facebook failed");
}

function showModal() {
	$('#modal').modal('show');

	var opts = {
		lines : 11, // The number of lines to draw
		length : 19, // The length of each line
		width : 10, // The line thickness
		radius : 22, // The radius of the inner circle
		corners : 1, // Corner roundness (0..1)
		rotate : 0, // The rotation offset
		direction : 1, // 1: clockwise, -1: counterclockwise
		color : '#fff', // #rgb or #rrggbb or array of colors
		speed : 1, // Rounds per second
		trail : 50, // Afterglow percentage
		shadow : false, // Whether to render a shadow
		hwaccel : true, // Whether to use hardware acceleration
		className : 'spinner', // The CSS class to assign to the spinner
		zIndex : 2e9, // The z-index (defaults to 2000000000)
		top : 'auto', // Top position relative to parent in px
		left : 'auto' // Left position relative to parent in px
	};

	var target = document.getElementById('spin');
	var spinner = new Spinner(opts).spin(target);
}

function hideModal() {
	$('#modal').modal('hide');
}
