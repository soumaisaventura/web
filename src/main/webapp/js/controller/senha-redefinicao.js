$(function () {
    $("#email").focus();

    $("form").submit(function (event) {
        event.preventDefault();
        $("[id$='-message']").hide();

        var data = {
            'email': $("#email").val(),
            'newPassword': $("#newPassword").val()
        };

        var token = App.getUrlParameterByName('token');
        PasswordProxy.reset(data, token).done(App.loginOk).fail(App.handle422Global);
    });
});