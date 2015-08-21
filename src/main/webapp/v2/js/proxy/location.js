var LocationProxy = {

	url : App.getContextPath() + "/api/location",

	loadStates : function() {
		return $.ajax({
			url : this.url + "/uf",
			dataType : "json"
		});
	},
	
	searchCity : function(abbreviation) {
		return $.ajax({
			url : this.url + "/uf/" + abbreviation + "/cities",
			dataType : "json"
		});
	}
};
