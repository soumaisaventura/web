var RaceProxy = {

	url : "api/race",

	findNext : function() {
		return $.ajax({
			type : "GET",
			url : this.url + "/next",
			contentType : "application/json"
		});
	}
};
