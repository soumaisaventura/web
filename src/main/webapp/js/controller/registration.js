$(function() {

	/**
	 * Carrega dados da inscrição
	 */
	RegistrationProxy.load($("#registration").val()).done(loadOk);
	
});

/* ---------------- Funções de Callback ---------------- */

/**
 * Carrega dados da inscrição
 */
function loadOk(data){
	console.log(data);
}
