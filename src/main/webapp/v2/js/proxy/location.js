var LocationProxy = {

	url : App.getContextPath() + "/api/location",

	loadCities : function($filter) {
		return $.ajax({
			url : this.url + "/city",
			dataType : "json",
			data : {
				stateId : $filter
			}
		});
	},

	loadStates : function(){
		return $.ajax({
			url : this.url + "/uf",
			dataType : "json"
		});
	}
};
