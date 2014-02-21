// Form events binding

$(function() {
    $("#update").click(
	    function() {
		$("[id$='-message']").hide();

		var form = {
		    'email' : $("#email").val(),
		    'newPassword' : $("#newPassword").val()
		};

		var token = $.url().param('token');
		new ResetProxy().performPasswordReset(form, token, resetOk,
			resetFailed);
	    });
});

// Password Reset process

function resetOk(data) {
    location.href = './';
}

function resetFailed(request) {
    switch (request.status) {
    case 400:
	break;

    case 412:
	$.each(request.responseJSON.reverse(), function(index, value) {
	    $("#" + value.property + "-message").html(value.message).show();
	});
	break;
    }
}
