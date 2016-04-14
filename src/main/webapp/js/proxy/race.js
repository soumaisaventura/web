var RaceProxy = {

    url: App.getContextPath() + "/api/events",

    load: function (raceId, eventId) {
        return $.ajax({
            type: "GET",
            url: this.url + "/" + eventId + "/races/" + raceId,
            beforeSend: function (request) {
                App.setHeader(request)
            }
        });
    },

    getOrder: function (raceId, eventId, userId) {
        return $.ajax({
            type: "GET",
            url: this.url + "/" + eventId + "/races/" + raceId + "/order?user_id=" + userId
        });
    }
};
