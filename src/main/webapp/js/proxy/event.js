var EventProxy = {

	url : App.getContextPath() + "/api/event",

	find : function(year) {
		return $.ajax({
			type : "GET",
			url : this.url + "/year/" + year
		});
	},

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
