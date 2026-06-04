package DTO;

public class Vehiculo {
	private String matricula;
	private String marca;
	private String modelo;
	private int anioFabricacion;
	private Combustible combustible;
	private int asientos;
	private double precioPorDia;
	private EstadoV estadoV;
	private Categoria categoria;

	public Vehiculo() {}
	
	public Vehiculo(String matricula, String marca, String modelo, int anioFabricacion, Combustible combustible,
			int asientos, double precioPorDia, EstadoV estadoV, Categoria categoria) {
		super();
		this.matricula = matricula;
		this.marca = marca;
		this.modelo = modelo;
		this.anioFabricacion = anioFabricacion;
		this.combustible = combustible;
		this.asientos = asientos;
		this.precioPorDia = precioPorDia;
		this.estadoV = estadoV;
		this.categoria = categoria;
	}

	public String getMatricula() { return matricula; }
	public void setMatricula(String matricula) { this.matricula = matricula; }

	public String getMarca() { return marca; }
	public void setMarca(String marca) { this.marca = marca; }

	public String getModelo() { return modelo; }
	public void setModelo(String modelo) { this.modelo = modelo; }

	public int getAnioFabricacion() { return anioFabricacion; }
	public void setAnioFabricacion(int anioFabricacion) { this.anioFabricacion = anioFabricacion; }

	public Combustible getCombustible() { return combustible; }
	public void setCombustible(Combustible combustible) { this.combustible = combustible; }

	public int getAsientos() { return asientos; }
	public void setAsientos(int asientos) { this.asientos = asientos; }

	public double getPrecioPorDia() { return precioPorDia; }
	public void setPrecioPorDia(double precioPorDia) { this.precioPorDia = precioPorDia; }

	public EstadoV getEstadoV() { return estadoV; }
	public void setEstadoV(EstadoV estadoV) { this.estadoV = estadoV; }

	public Categoria getCategoria() { return categoria; }
	public void setCategoria(Categoria categoria) { this.categoria = categoria; }

	@Override
	public String toString() {
    	return "Vehiculo [" + matricula + " - " + marca + " " + modelo + " (" + estadoV + ")]";
	}
}