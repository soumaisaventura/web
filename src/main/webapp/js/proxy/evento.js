var EventoProxy = {

	url : App.getContextPath() + "/api/event",

	load : function($slug) {
		return $.ajax({
			type : "GET",
			url : this.url + "/" + $slug
		});
	}
	
};
