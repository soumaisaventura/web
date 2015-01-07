$(document).ready(function() {
	
	ProfileProxy.load().done(loadOk);
    
    var navListItems = $('ul.setup-panel li a'),
        allWells = $('.setup-content');

    allWells.hide();

    navListItems.click(function(e)
    {
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
    
    // DEMO ONLY //
    $('#activate-step-2').on('click', function(e) {
    	
        //$('ul.setup-panel li:eq(1)').removeClass('disabled');
        //$('ul.setup-panel li a[href="#step-2"]').trigger('click');
        
        var data = {
    			'name' : $("#name").val(),
    			'rg' : $("#rg").val(),
    			'cpf' : $("#cpf").val(),
    			'birthday' : $("#birthday").val(),
    			'gender' : $("#gender").val()
    	};
        
        console.log(data);
        ProfileProxy.update(data).done(updateOk).fail(updateFail);
        
    })    
});

/**
 * Função que carrega os dados pessoais do usuário.
 * */
function loadOk(data){
	console.log(data);
	$("#name").val(data.name);
	$("#rg").val(data.rg);
	$("#cpf").val(data.cpf);
	$("#birthday").val(data.birthday);
	$("#gender select").val(data.gender);
}

function updateOk(data){
	console.log('updateOk');
	console.log(data);
}

function updateFail(data){
	console.log('updateFail');
	console.log(data);
}