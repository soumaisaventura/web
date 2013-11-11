var PersistenceService = function(baseUrl) {
	this.url = baseUrl;
	return this;
};

PersistenceService.prototype.create = function($type, $data, $success, $error) {
	return $.ajax({
		type : "POST",
		url : this.url + "/" + $type,
		data : JSON.stringify($data),
		contentType : "application/json;charset=utf8",
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

PersistenceService.prototype.update = function($type, $data, $success, $error) {
	return $.ajax({
		type : "PUT",
		url : this.url + "/" + $type,
		data : JSON.stringify($data),
		contentType : "application/json;charset=utf8",
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

PersistenceService.prototype.get = function($type, $id, $success, $error) {
	return $.ajax({
		type : "GET",
		url : this.url + "/" + $type + "/" + $id,
		contentType : "application/json;charset=utf8",
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

PersistenceService.prototype.all = function($type, $success, $error) {
	return $.ajax({
		type : "GET",
		url : this.url + "/" + $type,
		contentType : "application/json;charset=utf8",
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

PersistenceService.prototype.remove = function($type, $id, $success, $error) {
	return $.ajax({
		type : "DELETE",
		url : this.url + "/" + $type + "/" + $id,
		contentType : "application/json;charset=utf8",
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
