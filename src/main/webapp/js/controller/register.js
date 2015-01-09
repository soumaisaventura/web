$(document).ready(function() {
	
	ProfileProxy.load().done(loadStep1Ok);
	HealthProxy.load().done(loadStep2Ok);
    
    var navListItems = $('ul.setup-panel li a'),
        allWells = $('.setup-content');

    allWells.hide();

    navListItems.click(function(e) {
        e.preventDefault();
        var $target = $($(this).attr('href')),
            $item = $(this).closest('li');
        
        if (!$item.hasClass('disabled')) {
            navListItems.closest('li').removeClass('active');
            $item.addClass('active');
            allWells.hide();
            $target.show();
        }
    });
    
    $('ul.setup-panel li.active a').trigger('click');
    
    $('#activate-step-2').on('click', function(e) {
        var data = {
    			'name' : $("#name").val(),
    			'rg' : $("#rg").val(),
    			'cpf' : $("#cpf").val(),
    			'birthday' : $("#birthday").val(),
    			'gender' : $("#gender").val()
    	};
        ProfileProxy.update(data).done(updateStep1Ok).fail(updateStep1Fail);
    });
    
    $('#activate-step-3').on('click', function(e) {
        var data = {
    			'bloodType' : $("#bloodType").val(),
    			'allergy' : $("#allergy").val()
    	};
        HealthProxy.update(data).done(updateStep2Ok).fail(updateStep2Fail);
    });
    
    $('#activate-step-4').on('click', function(e) {
        var data = {
    	};
        Proxy.update(data).done(updateStep3Ok).fail(updateStep3Fail);
    });
    
});

/**
 * Função que carrega os dados pessoais do usuário.
 * */
function loadStep1Ok(data){
	console.log(data);
	$("#name").val(data.name);
	$("#rg").val(data.rg);
	$("#cpf").val(data.cpf);
	$("#birthday").val(data.birthday);
	$("#gender").val(data.gender);
}

function loadStep2Ok(data){
	console.log(data);
	$("#bloodType").val(data.bloodType);
	$("#allergy").val(data.allergy);
}

function updateStep1Ok(data){
	console.log('updateStep1Ok');
	$('ul.setup-panel li:eq(0)').addClass('disabled');
    $('ul.setup-panel li:eq(1)').removeClass('disabled');
    $('ul.setup-panel li a[href="#step-2"]').trigger('click');
}

function updateStep2Ok(data){
	console.log('updateStep2Ok');
	$('ul.setup-panel li:eq(1)').addClass('disabled');
    $('ul.setup-panel li:eq(2)').removeClass('disabled');
    $('ul.setup-panel li a[href="#step-3"]').trigger('click');
}

function updateStep1Fail(request){
	console.log('updateFail');
	switch (request.status) {
		case 422:
			$($("#form-step-1 input").get().reverse()).each(function() {
				var id = $(this).attr('id');
				var message = null;
	
				$.each(request.responseJSON, function(index, value) {
					if (id == value.property) {
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
			break;

		default:
			break;
	}
}

function updateStep2Fail(request){
	console.log('updateFail');
	switch (request.status) {
		case 422:
			$($("#form-step-2 input").get().reverse()).each(function() {
				var id = $(this).attr('id');
				var message = null;
	
				$.each(request.responseJSON, function(index, value) {
					if (id == value.property) {
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
			break;

		default:
			break;
	}
}