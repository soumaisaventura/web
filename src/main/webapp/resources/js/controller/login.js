// Form events binding

$(function() {
    $("#login").click(function() {
	$("[id$='-message']").hide();
	login();
    });

    $(document).keypress(function(e) {
	if (e.which == 13) {
	    login();
	}
    });

    $("#facebook-login").click(function() {
	$("[id$='-message']").hide();
	showModal();

	FB.init({
	    appId : '548688288560242',
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
				    'clientid' : '611475192580-k33ghah4orsl7d4r1r6qml5i4rtgnnrd.apps.googleusercontent.com',
				    'cookiepolicy' : 'single_host_origin',
				    'callback' : 'googleLogin',
				    'scope' : 'https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile'
				});
		    });
});

// Regular login process

function login() {
    var form = {
	'username' : $("#username").val(),
	'password' : $("#password").val()
    };

    new AuthProxy().login(form, loginOk, loginFailed);
}

function loginOk(data) {
    location.href = "./";
}

function loginFailed(request) {
    switch (request.status) {
    case 412:
	$.each(request.responseJSON, function(index, value) {
	    $("#" + value.property + "-message").html(value.message).show();
	});
	break;

    case 401:
	$("#global-message").html("Usuário ou senha inválidos.").show();
	break;
    }
}

// Facebook login process

function facebookLogin(response) {
    if (response.authResponse) {
	$("#form-login input").attr("disabled", true);
	new AuthProxy().facebookLogin(response.authResponse.accessToken,
		facebookLoginOk, facebookLoginFailed);

    } else {
	hideModal();
    }
}

function facebookLoginOk(data) {
    FB.logout();
    location.href = "./";
}

function facebookLoginFailed(request) {
    console.log("login facebook failed");
}

// Google login process

function googleLogin(authResult) {
    if (authResult['access_token']) {
	$("#form-login input").attr("disabled", true);
	new AuthProxy().googleLogin(authResult.code, googleLoginOk);

    } else if (authResult['error']) {
	// hideModal();
    }
}

function googleLoginOk(data) {
    gapi.auth.signOut();
    location.href = "./";
}

// UI elements manipulation

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