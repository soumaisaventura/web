var EventoProxy = {

	url : App.getContextPath() + "/api/event",

	load : function(slug) {
		return $.ajax({
			type : "GET",
			url : this.url + "/" + slug
		});
	},

	loadMap : function(slug) {
		return $.ajax({
			type : "GET",
			url : this.url + "/" + slug + "/map"
		});
	},

	getBanner : function(slug) {
		return $.ajax({
			type : "GET",
			url : this.url + "/" + slug + "/banner/base64"
		});
	}

};
