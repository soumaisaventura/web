var LocationProxy = {

    url: App.getContextPath() + "/api/location",

    searchCity: function ($filter) {
        return $.ajax({
            url: this.url + "/city",
            dataType: "json",
            data: {
                q: $filter
            }
        });
    },
    
    searchCityByUf: function ($uf) {
    	return [{"id" : "1", "name" : "Salvador"}, {"id" : "2", "name" : "Feira de Santana"}];
    	/*
    	 return $.ajax({
            url: this.url + "/city",
            dataType: "json",
            data: {
                q: $uf
            }
        });
        */
    }
};
