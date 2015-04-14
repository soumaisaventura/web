$(function() {
	$("#race-next-menu-item").addClass("active");
	RaceProxy.find($("#ano").text()).done(findNextOk);

	$('#open-races').on('click', '.panel', function() {
		location.href = App.getContextPath() + "/prova/" + $(this).data("race");
	});

	$('#open-races').on('mouseover', '.panel', function() {
		$(this).css('cursor', 'pointer');
	});
});

function findNextOk(data) {
	var group1 = new Riloadr({
		breakpoints : [ {
			name : '320',
			maxWidth : 320
		}, // iPhone 3
		{
			name : '640',
			maxWidth : 320,
			minDevicePixelRatio : 2
		}, // iPhone 4 Retina display
		{
			name : '640',
			minWidth : 321,
			maxWidth : 640
		}, {
			name : '1024',
			minWidth : 641
		} ]
	});

	$
			.each(
					data,
					function(index, value) {
						
						var day = moment(value.date, "YYYY-MM-DD");
					
						var block = document.createElement("div");
							block.className = "race col-md-4";
							block.setAttribute("id","block-" + value.id);
						
						var panel = document.createElement("div");
							panel.className = "panel panel-default";
							panel.dataset.race = value.id;

						var heading = document.createElement("div");
							heading.className = "panel-heading";
							heading.style.padding = "0px";
							
						var body = document.createElement("div");
							body.className = "panel-body";
							body.style.paddingTop = "5px";
						
						var box = document.createElement("div");
							box.className = "box";
						
						var corner = document.createElement("div");
							corner.className = "corner";
						
						var span = document.createElement("span");
						
						var banner = document.createElement("img");
							banner.setAttribute("id", "banner-" + value.id);
							banner.setAttribute("alt", value.name);
							banner.className = "responsive";
							banner.dataset.src = App.getBaseUrl() + "/api/race/" + value.id + "/banner/{breakpoint-name}";
							
						
						var body = document.createElement("div");
							body.className = "panel-body";
							body.style.paddingTop = "5px";
							
						var row = document.createElement("div");
							row.className = "row";
						
						var col = document.createElement("div");
							col.className = "col-md-12 text-right";
							
						var left = document.createElement("h5");
							left.className = "pull-left";
							left.style.marginTop = "10px";
							left.style.marginBottom = "5px";
						
						var right = document.createElement("h5");
							right.className = "pull-right";
							right.style.marginTop = "10px";
							right.style.marginBottom = "5px";
						
						var calendarIcon = document.createElement("span");
							calendarIcon.className = "glyphicon glyphicon-calendar";
							calendarIcon.style.fontSize = "0.8em";
							
						var mapIcon = document.createElement("span");
							mapIcon.className = "glyphicon glyphicon-map-marker";
							mapIcon.style.fontSize = "0.8em";

						var date = document.createTextNode(day.locale("pt-br").format("DD [de] MMMM"));
						
						var place = document.createTextNode((value.city ? value.city : "Local não definido"));
						
						var text = document.createTextNode("");
						
						switch(value.status){
							case 'open'   : text = document.createTextNode("inscrições abertas");
											corner.className += " open";
											break;
										
							case 'closed' : text = document.createTextNode("inscrições encerradas");
											corner.className += " closed";
											break;
											
							case 'end'    : banner.className += " end";
											break;
							
							default		  : text = document.createTextNode("");
											break;
						}
						
						span.appendChild(text);
						corner.appendChild(span);
						box.appendChild(corner);
						box.appendChild(banner);
						heading.appendChild(box);
						
						left.appendChild(calendarIcon);
						left.appendChild(date);
						right.appendChild(mapIcon);
						right.appendChild(place);
						col.appendChild(left);
						col.appendChild(right);
						row.appendChild(col);
						body.appendChild(row);
						
						panel.appendChild(heading);
						panel.appendChild(body);
						
						block.appendChild(panel);
						
						$('#open-races').append(block);
					});
}

function carregarBanner(id, data) {
	var banner = "";

	if (data) {
		banner = "data:image/png;base64," + data;
	} else {
		banner = "http://placehold.it/750x350";
	}

	$("#banner-" + id).attr("src", banner);
	$("#block-" + id).show();
}
