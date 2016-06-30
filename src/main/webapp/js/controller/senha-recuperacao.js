$(function () {
    $("#email").focus();

    $("form").submit(function (event) {
        event.preventDefault();
        $(".message").hide();

        var data = {
            'email': $("#email").val()
        };

        PasswordProxy.recovery(data).done(resetOk);
    });
});

function resetOk(data) {
    swal({
        title: "Recuperação de senha",
        text: "Acesse seu e-mail e siga as instruções para redefinir sua senha.",
        type: "success"
    });
}
