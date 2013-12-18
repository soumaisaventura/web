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
			console.log("login google ok");
		},
		error : function() {
			console.log("login google failed");
		}
	});

};
