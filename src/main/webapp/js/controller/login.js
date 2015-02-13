var googleAppId;

$(function() {
	$("#username").focus();
	$("#login-menu-item").addClass("active");

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
	setTimeout(loadGoogle, 0, data.google);
}

function loadFacebook(appId) {
	window.fbAsyncInit = function() {
		FB.init({
			appId : appId,
			status : true,
			cookie : true,
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

function loadGoogle(appId) {
	googleAppId = appId;

	window.___gcfg = {
		lang : 'pt-BR'
	};

	(function() {
		var po = document.createElement('script');
		po.type = 'text/javascript';
		po.async = true;
		po.src = 'https://apis.google.com/js/client:plusone.js?onload=loadGoogleOk';
		var s = document.getElementsByTagName('script')[0];
		s.parentNode.insertBefore(po, s);
	})();
}

function loadGoogleOk() {
	$("#google-login").removeAttr("disabled");

	$("#google-login")
			.click(
					function() {
						$("[id$='-message']").hide();
						showModal();

						gapi.auth
								.signIn({
									'clientid' : googleAppId,
									'cookiepolicy' : 'single_host_origin',
									'callback' : 'googleLogin',
									'scope' : 'https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/plus.login'
								});
					});
}

// Regular login process

function loginOk($data, $status, $request) {
	App.setToken($request.getResponseHeader('Set-Token'));
	App.setLoggedInUser($data);

	var url;
	var pendencies = false;

	if ($data.profile.pendencies > 0) {
		url = App.getContextPath() + "/user/profile";
		pendencies = true;

	} else if ($data.health.pendencies > 0) {
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
				App.restoreSavedLocation();
			}
		});

	} else {
		App.restoreSavedLocation();
	}
}

function loginFailed(request) {
	switch (request.status) {
		case 422:
			App.handle422(request);
			break;

		case 401:
			$("#global-message").html('Usuário ou senha inválidos.').show();
			break;
	}
}

// Facebook login process

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

// Google login process
function googleLogin(authResult) {
	if (authResult['access_token']) {
		$("form input").attr("disabled", true);

		var data = {
			'token' : authResult.code
		};

		LogonProxy.googleLogin(data).done(googleLoginOk);

	} else if (authResult['error']) {}
}

function googleLoginOk(data, status, request) {
	gapi.auth.signOut();
	loginOk(data, status, request);
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
