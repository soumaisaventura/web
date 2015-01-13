$(document).ready(function() {
    var members = [];

	ProfileProxy.load().done(loadStep1Ok);
	HealthProxy.load().done(loadStep2Ok);
	RaceProxy.findCategories($("#race").val()).done(loadComboCategoriesOk);

	/** TODO 
	 * Trazer o nome do atleta logo como primeiro item e sem permissão de exclusão
	 * Guardar as informações num array e montar a lista baseada no array.
	 * Ao remover item retirar do array.
	 * Adicionar o excludes na pesquisa.
	 * Não permitir adicionar mais atletas que a quantidade permitida da corrida. 
	 */
	$("#user").autocomplete({
		source: function(request, response){
			RaceProxy.searchAvailableUsers($("#race").val(), request.term, members).done(function(data){
				response(convertToLabelValueStructure(data));
			});
		},
		minLength: 3,
		select: function( event, ui ) {
			members.push(ui.item.value);
							console.log(members);
			$("#members").append('<li class="list-group-item">' + ui.item.label + '<span class="pull-right glyphicon glyphicon-remove" aria-hidden="true"></span></li>');
			$("#user").val("");
			return false;
		}
	});
    
	// Barra de Navegação 
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
     
	// Cadastro dos dados pessoais
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
    
    // Cadastro dos dados médicos
    $('#activate-step-3').on('click', function(e) {
        var data = {
    		'bloodType' : $("#bloodType").val(),
    		'allergy' : $("#allergy").val()
    	};
        HealthProxy.update(data).done(updateStep2Ok).fail(updateStep2Fail);
    });
    
    // Cadastro dos dados da corrida
    $('#activate-step-4').on('click', function(e) {
    	
    	var data = {
    		'teamName' : $("#teamName").val(),
    		'category' : $("#category").val().split("#")[0],
    		'course' : $("#category").val().split("#")[2],
    		'members' : members
    	};
    	
    	RegisterProxy.validate(data, $("#race").val()).done(updateStep3Ok).fail(updateStep3Fail);
    	
    });
    
});

/**
 * Função que carrega os dados pessoais do usuário.
 */
function loadStep1Ok(data){
	console.log(data);
	$("#name").val(data.name);
	$("#rg").val(data.rg);
	$("#cpf").val(data.cpf);
	$("#birthday").val(data.birthday);
	$("#gender").val(data.gender);
	
	$("#loggedUser").text(data.name);
}

/**
 * Funçao que carrega os dados médicos do usuário.
 */
function loadStep2Ok(data){
	console.log(data);
	$("#bloodType").val(data.bloodType);
	$("#allergy").val(data.allergy);
}

/**
 * Monta a caixa de seleção das categorias disponíveis da corrida.
 * TODO Pensar numa estrutura para pegar a quantidade de membros da corrida. 
 */
function loadComboCategoriesOk(data){
	$.each(data, function(){
		$("#category").append(new Option(this.name, this.id + "#" + this.teamSize + "#" + this.course));
	});
}

/**
 * 
 * */
function updateStep1Ok(data){
	console.log('updateStep1Ok');
	$('ul.setup-panel li:eq(0)').addClass('disabled');
    $('ul.setup-panel li:eq(1)').removeClass('disabled');
    $('ul.setup-panel li a[href="#step-2"]').trigger('click');
}

/**
 * 
 * */
function updateStep2Ok(data){
	console.log('updateStep2Ok');
	$('ul.setup-panel li:eq(1)').addClass('disabled');
    $('ul.setup-panel li:eq(2)').removeClass('disabled');
    $('ul.setup-panel li a[href="#step-3"]').trigger('click');
}

/**
 * 
 * */
function updateStep3Ok(data){
	console.log('updateStep3Ok');
	$('ul.setup-panel li:eq(2)').addClass('disabled');
    $('ul.setup-panel li:eq(3)').removeClass('disabled');
    $('ul.setup-panel li a[href="#step-4"]').trigger('click');
}

/**
 * Tratamento de erro dos dados básicos. 
 * */
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

/**
 * Tratamento de erro dos dados médicos.
 * */
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

/**
 * Tratamento de erro dos dados corrida.
 * */
function updateStep3Fail(request){
	console.log('updateFail');
	switch (request.status) {
		case 422:
			$($("#form-step-3 input").get().reverse()).each(function() {
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

/**
 * Função utilitária que converte o objeto retornado no suggest para o formato do jqueryUi.
 * */
function convertToLabelValueStructure(data){
	var newData = [];
	$.each(data, function(){
		newData.push({"label" : this.name, "value" : this.id});
	});
	return newData;
}