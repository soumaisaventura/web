$(function() {
//	$(".loggedin-menu").hide();
//
//	loadMenu(App.getLoggedInUser());
//
//	$("#bt-logout").click(function() {
//		LogoutProxy.logout().done(logoutOk);
//	});
});

function loadMenu(data) {
	if (data) {
		$(".loggedout-menu").hide();
		$(".loggedin-menu").show();
		$("#currentUser").html(data.profile.name);

		if (data.profile.pendencies > 0) {
			$("#profile-badge").text(data.profile.pendencies);
		}

		if (data.health && data.health.pendencies > 0) {
			$("#health-badge").text(data.health.pendencies);
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
