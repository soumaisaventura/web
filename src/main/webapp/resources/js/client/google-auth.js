var GoogleAuthClient = function GoogleAuthClient() {
	this.url = "api/auth/google";
};

GoogleAuthClient.prototype.login = function(code) {
	$.ajax({
		type : "POST",
		url : this.url,
		data : "code=" + code,
		processData: false,
		success : function() {
			gapi.auth.signOut();
			window.location = "index.jsf";
		},
		error : function() {
			console.log("login google failed");
		}
	});

};
