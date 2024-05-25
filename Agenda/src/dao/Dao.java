package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import conexion.Conexion;
import modelo.Amigo;
import modelo.Contacto;
import modelo.Profesional;

public class Dao {
	Conexion con;

	public Dao() throws SQLException {

		con = new Conexion();
	}

	public ArrayList<String> getNombres() throws SQLException {
		Connection c = con.getConnection(); // abrimos conexion
		Statement st = c.createStatement();
		ResultSet rs = st.executeQuery("SELECT nombre FROM contactos ORDER BY nombre"); // ejecutamos y almacenamos
		ArrayList<String> nombres = new ArrayList<>();
		while (rs.next())
			nombres.add(rs.getString("nombre"));
		return nombres;
	}

	public Contacto getContacto(String nombre) throws SQLException {
		Connection c = con.getConnection(); // abrimos conexion
		PreparedStatement ps = c.prepareStatement("SELECT * FROM contactos WHERE nombre = ?");
		ps.setString(1, nombre);
		ResultSet rs = ps.executeQuery();

		if (rs.next() == true) { // en caso de que existe lo retornamos
			if (rs.getString("tipo").equals("A") && !rs.getString("tipo").isBlank()) {
				return new Amigo(rs.getString("nombre"),
						((rs.getString("email") == null || rs.getString("email").isBlank()) ? null
								: rs.getString("email")),
						rs.getInt("tel"), ((rs.getDate("fecha_nac") == null) ? null : rs.getDate("fecha_nac")));
			} else if (rs.getString("tipo").equals("P") && !rs.getString("tipo").isBlank()) {
				return new Profesional(rs.getString("nombre"),
						((rs.getString("email") == null || rs.getString("email").isBlank()) ? null
								: rs.getString("email")),
						rs.getInt("tel"), ((rs.getString("empresa") == null) ? null : rs.getString("empresa")));
			}
		}
		c.close();
		return null; // en caso de que no existe retornamos null
	}

	public boolean existe(String nombre) throws SQLException {
		Connection c = con.getConnection(); // abrimos conexion
		PreparedStatement ps = c.prepareStatement("SELECT nombre FROM contactos WHERE nombre = ?");
		ps.setString(1, nombre);
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			return true;
		} else {
			return false;
		}
	}

	public void baja(String nombre) throws SQLException {
		Connection c = con.getConnection(); // abrimos conexion
		PreparedStatement ps = c.prepareStatement("DELETE FROM contactos WHERE nombre = ?");
		ps.setString(1, nombre);
		ps.execute();
		c.close();
	}

	public void setContacto(Contacto contacto) throws SQLException {
		Connection c = con.getConnection();
		PreparedStatement ps = c.prepareStatement("INSERT INTO contactos VALUES(?, ?, ?, ?, ?, ?)");
		ps.setString(1, contacto.getNombre());
		ps.setInt(2, contacto.getTelefono());
		ps.setString(3, contacto.getEmail());
		if (contacto instanceof Amigo) {
			ps.setDate(4, ((((Amigo) contacto).getFecha_nac() == null) ? null
					: new java.sql.Date(((Amigo) contacto).getFecha_nac().getTime())));
			ps.setString(5, null);
			ps.setString(6, "A");
		} else {
			ps.setDate(4, null);
			ps.setString(5,
					((((Profesional) contacto).getEmpresa() == null)) ? null : ((Profesional) contacto).getEmpresa());
			ps.setString(6, "P");

		}
		ps.execute();
		c.close();
	}

	public void modificar(Contacto contacto) throws SQLException {
		Connection c = con.getConnection();
		PreparedStatement ps = c.prepareStatement(
				"UPDATE contactos SET tel = ?, email = ?, fecha_nac = ?, empresa = ? WHERE nombre = ?");
		ps.setInt(1, contacto.getTelefono());
		ps.setString(2, contacto.getEmail());
		if (contacto instanceof Amigo) {
			ps.setDate(3, ((((Amigo) contacto).getFecha_nac() == null) ? null
					: new java.sql.Date(((Amigo) contacto).getFecha_nac().getTime())));
			ps.setString(4, null);

		} else {
			ps.setDate(3, null);
			ps.setString(4,
					((((Profesional) contacto).getEmpresa() == null)) ? null : ((Profesional) contacto).getEmpresa());
		}
		ps.setString(5, contacto.getNombre());
		ps.execute();
		c.close();
	}

	public boolean deleteAll() throws SQLException {
		Connection c = con.getConnection(); // abrimos conexion
		Statement st = c.createStatement();
		ResultSet rs = st.executeQuery("DELETE FROM contactos");
		if (!rs.next())
			return false;

		return true;
	}

}
