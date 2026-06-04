package DTO;
import java.time.LocalDate;

public class Alquiler {
	private int idA;
	private LocalDate fechaInicio;
	private LocalDate fechaDevolucionPrevista;
	private LocalDate fechaDevolucion;
	private double precio;
	private EstadoA estadoA;
	private Cliente cliente;
	private Empleado empleado;
	private Vehiculo vehiculo;

	public Alquiler() {}

	public Alquiler(int idA, LocalDate fechaInicio, LocalDate fechaDevolucionPrevista, LocalDate fechaDevolucion,
			double precio, EstadoA estadoA, Cliente cliente, Empleado empleado, Vehiculo vehiculo) {
		super();
		this.idA = idA;
		this.fechaInicio = fechaInicio;
		this.fechaDevolucionPrevista = fechaDevolucionPrevista;
		this.fechaDevolucion = fechaDevolucion;
		this.precio = precio;
		this.estadoA = estadoA;
		this.cliente = cliente;
		this.empleado = empleado;
		this.vehiculo = vehiculo;
	}

	public int getIdA() { return idA; }
	public void setIdA(int idA) { this.idA = idA; }

	public LocalDate getFechaInicio() { return fechaInicio; }
	public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

	public LocalDate getFechaDevolucionPrevista() { return fechaDevolucionPrevista; }
	public void setFechaDevolucionPrevista(LocalDate fechaDevolucionPrevista) { this.fechaDevolucionPrevista = fechaDevolucionPrevista; }

	public LocalDate getFechaDevolucion() { return fechaDevolucion; }
	public void setFechaDevolucion(LocalDate fechaDevolucion) { this.fechaDevolucion = fechaDevolucion; }

	public double getPrecio() { return precio; }
	public void setPrecio(double precio) { this.precio = precio; }

	public EstadoA getEstadoA() { return estadoA; }
	public void setEstadoA(EstadoA estadoA) { this.estadoA = estadoA; }

	public Cliente getCliente() { return cliente; }
	public void setCliente(Cliente cliente) { this.cliente = cliente; }

	public Empleado getEmpleado() { return empleado; }
	public void setEmpleado(Empleado empleado) { this.empleado = empleado; }

	public Vehiculo getVehiculo() { return vehiculo; }
	public void setVehiculo(Vehiculo vehiculo) { this.vehiculo = vehiculo; }

	@Override
	public String toString() {
		return "Alquiler ID=" + idA + " [Cliente=" + (cliente != null ? cliente.getNombre() : "null") + ", Vehiculo=" + (vehiculo != null ? vehiculo.getMatricula() : "null") + ", Total=" + precio + "€, Estado=" + estadoA + "]";
	}
}