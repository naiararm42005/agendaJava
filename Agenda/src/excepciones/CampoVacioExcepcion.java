package excepciones;

public class CampoVacioExcepcion extends Exception {
	private String nombreCampo;

	public CampoVacioExcepcion(String nombreCampo) {
		this.nombreCampo = nombreCampo;
	}

	@Override
	public String toString() {
		return "Debes introducir el campo: " + this.nombreCampo;
	}
}
