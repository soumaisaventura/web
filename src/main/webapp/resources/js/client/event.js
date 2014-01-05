var EventClient = function EventClient() {
	this.url = "api/event";
};

EventClient.prototype.search = function($successCallback, $errorCallback) {
	$.ajax({
		type : "GET",
		url : this.url,
		contentType : "application/json;charset=utf8",
		success : function(data) {
			if ($successCallback) {
				$successCallback(data);
			}
		},
		error : function(data) {
			if ($errorCallback) {
				$errorCallback(data);
			}
		}		
	});
};
