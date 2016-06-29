var LocationProxy = {

    url: App.getContextPath() + "/api/location",

    findStates: function (countryId) {
        return $.ajax({
            type: "GET",
            url: this.url + "/countries/" + countryId + "/states"
        });
    },

    findCities: function (countryId, stateId) {
        return $.ajax({
            type: "GET",
            url: this.url + "/countries/" + countryId + "/states/" + stateId + "/cities"
        });
    }
};
