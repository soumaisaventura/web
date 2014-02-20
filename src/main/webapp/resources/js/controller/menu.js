// Form events binding

$(function() {
    $("#logout").click(function() {
	new AuthProxy().logout(logoutOk);
    });
});

// Logout process

function logoutOk(data) {
    window.location = "home";
}