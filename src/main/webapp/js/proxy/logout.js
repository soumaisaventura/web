var LogoutProxy = {

    url: App.getContextPath() + "/api/logout",

    logout: function () {
        return $.ajax({
            type: "POST",
            url: this.url,
            beforeSend: function (request) {
                App.setHeader(request)
            }
        });
    }
};
