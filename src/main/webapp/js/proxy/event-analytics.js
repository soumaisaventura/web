var EventAnalyticsProxy = {

    url: App.getContextPath() + "/api/events",

    getByCategories: function (id) {
        return $.ajax({
            type: "GET",
            url: this.url + "/" + id + "/analytics/category",
            beforeSend: function (request) {
                App.setHeader(request)
            }
        });
    },

    getByRaces: function (id) {
        return $.ajax({
            type: "GET",
            url: this.url + "/" + id + "/analytics/race",
            beforeSend: function (request) {
                App.setHeader(request)
            }
        });
    },

    getByStatus: function (id) {
        return $.ajax({
            type: "GET",
            url: this.url + "/" + id + "/analytics/status",
            beforeSend: function (request) {
                App.setHeader(request)
            }
        });
    },

    getByStatusByDay: function (id) {
        return $.ajax({
            type: "GET",
            url: this.url + "/" + id + "/analytics/status/day",
            beforeSend: function (request) {
                App.setHeader(request)
            }
        });
    },

    getByLocation: function (id) {
        return $.ajax({
            type: "GET",
            url: this.url + "/" + id + "/analytics/location",
            beforeSend: function (request) {
                App.setHeader(request)
            }
        });
    },

    getByTshirt: function (id) {
        return $.ajax({
            type: "GET",
            url: this.url + "/" + id + "/analytics/tshirt",
            beforeSend: function (request) {
                App.setHeader(request)
            }
        });
    },

    getGenders: function (id) {
        return $.ajax({
            type: "GET",
            url: this.url + "/" + id + "/analytics/gender",
            beforeSend: function (request) {
                App.setHeader(request)
            }
        });
    },

    getAmountRaised: function (id) {
        return $.ajax({
            type: "GET",
            url: this.url + "/" + id + "/analytics/amountraised",
            beforeSend: function (request) {
                App.setHeader(request)
            }
        });
    },

    getAmountDiscounted: function (id) {
        return $.ajax({
            type: "GET",
            url: this.url + "/" + id + "/analytics/discount",
            beforeSend: function (request) {
                App.setHeader(request)
            }
        });
    },

    gerRegistrationByAgeGroup: function (id) {
        return $.ajax({
            type: "GET",
            url: this.url + "/" + id + "/analytics/agegroup",
            beforeSend: function (request) {
                App.setHeader(request)
            }
        });
    }
};
