package conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import oracle.jdbc.OracleDriver;

public class Conexion {

	public Conexion() throws SQLException {
		DriverManager.registerDriver(new OracleDriver());

	}

	public Connection getConnection() throws SQLException {
		Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521/xe", "system", "romaol2");
		return conn;

	}

}
