$(function() {
	
	LogonProxy.getOAuthAppIds().done(getOAuthAppIdsOk);

	$("#name").focus();
	$("#signup-menu-item").addClass("active");

	App.loadDateCombos($("#birthday"), $("#birthday-month"), $("#birthday-year"))

	$("form").submit(function(event) {
		event.preventDefault();
		$("[id$='-message']").hide();

		var birthday = "";
		if ($("#birthday-year").val() && $("#birthday-month").val() && $("#birthday").val()) {
			birthday = $("#birthday-year").val() + "-" + $("#birthday-month").val() + "-" + $("#birthday").val();
		}

		var data = {
			'name' : $("#name").val(),
			'password' : $("#password").val(),
			'email' : $("#email").val(),
			'birthday' : birthday,
			'gender' : $("#gender").val()
		};
		SignUpProxy.signup(data).done(signupOk);
	});
	
	
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

// SignUp process
function signupOk(data, status, request) {
	$(".has-error").removeClass("has-error");
	swal("Cadastro efetuado.", "Siga as instruções no seu e-mail para ativar a sua conta.", "success");
}
