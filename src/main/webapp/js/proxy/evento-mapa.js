var EventoMapaProxy = {

	url : App.getContextPath() + "/api/event/map",

	load : function() {
		return $.ajax({
			type : "GET",
			url : this.url
		});
	}

};
