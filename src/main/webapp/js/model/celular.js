function Celular($area, $numero){
	this.area = $area;
	this.numero = $numero;
}

Celular.prototype = new Telefone()

Celular.prototype.constructor = Celular() 