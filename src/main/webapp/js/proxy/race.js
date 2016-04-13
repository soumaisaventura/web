var RaceProxy = {

    url: App.getContextPath() + "/api/events",

    loadSummary: function (raceId, eventId) {
        return $.ajax({
            type: "GET",
            url: this.url + "/" + eventId + "/" + raceId + "/summary",
            beforeSend: function (request) {
                App.setHeader(request)
            }
        });
    },

	findKits : function(raceId, eventId) {
		return $.ajax({
			type : "GET",
			url : this.url + "/" + eventId + "/" + raceId + "/kits"
		});
	},
	
    findCategories: function (raceId, eventId) {
        return $.ajax({
            type: "GET",
            url: this.url + "/" + eventId + "/" + raceId + "/categories"
        });
    },

    getOrder: function (raceId, eventId, userId) {
        return $.ajax({
            type: "GET",
            url: this.url + "/" + eventId + "/" + raceId + "/order?user_id=" + userId
        });
    }
};
