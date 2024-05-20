package excepciones;

public class ExisteExcepcion extends Exception {
	String nombre;
	boolean existe;

	public ExisteExcepcion(String nombre, boolean existe) {
		this.nombre = nombre;
		this.existe = existe;
	}

	@Override
	public String toString() {
		return "El contacto " + this.nombre + ((existe) ? " existe" : " no existe");
	}

}
