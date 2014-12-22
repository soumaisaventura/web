$(function() {
	AuthProxy.getUser().done(getUserOk);

	$("#logout").click(function() {
		AuthProxy.logout().done(logoutOk);
	});
});

function getUserOk(data) {
	$("#loggedout-menu").hide();
	$("#loggedin-menu").show();
	$("#username").html(data.name);
}

function logoutOk() {
	$("#loggedin-menu").hide();
	$("#loggedout-menu").show();
	App.removeToken();
}
