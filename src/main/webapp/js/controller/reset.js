// Form events binding

$(function() {
    $("#send").click(function() {
	$("[id$='-message']").hide();

	var form = {
	    'email' : $("#email").val()
	};

	new ResetProxy().requestPasswordReset(form, resetOk, resetFailed);
    });
});

// Password Reset process

function resetOk(data) {
    $("#global-message").html(
	    "Acesse seu e-mail e siga as instruções para redefinir sua senha.")
	    .show();
}

function resetFailed(request) {
    $.each(request.responseJSON.reverse(), function(index, value) {
	$("#" + value.property + "-message").html(value.message).show();
    });
}
