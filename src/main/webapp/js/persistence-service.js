var PersistenceService = function(baseUrl) {
	this.url = baseUrl;
	return this;
};

PersistenceService.prototype.action = function($service, $method, $id, $data, $success, $error) {
	return $.ajax({
		type : $method,
		url : this.url + "/" + $service + ($id ? "/" + $id : ""),
		contentType : "application/json;charset=utf8",
		data : $data ? JSON.stringify($data) : null,
		dataType : "json",
		success : function(respData) {
			if ($success) {
				$success(respData);
			}
		},
		error : function(respData){
			if ($error){
				$error(respData);
			}
		}
	});
};

PersistenceService.prototype.create = function($service, $data, $success, $error) {
	return this.action($service, "POST", null, $data, $success, $error);
};

PersistenceService.prototype.update = function($service, $data, $success, $error) {
	return this.action($service, "PUT", null, $data, $success, $error);
};

PersistenceService.prototype.get = function($service, $id, $success, $error) {
	return this.action($service, "GET", $id, null, $success, $error);
};

PersistenceService.prototype.all = function($service, $success, $error) {
	return this.action($service, "GET", null, null, $success, $error);
};

PersistenceService.prototype.remove = function($service, $id, $success, $error) {
	return this.action($service, "DELETE", $id, null, $success, $error);
};
