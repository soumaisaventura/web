var RegistrationProxy = {

	url : App.getContextPath() + "/api/registration",

	find : function() {
		return $.ajax({
			type : "GET",
			url : this.url,
			beforeSend : function(request) {
				App.setHeader(request)
			}
		});
	},

	load : function(id) {
		return $.ajax({
			type : "GET",
			url : this.url + "/" + id,
			beforeSend : function(request) {
				App.setHeader(request)
			}
		});
	},

	loadPublic : function(id) {
		return $.ajax({
			type : "GET",
			url : this.url + "/" + id + "/public"
		});
	},

	sendPayment : function(id) {
		return $.ajax({
			type : "POST",
			url : this.url + "/" + id + "/payment",
			beforeSend : function(request) {
				App.setHeader(request)
			}
		});
	},

	updateRacePrice : function(id, member, price) {
		return $.ajax({
			type : "PUT",
			url : this.url + "/" + id + "/member/" + member + "/price",
			data : price,
			contentType : "application/json",
			beforeSend : function(request) {
				App.setHeader(request)
			}
		});
	},

	updateTeamName : function(id, name) {
		return $.ajax({
			type : "PUT",
			url : this.url + "/" + id + "/team/name",
			data : name,
			contentType : "application/json",
			beforeSend : function(request) {
				App.setHeader(request)
			}
		});
	},

	confirm : function(id) {
		return $.ajax({
			type : "POST",
			url : this.url + "/" + id + "/confirm",
			beforeSend : function(request) {
				App.setHeader(request)
			}
		});
	},

	cancel : function(id) {
		return $.ajax({
			type : "POST",
			url : this.url + "/" + id + "/cancel",
			beforeSend : function(request) {
				App.setHeader(request)
			}
		});
	}
};
