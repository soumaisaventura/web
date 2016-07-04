$(function () {
    $(".loggedin-menu").hide();

    loadMenu(App.getLoggedInUser());

    $("#bt-logout").click(function () {
        LogoutProxy.logout().done(logoutOk);
    });
});

function loadMenu(user) {
    if (user) {
        $(".loggedout-menu").hide();
        $(".loggedin-menu").show();
        $("#currentUser").html("<img style='width: 20px; margin-right: 5px;' src='" + user.picture.thumbnail + "'/>" + user.profile.short_name);

        console.log(user);

        if (user.profile && user.profile.pendencies > 0) {
            $("#profile-badge").text(user.profile.pendencies);
        }

        if (user.health && user.health.pendencies > 0) {
            $("#health-badge").text(user.health.pendencies);
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
