package modelo;

public class Profesional extends Contacto {
	private String empresa;

	public Profesional(String nombre, String email, int telefono, String empresa) {
		super(nombre, email, telefono);
		this.empresa = empresa;
	}

	public String getEmpresa() {
		return empresa;
	}

	public void setEmpresa(String empresa) {
		this.empresa = empresa;
	}

	public String toString() {
		return super.toString() + " empresa=" + empresa + "]";
	}
}
