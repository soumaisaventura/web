$(function() {
	RaceProxy.getBanner($("#race").val()).done(getBannerOk);
	RaceProxy.load($("#race").val()).done(loadOk);

	$("#bt-registration").click(function() {
		location.href = $("#race").val() + "/registration";
	});
});

function getBannerOk(data) {
	if (data) {
		$("#banner").attr("src", "data:image/png;base64," + data);
		$("#banner-section").show();
	}
}

function loadOk(data) {
	$("#name").prepend(data.name);
	$("#date").prepend(moment(data.date, "YYYY-MM-DD").locale("pt-br").format('L'));

	if (data.description) {
		$("#description").prepend(data.description);
	} else {
		$("#description").hide();
	}

	if (data.city) {
		$("#city").prepend(data.city);
		$("#city-section").show();
	}

	if (data.registration.open) {
		$("#bt-registration-section").show();
	}

	if (data.registration.periods.length > 0) {
		$.each(data.registration.periods, function(index, value) {
			$("#registration-periods").append(
					"<h4>" + moment(value.beginning, "YYYY-MM-DD").locale("pt-br").format('DD/MM') + " à "
							+ moment(value.end, "YYYY-MM-DD").locale("pt-br").format('DD/MM') + ": R$ " + value.price
							+ " <span style='font-size:0.8em; color:green'>+anuidade</span></h4>")
		});
		$("#registration-periods-section").show();
	}

	if (data.courses.length > 0) {
		var category = "";
		$.each(data.courses, function(index, value) {
			category += "<h4>" + value.length + " km</h4>";
			category += "<ul>";
			$.each(value.categories, function(index, value) {
				category += "<li>" + value.name + " <span title='" + value.description + "'><span></li>";
			});
			category += "</ul>";
		});
		$("#categories").after(category);
		$("#categories-section").show();
	}

	if (data.organizers.length > 0) {
		$.each(data.organizers, function(index, value) {
			$("#organizers").append("<h4>" + value.name + " <span style='font-size:0.8em; color:#EA8E13'>" + value.email + "</span></h4>");
		});
		$("#organizers-section").show();
	}
}
