function Residencial($area, $numero){
	this.area = $area;
	this.numero = $numero;
}

Residencial.prototype = new Telefone()

Residencial.prototype.constructor = Residencial() 