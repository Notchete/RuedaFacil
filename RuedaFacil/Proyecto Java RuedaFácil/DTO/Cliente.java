package DTO;
public class Cliente {
	private String dni;
	private String nombre;
	private String telefono;
	private String correo;
	private String direccion;
	private String numCarnet;

	public Cliente() {}

	public Cliente(String dni, String nombre, String telefono, String correo, String direccion, String numCarnet) {
	    this.dni = dni;
	    this.nombre = nombre;
	    this.telefono = telefono;
	    this.correo = correo;
	    this.direccion = direccion;
	    this.numCarnet = numCarnet;
	}
	
	public String getDni() { return dni; }
	public void setDni(String dni) { this.dni = dni; }

	public String getNombre() { return nombre; }
	public void setNombre(String nombre) { this.nombre = nombre; }

	public String getTelefono() { return telefono; }
	public void setTelefono(String telefono) { this.telefono = telefono; }

	public String getCorreo() { return correo; }
	public void setCorreo(String correo) { this.correo = correo; }

	public String getDireccion() { return direccion; }
	public void setDireccion(String direccion) { this.direccion = direccion; }

	public String getNumCarnet() { return numCarnet; }
	public void setNumCarnet(String numCarnet) { this.numCarnet = numCarnet; }

	@Override
	public String toString() {
    	return "Cliente [DNI=" + dni + ", Nombre=" + nombre + ", Carnet=" + numCarnet + "]";
	}
}