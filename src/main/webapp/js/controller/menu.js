$(function() {
	$(".loggedin-menu").hide();

	loadMenu(App.getLoggedInUser());

	$("#bt-logout").click(function() {
		LogoutProxy.logout().done(logoutOk);
	});
});

function loadMenu(user) {
	if (user) {
		$(".loggedout-menu").hide();
		$(".loggedin-menu").show();
		$("#currentUser").html(user.name);

		if (user.pendencies.profile && user.pendencies.profile > 0) {
			$("#profile-badge").text(user.pendencies.profile);
		}

		if (user.pendencies.health && user.pendencies.health > 0) {
			$("#health-badge").text(user.pendencies.health);
		}
	}
}

function logoutOk() {
	$(".loggedin-menu").hide();
	$(".loggedout-menu").show();
	App.clearAuthentication();
	App.clearSavedLocation();

	location.href = App.getContextPath() + "/home";
}
