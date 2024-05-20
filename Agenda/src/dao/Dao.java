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
			if (rs.getString("tipo").equals("A")) {
				return new Amigo(rs.getString("nombre"),
						((rs.getString("email") == null || rs.getString("email").isBlank()) ? null
								: rs.getString("email")),
						rs.getInt("tel"), ((rs.getDate("fecha_nac") == null) ? null : rs.getDate("fecha_nac")));
			} else {
				return new Profesional(rs.getString("nombre"),
						((rs.getString("email") == null || rs.getString("email").isBlank()) ? null
								: rs.getString("email")),
						rs.getInt("tel"), ((rs.getString("empresa") == null) ? null : rs.getString("empresa")));
			}
		}
		return null; // en caso de que no existe retornamos null
	}
}
