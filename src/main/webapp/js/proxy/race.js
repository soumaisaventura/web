var RaceProxy = {

	url : App.getContextPath() + "/api/race",

	findNext : function() {
		return $.ajax({
			type : "GET",
			url : this.url + "/next",
			contentType : "application/json"
		});
	},
	
	findCategories : function(race) {
		return $.ajax({
			type: "GET",
			url : this.url + "/" + race + "/categories",
			contentType : "application/json"
		});
	}
};
