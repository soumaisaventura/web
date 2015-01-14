var App = {

	tokenKey : "Token",

	restoreLocation : function() {
		var url = sessionStorage.getItem("saved_location");
		location.href = url ? url : App.getContextPath() + "/home";
		sessionStorage.removeItem("saved_location");
	},

	saveLocation : function() {
		sessionStorage.setItem("saved_location", location.href);
	},

	getToken : function() {
		return sessionStorage.getItem(this.tokenKey);
	},

	setToken : function(token) {
		console.log(token);
		sessionStorage.setItem(this.tokenKey, token);
	},

	setHeader : function(request) {
		request.setRequestHeader("Authorization", "Token " + App.getToken());
	},

	removeToken : function() {
		sessionStorage.removeItem(this.tokenKey);
	},

	getContextPath : function() {
		return $("#contextPath").val();
	},

	getUrlParameterByName : function($name) {
		$name = $name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
		var regex = new RegExp("[\\?&]" + $name + "=([^&#]*)"), results = regex.exec(location.search);
		return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
	},

	handleValidation : function($request) {
		$($("form input, form select, form textarea").get().reverse()).each(function() {
			var id = $(this).attr('id');
			var message = null;

			$.each($request.responseJSON, function(index, value) {
				var aux = value.property ? value.property : "global";

				if (id == aux) {
					message = value.message;
					return;
				}
			});

			if (message) {
				$("#" + id + "-message").html(message).show();
				$(this).focus();
			} else {
				$("#" + id + "-message").hide();
			}
		});
	},
	
	loadDateCombos : function($day, $month, $year) {
		for (i=1; i<=31; i++){
			$($day).append(new Option(i, i));
		};
		
		var monthNames = [ "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho",
		                   "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro" ];
		for (i=1; i<=12; i++){
			$($month).append(new Option(monthNames[i-1], i));
		};
		
		for (i=2015; i>= 1915; i--){
			$($year).append(new Option(i, i));
		}
		
	}
};

var Spin = {
    "start" : function(){
        var opts = {
            lines : 13, // The number of lines to draw
            length : 40, // The length of each line
            width : 10, // The line thickness
            radius : 30, // The radius of the inner circle
            corners : 1, // Corner roundness (0..1)
            rotate : 0, // The rotation offset
            color : '#000', // #rgb or #rrggbb
            speed : 1, // Rounds per second
            trail : 60, // Afterglow percentage
            shadow : false, // Whether to render a shadow
            hwaccel : false, // Whether to use hardware acceleration
            className : 'spinner', // The CSS class to assign to the spinner
            zIndex : 2e9, // The z-index (defaults to 2000000000)
            top : $(window).height()/2.5, // Manual positioning in viewport
            left : "auto"
        };
        var target = $("#spin")[0];
        return new Spinner(opts).spin(target);
     },
     "stop" : function(spinner){
        spinner.stop();
     }
};

$.ajaxSetup({
	error : function(request) {
		switch (request.status) {
			case 401:
				alert("Olá, precisamos saber quem é você")
				App.saveLocation();
				location.href = App.getContextPath() + "/login";
				break;

			case 422:
				App.handleValidation(request);
				break;
		}
	}
});