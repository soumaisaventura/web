// Form events binding

$(function() {
    $(".input-group.date").datepicker({
	todayHighlight : true,
	language : "pt-BR",
	startView : 2
    });

    $("#salvar").click(function() {
	$("[id$='-message']").hide();

	var form = {
	    'fullName' : $("#fullName").val(),
	    'password' : $("#password").val(),
	    'email' : $("#email").val(),
	    'birthday' : $("#birthday").val(),
	    'gender' : $("#gender").val()
	};

	new SignUpProxy().signup(form, signupOk, signupFailed);
    });
});

// SignUp process

function signupOk(data) {
    $('#myModal').modal('toggle').on('hidden.bs.modal', function() {
	window.location = 'home';
    });
}

function signupFailed(request) {
    $.each(request.responseJSON.reverse(), function(index, value) {
	$("#" + value.property + "-message").html(value.message).show();
    });
}