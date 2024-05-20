package modelo;

import java.util.Date;

public class Amigo extends Contacto {
	private Date fecha_nac;

	public Amigo(String nombre, String email, int telefono, Date fecha_nac) {
		super(nombre, email, telefono);
		this.fecha_nac = fecha_nac;
	}

	public Date getFecha_nac() {
		return fecha_nac;
	}

	public void setFecha_nac(Date fecha_nac) {
		this.fecha_nac = fecha_nac;
	}

	@Override
	public String toString() {
		return super.toString() + " fecha_nac=" + fecha_nac + "]";
	}

}
