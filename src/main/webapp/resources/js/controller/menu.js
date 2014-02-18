// Form events binding

$(function() {
	$("#logout").click(function() {
		var proxy = new AuthProxy();
		proxy.logout(logoutOk);
	});
});

// Logout process

function logoutOk(data) {
	window.location = "home";
}