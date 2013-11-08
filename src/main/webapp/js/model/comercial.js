function Comercial($area, $numero){
	this.area = $area;
	this.numero = $numero;
}

Comercial.prototype = new Telefone()

Comercial.prototype.constructor = Comercial() 