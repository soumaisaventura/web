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
	
	if($data.profile.pendencies > 0){
		$("#profile-badge").text($data.profile.pendencies);
	}
	
	if($data.health.pendencies > 0){
		$("#health-badge").text($data.health.pendencies);
	}
}

function logoutOk() {
	$(".loggedin-menu").hide();
	$(".loggedout-menu").show();
	App.removeToken();
	location.href = App.getContextPath() + "/home";
}