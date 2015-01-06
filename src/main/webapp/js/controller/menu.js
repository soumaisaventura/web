$(function() {
	$(".loggedin-menu").hide();
	
	UserProxy.getLoggedInUser().done(getLoggedInUserOk);

	$("#logout").click(function() {
		LogoutProxy.logout().done(logoutOk);
	});
});

function getLoggedInUserOk(data) {
	console.log(data);
	$(".loggedout-menu").hide();
	$(".loggedin-menu").show();
	$("#currentUser").html(data.name);
}

function logoutOk() {
	$(".loggedin-menu").hide();
	$(".loggedout-menu").show();
	App.removeToken();
}
