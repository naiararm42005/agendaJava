package controlador;

import java.sql.SQLException;
import java.util.ArrayList;

import dao.Dao;
import excepciones.AgendaVaciaExcepcion;
import excepciones.ExisteExcepcion;
import modelo.Contacto;

public class Controlador {
	Dao dao;

	public Controlador() throws SQLException {
		dao = new Dao();
	}

	// para poder lanzar excepciones tendremos que indicarlo en la cabecera
	// del metodo usando la palabra 'throws'
	public ArrayList<String> getNombres() throws AgendaVaciaExcepcion, SQLException {
		ArrayList<String> nombres = dao.getNombres();
		if (nombres.isEmpty())
			throw new AgendaVaciaExcepcion(); // para lanzar usaremos la palabra 'throw'
		return nombres;
	}

	// en caso de que dentro de un else se corte la ejecucion de alguna manera
	// (throw, return...)
	// podemos evitar poner else
	public Contacto getContacto(String nombre) throws SQLException, ExisteExcepcion {
		Contacto c = dao.getContacto(nombre.trim());
		if (c == null) // en caso de que sea null, no se ha encontrado el contacto
			throw new ExisteExcepcion(nombre, false);
		return c; // en caso de que se encuentre lo retornamos
	}
}
