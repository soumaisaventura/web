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
});


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
