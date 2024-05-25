package controlador;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Scanner;

import dao.Dao;
import excepciones.AgendaVaciaExcepcion;
import excepciones.CampoVacioExcepcion;
import excepciones.ExisteExcepcion;
import modelo.Amigo;
import modelo.Contacto;
import modelo.Profesional;

public class Controlador {
	Dao dao;

	public Controlador() throws SQLException {
		dao = new Dao();
	}

	public boolean existe(String nombre) throws SQLException {
		if (dao.existe(nombre.trim()))
			return true;
		else
			return false;
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

	public void baja(String nombre) throws SQLException, ExisteExcepcion {
		if (!this.existe(nombre))
			throw new ExisteExcepcion(nombre, false);
		dao.baja(nombre.trim());
	}

	public void setContacto(String nombre, String telefono, String email, String fecha_nac, String empresa, String tipo)
			throws CampoVacioExcepcion, NumberFormatException, ParseException, SQLException, ExisteExcepcion {
		if (nombre == null || nombre.isBlank())
			throw new CampoVacioExcepcion("nombre");
		if (this.existe(nombre))
			throw new ExisteExcepcion(nombre, true);
		if (telefono == null || telefono.isBlank())
			throw new CampoVacioExcepcion("telefono");
		if (tipo == null || tipo.isBlank())
			throw new CampoVacioExcepcion("tipo");

		if (tipo.equals("Amigo"))
			dao.setContacto(new Amigo(nombre.trim(), ((email == null || email.isBlank()) ? null : email.trim()),
					Integer.parseInt(telefono), ((fecha_nac == null || fecha_nac.isEmpty()) ? null
							: new SimpleDateFormat("yyyy-MM-dd").parse(fecha_nac))));
		else
			dao.setContacto(new Profesional(nombre.trim(), ((email == null || email.isBlank()) ? null : email.trim()),
					Integer.parseInt(telefono), ((empresa == null || empresa.isEmpty()) ? null : empresa.trim())));

	}

	public void importar(String ruta) throws FileNotFoundException, ExisteExcepcion, SQLException,
			NumberFormatException, CampoVacioExcepcion, ParseException {
		File f = new File(ruta);
		Scanner teclado;
		teclado = new Scanner(f);
		while (teclado.hasNextLine()) {
			String linea = teclado.nextLine();
			Scanner sl = new Scanner(linea);
			sl.useDelimiter("[,\\s]");

			String nombre = sl.next();
			if (this.existe(nombre)) {
				sl.close();
				throw new ExisteExcepcion(nombre, true);
			}
			String telefono = sl.next();
			String email = sl.next();
			String variable = sl.next();
			if (variable.contains("-"))
				this.setContacto(nombre, telefono, email, variable, null, "Amigo");
			else
				this.setContacto(nombre, telefono, email, null, variable, "Profesional");
		}
		teclado.close();
	}

	public void modificar(String nombre, String telefono, String email, String fecha_nac, String empresa)
			throws SQLException, ParseException {
		Contacto c = dao.getContacto(nombre);
		c.setEmail((email.isBlank()) ? null : email.trim());
		c.setTelefono(Integer.parseInt(telefono.trim()));
		if (c instanceof Amigo) {
			((Amigo) c).setFecha_nac(
					(fecha_nac.isBlank()) ? null : new SimpleDateFormat("yyyy-MM-dd").parse(fecha_nac.trim()));
		} else {
			((Profesional) c).setEmpresa((empresa.isBlank()) ? null : empresa.trim());
		}
		dao.modificar(c);
	}

	public void deleteAll() throws SQLException, AgendaVaciaExcepcion {
		if (!dao.deleteAll())
			throw new AgendaVaciaExcepcion();
	}
}
