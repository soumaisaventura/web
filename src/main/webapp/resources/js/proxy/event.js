var EventProxy = function EventProxy() {
	this.url = "api/event";
};

EventProxy.prototype.search = function($success, $error) {
	$.ajax({
		type : "GET",
		url : this.url,
		success : function(data) {
			if ($success) {
				$success(data);
			}
		},
		error : function(request) {
			if ($error) {
				$error(request);
			}
		}
	});
};
