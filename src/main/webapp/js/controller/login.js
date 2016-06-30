var googleAppId;

$(function () {
    $("#username").focus();
    $("#login-menu-item").addClass("active");

    $("form").submit(function (event) {
        event.preventDefault();
        $(".message").hide();

        var data = {
            'username': $("#username").val().trim(),
            'password': $("#password").val()
        };

        LogonProxy.login(data).done(App.loginOk).fail(loginFailed);
    });

    LogonProxy.getOAuthAppIds().done(getOAuthAppIdsOk);
});

function getOAuthAppIdsOk(data) {
    setTimeout(loadFacebook, 0, data.facebook);
    setTimeout(loadGoogle, 0, data.google);
}

function loadFacebook(appId) {
    window.fbAsyncInit = function () {
        FB.init({
            appId: appId,
            status: true,
            cookie: false,
            xfbml: true,
            version: 'v2.0'
        });

        FB.getLoginStatus(loadFacebookOk);
    };

    (function (d, s, id) {
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

    $("#facebook-login").click(function () {
        $(".message").hide();
        showModal();

        FB.login(facebookLogin, {
            scope: 'email,user_birthday'
        });
    });
}

function loadGoogle(appId) {
    googleAppId = appId;

    window.___gcfg = {
        lang: 'pt-BR'
    };

    (function () {
        var po = document.createElement('script');
        po.type = 'text/javascript';
        po.async = true;
        po.src = 'https://apis.google.com/js/client:plusone.js?onload=loadGoogleOk';
        var s = document.getElementsByTagName('script')[0];
        s.parentNode.insertBefore(po, s);
    })();
}

function loadGoogleOk() {
    console.log($("#google-login"));
    $("#google-login").removeAttr("disabled");

    $("#google-login")
        .click(
            function () {
                $(".message").hide();
                showModal();

                gapi.auth
                    .signIn({
                        'clientid': googleAppId,
                        'cookiepolicy': 'single_host_origin',
                        'callback': 'googleLogin',
                        'scope': 'https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/plus.login'
                    });
            });
}

// Regular login process

function loginFailed(request) {
    switch (request.status) {
        case 422:
            App.handle422(request);
            break;

        case 401:
            swal({
                title: "",
                text: request.responseText,
                confirmButtonClass: "btn-danger",
                type: "error"
            });
            break;
    }
}

// Facebook login process

function facebookLogin(response) {
    if (response.authResponse) {
        $("form input").attr("disabled", true);

        var data = {
            'token': response.authResponse.accessToken
        };

        LogonProxy.facebookLogin(data).done(facebookLoginOk).fail(facebookLoginFailed);
    } else {
        hideModal();
    }
}

function facebookLoginOk(data, status, request) {
    // FB.logout();
    App.loginOk(data, status, request);
}

function facebookLoginFailed(request) {
    console.log("login facebook failed");
}

// Google login process
function googleLogin(authResult) {
    if (authResult['access_token']) {
        $("form input").attr("disabled", true);

        var data = {
            'token': authResult.code
        };

        LogonProxy.googleLogin(data).done(googleLoginOk);

    } else if (authResult['error']) {
    }
}

function googleLoginOk(data, status, request) {
    gapi.auth.signOut();
    App.loginOk(data, status, request);
}

function showModal() {
    $('#modal').modal('show');

    var opts = {
        lines: 11, // The number of lines to draw
        length: 19, // The length of each line
        width: 10, // The line thickness
        radius: 22, // The radius of the inner circle
        corners: 1, // Corner roundness (0..1)
        rotate: 0, // The rotation offset
        direction: 1, // 1: clockwise, -1: counterclockwise
        color: '#fff', // #rgb or #rrggbb or array of colors
        speed: 1, // Rounds per second
        trail: 50, // Afterglow percentage
        shadow: false, // Whether to render a shadow
        hwaccel: true, // Whether to use hardware acceleration
        className: 'spinner', // The CSS class to assign to the spinner
        zIndex: 2e9, // The z-index (defaults to 2000000000)
        top: 'auto', // Top position relative to parent in px
        left: 'auto' // Left position relative to parent in px
    };

    var target = document.getElementById('spin');
    var spinner = new Spinner(opts).spin(target);
}

function hideModal() {
    $('#modal').modal('hide');
}
