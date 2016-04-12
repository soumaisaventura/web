$(function () {
	moment.locale("pt-br");
	numeral.language('pt-br');
	numeral.defaultFormat('$ 0,0');

	var id = $("#id").val();

	RaceProxy.getBanner(id).done(getBannerOk);
	RaceProxy.load(id).done(loadOk);
});

function getBannerOk(data) {
	if (data) {
		$("#banner").attr("src", "data:image/png;base64," + data);
		$("#banner-section").show();
	}
}

function loadOk(data) {
	$(".race-name").text(data.name);
	$("#date").prepend(moment(data.date, "YYYY-MM-DD").format('LL'));

	if (data.description) {
		$("#description").prepend(data.description);
	} else {
		$("#description").hide();
	}

	if (data.site) {
		$("#site").prepend(data.site);
		$("#site").parent().attr("href", data.site);
		$("#site-section").show();
	}

	if (data.city) {
		$("#city").prepend(data.city);
		$("#city-section").show();
	}

	if (data.registration.prices && data.registration.prices.length > 0) {
		$.each(data.registration.prices, function (index, value) {
			$("#registration-periods").append(
				"<h4><span style='font-size: 0.8em'>" + moment(value.beginning, "YYYY-MM-DD").format('DD/MM') + " à "
				+ moment(value.end, "YYYY-MM-DD").format('DD/MM') + ":</span> " + numeral(value.price).format() + "</h4>")
		});
		$("#registration-periods-section").show();
	}

	if (data.courses && data.courses.length > 0) {
		var category = "";
		$.each(data.courses, function (index, value) {
			category += "<h4>" + value.name + "</h4>";
			category += "<ul>";
			$.each(value.categories, function (index, value) {
				category += "<li>" + value.name + " <span title='" + value.description + "'><span></li>";
			});
			category += "</ul>";
		});
		$("#categories").after(category);
		$("#categories-section").show();
	}

	var user = App.getLoggedInUser();
	var authorized = user ? user.admin : null;
	if (data.organizers && data.organizers.length > 0) {
		$.each(data.organizers, function (index, value) {
			$("#organizers").append(
				"<h4>" + value.name + " <a style='font-size:0.8em; color:#EA8E13' href='mailto:" + value.email + "?Subject=Dúvida sobra a prova "
				+ data.name + "'>" + value.email + "</a></h4>");

			if (user && value.id == user.id) {
				authorized = true;
			}
		});
		$("#organizers-section").show();
	}

	if (data.status == 'open' && !authorized) {
		$("#bt-registration-section").show();
	}

	if (authorized) {
		$("#bt-manage-section").show();
	}
}
