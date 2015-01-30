$(function() {

	// "receiverEmail" : "arnaldo_maciel@hotmail.com",

	// var form = {
	// "email" : "arnaldo_maciel@hotmail.com",
	// "token" : "F5320349987D4692BD5E599695E7CF5D",
	// "currency" : "BRL",
	// "itemId1" : "0001",
	// "itemDescription1" : "Inscrição Individual",
	// "itemAmount1" : "80.00",
	// "itemQuantity1" : 2,
	//
	// "itemId1" : "0002",
	// "itemDescription1" : "Anuidade da Federação",
	// "itemAmount1" : "10.00",
	// "itemQuantity1" : 2,
	//
	// "reference" : "000001",
	//
	// "senderName" : "Cleverson Sacramento",
	// "senderEmail" : "cleverson.sacramento@gmail.com",
	// };

	// var form = ""
	// form += '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>';
	// form += ' <checkout>';
	// form += ' <currency>BRL</currency>';
	// form += ' <items>';
	// form += ' <item>';
	// form += ' <id>' + '1' + '</id>';
	// form += ' <description>' + 'Inscrição Cleverson Sacramento' +
	// '</description>';
	// form += ' <amount>' + '80.00' + '</amount>';
	// form += ' <quantity>' + '1' + '</quantity>';
	// form += ' </item>';
	// form += ' </items>';
	// form += ' <reference>' + '000001' + '</reference>';
	// form += ' <sender>';
	// form += ' <name>' + 'Cleverson Sacramento' + '</name>';
	// form += ' <email>' + 'cleverson.sacramento@gmail.com' + '</email>';
	// form += ' </sender>';
	// form += ' </checkout>';
	
	
	var form = "email=arnaldo_maciel%40hotmail.com&token=F5320349987D4692BD5E599695E7CF5D&currency=BRL&itemId1=0001&itemDescription1=Inscri%C3%A7%C3%A3o+Individual&itemAmount1=80.00&itemQuantity1=2&itemId1=0002&itemDescription1=Anuidade+da+Federa%C3%A7%C3%A3o&itemAmount1=10.00&itemQuantity1=2&reference=000001&senderName=Cleverson+Sacramento&senderEmail=cleverson.sacramento%40gmail.com";

	$("form").submit(function(event) {
		event.preventDefault();

		var ajax = $.ajax({
			type : "POST",
			url : "https://ws.pagseguro.uol.com.br/v2/checkout/",
			data : form
		// ,
		// contentType : "application/x-www-form-urlencoded;charset=UTF-8;",
		// beforeSend : function($request) {
		// $request.setRequestHeader("Access-Control-Allow-Origin", "*");
		// }
		});

		ajax.done(function($data, $status, $request) {

			console.log($data);
			console.log($request);

		}).fail(function($request) {
			console.log($request);
		});

	});

});
