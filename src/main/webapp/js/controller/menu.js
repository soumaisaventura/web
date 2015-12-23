$(function() {
	$(".loggedin-menu").hide();

	loadMenu(App.getLoggedInUser());

	$("#bt-logout").click(function() {
		LogoutProxy.logout().done(logoutOk);
	});
});

function loadMenu(data) {
	if (data) {
		$(".loggedout-menu").hide();
		$(".loggedin-menu").show();
		$("#currentUser").html(data.name);

		if (data.pendencies.profile > 0) {
			$("#profile-badge").text(data.pendencies.profile);
		}

		if (data.health && data.pendencies.health > 0) {
			$("#health-badge").text(data.pendencies.health);
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
