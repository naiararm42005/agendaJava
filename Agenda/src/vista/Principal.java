package vista;

import java.awt.Button;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Label;
import java.awt.List;
import java.awt.Panel;
import java.awt.SystemColor;
import java.awt.TextField;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.SQLException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;

import controlador.Controlador;
import excepciones.AgendaVaciaExcepcion;
import excepciones.ExisteExcepcion;
import modelo.Amigo;
import modelo.Contacto;
import modelo.Profesional;

public class Principal {

	private JFrame frame;
	private Controlador controlador;
	/**
	 * @wbp.nonvisual location=572,659
	 */
	private final Panel panel = new Panel();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Principal window = new Principal();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Principal() {
		initialize(); // 'pintar' la agenda
		try {
			controlador = new Controlador();
			this.setLista();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	private void setLista() {
		try {
			for (String nombre : controlador.getNombres())
				lista.add(nombre);
		} catch (AgendaVaciaExcepcion e) {
			System.out.println(e);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	void setDatos(String nom) {
		try {
			Contacto c = controlador.getContacto(nom);
			nombre.setText(c.getNombre());
			nombre.setEnabled(false);
			telefono.setText(String.valueOf(c.getTelefono()));
			email.setText(c.getEmail());
			if (c instanceof Amigo) {
				fecha_nac.setText(String.valueOf(((Amigo) c).getFecha_nac()));
				empresa.setText("");
				empresa.setEnabled(false);
				tipo.setSelectedIndex(0);
			} else {
				fecha_nac.setText("");
				fecha_nac.setEnabled(false);
				empresa.setText(((Profesional) c).getEmpresa());
				tipo.setSelectedIndex(1);
			}
			tipo.setEnabled(false);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} catch (ExisteExcepcion e) {
			System.out.println(e);
		}
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		frame = new JFrame();
		frame.setBounds(100, 100, 436, 325);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		lista = new List();
		lista.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				setDatos(lista.getSelectedItem());
			}
		});
		lista.setFont(new Font("Arial", Font.BOLD, 13));
		lista.setBounds(10, 10, 136, 266);
		frame.getContentPane().add(lista);

		lNombre = new Label("Nombre: ");
		lNombre.setFont(new Font("Arial", Font.BOLD, 15));
		lNombre.setAlignment(Label.RIGHT);
		lNombre.setBounds(152, 10, 97, 22);
		frame.getContentPane().add(lNombre);

		lTelefono = new Label("Telefono: ");
		lTelefono.setFont(new Font("Arial", Font.BOLD, 15));
		lTelefono.setAlignment(Label.RIGHT);
		lTelefono.setBounds(152, 38, 97, 22);
		frame.getContentPane().add(lTelefono);

		lEmail = new Label("Email: ");
		lEmail.setFont(new Font("Arial", Font.BOLD, 15));
		lEmail.setAlignment(Label.RIGHT);
		lEmail.setBounds(152, 66, 97, 22);
		frame.getContentPane().add(lEmail);

		lFecha_nac = new Label("Fecha nac: ");
		lFecha_nac.setFont(new Font("Arial", Font.BOLD, 15));
		lFecha_nac.setAlignment(Label.RIGHT);
		lFecha_nac.setBounds(152, 94, 97, 22);
		frame.getContentPane().add(lFecha_nac);

		lEmpresa = new Label("Empresa: ");
		lEmpresa.setFont(new Font("Arial", Font.BOLD, 15));
		lEmpresa.setAlignment(Label.RIGHT);
		lEmpresa.setBounds(152, 122, 97, 22);
		frame.getContentPane().add(lEmpresa);

		nombre = new TextField();
		nombre.setFont(new Font("Arial", Font.PLAIN, 13));
		nombre.setBounds(255, 10, 154, 22);
		frame.getContentPane().add(nombre);

		telefono = new TextField();
		telefono.setFont(new Font("Arial", Font.PLAIN, 13));
		telefono.setBounds(255, 38, 154, 22);
		frame.getContentPane().add(telefono);

		email = new TextField();
		email.setFont(new Font("Arial", Font.PLAIN, 13));
		email.setBounds(255, 66, 154, 22);
		frame.getContentPane().add(email);

		fecha_nac = new TextField();
		fecha_nac.setFont(new Font("Arial", Font.PLAIN, 13));
		fecha_nac.setBounds(255, 94, 154, 22);
		frame.getContentPane().add(fecha_nac);

		empresa = new TextField();
		empresa.setFont(new Font("Arial", Font.PLAIN, 13));
		empresa.setBounds(255, 122, 154, 22);
		frame.getContentPane().add(empresa);

		tipo = new JComboBox();
		tipo.setFont(new Font("Consolas", Font.BOLD, 14));
		tipo.setModel(new DefaultComboBoxModel(new String[] { "Amigo", "Profesional" }));
		tipo.setBounds(255, 150, 154, 22);
		frame.getContentPane().add(tipo);

		lTipo = new Label("Tipo: ");
		lTipo.setFont(new Font("Arial", Font.BOLD, 15));
		lTipo.setAlignment(Label.RIGHT);
		lTipo.setBounds(152, 150, 97, 22);
		frame.getContentPane().add(lTipo);

		alta = new Button("Alta");
		alta.setFont(new Font("Arial", Font.BOLD, 13));
		alta.setBackground(SystemColor.activeCaption);
		alta.setBounds(339, 254, 70, 22);
		frame.getContentPane().add(alta);

		baja = new Button("Baja");
		baja.setFont(new Font("Arial", Font.BOLD, 13));
		baja.setBackground(new Color(255, 128, 128));
		baja.setBounds(187, 254, 70, 22);
		frame.getContentPane().add(baja);

		modificar = new Button("Modificar");
		modificar.setFont(new Font("Arial", Font.BOLD, 13));
		modificar.setBackground(Color.ORANGE);
		modificar.setBounds(263, 254, 70, 22);
		frame.getContentPane().add(modificar);
	}

	private List lista;
	private Label lNombre;
	private Label lTelefono;
	private Label lEmail;
	private Label lFecha_nac;
	private Label lEmpresa;
	private TextField nombre;
	private TextField telefono;
	private TextField email;
	private TextField fecha_nac;
	private TextField empresa;
	private JComboBox tipo;
	private Label lTipo;
	private Button alta;
	private Button baja;
	private Button modificar;

}
