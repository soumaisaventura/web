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

	LogonProxy.getOAuthAppIds().done(getOAuthAppIdsOk);
});

function getOAuthAppIdsOk(data) {
	$("#facebook-login").click(function() {
		$("[id$='-message']").hide();
		showModal();

		FB.init({
			appId : data.facebook,
			status : true,
			cookie : true,
			xfbml : true
		});

		FB.login(facebookLogin, {
			scope : 'email,user_birthday'
		});
	});

	$("#google-login")
			.click(
					function() {
						$("[id$='-message']").hide();
						showModal();

						gapi.auth
								.signIn({
									'clientid' : data.google,
									'cookiepolicy' : 'single_host_origin',
									'callback' : 'googleLogin',
									'scope' : 'https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/plus.login'
								});
					});

	$("#facebook-login, #google-login").removeAttr("disabled");
}

// Regular login process

function loginOk($data, $status, $request) {
	App.setToken($request.getResponseHeader('Set-Token'));
	App.setLoggedInUser($data);
	App.restoreSavedLocation();
}

function loginFailed(request) {
	switch (request.status) {
		case 422:
			App.handle422(request);
			break;

		case 401:
			$("#global-message").html(request.responseText).show();
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
