var LocationProxy = {

	url : App.getContextPath() + "/api/location",

	searchCity : function($filter) {
		return $.ajax({
			url : this.url + "/city",
			dataType : "json",
			data : {
				q : $filter
			}
		});
	}
};
