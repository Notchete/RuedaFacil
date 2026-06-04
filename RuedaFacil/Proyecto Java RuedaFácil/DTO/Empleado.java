package DTO;

public class Empleado {
	private int idE;
	private String nombre;
	private String puesto;
	private String oficina;
	private Turno turno;
	private int aniosExp;

	public Empleado() {}

	public Empleado(int idE, String nombre, String puesto, String oficina, Turno turno, int aniosExp) {
		super();
		this.idE = idE;
		this.nombre = nombre;
		this.puesto = puesto;
		this.oficina = oficina;
		this.turno = turno;
		this.aniosExp = aniosExp;
	}

	public int getIdE() { return idE; }
	public void setIdE(int idE) { this.idE = idE; }

	public String getNombre() { return nombre; }
	public void setNombre(String nombre) { this.nombre = nombre; }

	public String getPuesto() { return puesto; }
	public void setPuesto(String puesto) { this.puesto = puesto; }

	public String getOficina() { return oficina; }
	public void setOficina(String oficina) { this.oficina = oficina; }

    public Turno getTurno() { return turno; }
	public void setTurno(Turno turno) { this.turno = turno; }

	public int getAniosExp() { return aniosExp; }
	public void setAniosExp(int anosExp) { this.aniosExp = anosExp; }

	@Override
	public String toString() {
    	return "Empleado [ID=" + idE + ", Nombre=" + nombre + ", Puesto=" + puesto + ", Turno=" + turno + "]";
	}
}