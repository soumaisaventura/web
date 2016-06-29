var EventProxy = {

    url: App.getContextPath() + "/api/events",

    find: function (year) {
        return $.ajax({
            type: "GET",
            url: this.url + "/year/" + year
        });
    },

    load: function (id) {
        return $.ajax({
            type: "GET",
            url: this.url + "/" + id
        });
    },

    findRegistrations: function (id) {
        return $.ajax({
            type: "GET",
            url: this.url + "/" + id + "/registrations",
            beforeSend: function (request) {
                App.setHeader(request)
            }
        });
    },

    loadSummary: function (id) {
        return $.ajax({
            type: "GET",
            url: this.url + "/" + id + "?summary=true"
        });
    }
};
