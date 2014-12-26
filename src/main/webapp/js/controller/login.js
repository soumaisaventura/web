$(function() {
	$("#username").focus();

	$("form").submit(function(event) {
		event.preventDefault();
		$("[id$='-message']").hide();

		var data = {
			'username' : $("#username").val().trim(),
			'password' : $("#password").val()
		};

		LogonProxy.login(data).done(loginOk).fail(loginFailed);
	});

	$("#facebook-login").click(function() {
		$("[id$='-message']").hide();

		FB.init({
			appId : '1422799641299675',
			status : true,
			cookie : true,
			xfbml : true
		});

		FB.login(facebookLogin, {
			scope : 'email,user_birthday'
		});
	});

	$("#google-login").click(function() {
		$("[id$='-message']").hide();

		gapi.auth.signIn({
			'clientid' : '611475192580-k33ghah4orsl7d4r1r6qml5i4rtgnnrd.apps.googleusercontent.com',
			'cookiepolicy' : 'single_host_origin',
			'callback' : 'googleLogin',
			'scope' : 'https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile'
		});
	});
});

// Regular login process

function loginOk(data, status, request) {
	App.setToken(request.getResponseHeader('Set-Token'));
	location.href = "./";
}

function loginFailed(request) {
	switch (request.status) {
		case 422:
			App.handleValidation(request);
			break;

		case 401:
			$("#global-message").html("Usuário ou senha inválidos.").show();
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

	}
}

function facebookLoginOk(data, status, request) {
	FB.logout();
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