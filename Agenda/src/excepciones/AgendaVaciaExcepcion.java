package excepciones;

public class AgendaVaciaExcepcion extends Exception {
	@Override
	public String toString() {
		return "La agenda esta vacia.";
	}
}
