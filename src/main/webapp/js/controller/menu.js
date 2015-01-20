$(function() {
	$(".loggedin-menu").hide();

	UserProxy.getLoggedInUser().done(getLoggedInUserOk);

	$("#bt-logout").click(function() {
		LogoutProxy.logout().done(logoutOk);
	});
});

function getLoggedInUserOk($data) {
	$(".loggedout-menu").hide();
	$(".loggedin-menu").show();
	$("#currentUser").html($data.profile.name);
}

function logoutOk() {
	$(".loggedin-menu").hide();
	$(".loggedout-menu").show();
	App.removeToken();
	location.href = App.getContextPath() + "/home";
}