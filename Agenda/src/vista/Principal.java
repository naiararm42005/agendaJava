package vista;

import java.awt.Button;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.Label;
import java.awt.List;
import java.awt.Panel;
import java.awt.SystemColor;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.text.ParseException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import controlador.Controlador;
import excepciones.AgendaVaciaExcepcion;
import excepciones.CampoVacioExcepcion;
import excepciones.ExisteExcepcion;
import modelo.Amigo;
import modelo.Contacto;
import modelo.Profesional;

public class Principal {

	private JFrame frame;
	private Controlador controlador;
	private FileDialog fichero;
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
					Principal window = new Principal(0);
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
	public Principal(int LOAD) {
		initialize(); // 'pintar' la agenda
		try {
			controlador = new Controlador();
			fichero = new FileDialog(this.frame, "SELECCIONAR FICHERO", LOAD);
			this.setLista();
			this.reset();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(frame, e);
		}
	}

	private void reset() {
		baja.setEnabled(false);
		modificar.setEnabled(false);
		alta.setEnabled(false);
		nombre.setText("");
		telefono.setText("");
		email.setText("");
		fecha_nac.setText("");
		empresa.setText("");
		nombre.setEnabled(false);
		telefono.setEnabled(false);
		email.setEnabled(false);
		tipoContacto(tipo.getSelectedItem());
		tipo.setEnabled(false);
		fecha_nac.setEnabled(false);
		empresa.setEnabled(false);

	}

	private void importar() {
		fichero.setVisible(true);
		try {
			controlador.importar(fichero.getDirectory() + fichero.getFile());
			this.setLista();
		} catch (NumberFormatException e1) {
			JOptionPane.showMessageDialog(frame, "El formato del telefono es erroneo.");
		} catch (FileNotFoundException e1) {
			JOptionPane.showMessageDialog(frame, "El fichero no se ha encontrado.");
		} catch (ExisteExcepcion e1) {
			JOptionPane.showMessageDialog(frame, e1);
		} catch (SQLException e1) {
			JOptionPane.showMessageDialog(frame, e1.getMessage());
		} catch (CampoVacioExcepcion e1) {
			JOptionPane.showMessageDialog(frame, e1);
		} catch (ParseException e1) {
			JOptionPane.showMessageDialog(frame, "El formato de la fecha es erroneo.");
		}

	}

	private void setLista() {
		try {
			this.lista.removeAll();
			this.lista.add("<nuevo>");
			for (String nombre : controlador.getNombres())
				lista.add(nombre);
		} catch (AgendaVaciaExcepcion e) {
			JOptionPane.showMessageDialog(frame, e);
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(frame, e.getMessage());
		}
	}

	private void setDatos(String nom) {
		try {
			Contacto c = controlador.getContacto(nom);
			nombre.setText(c.getNombre());
			nombre.setEnabled(false);
			telefono.setText(String.valueOf(c.getTelefono()));
			telefono.setEnabled(true);
			email.setText(c.getEmail());
			email.setEnabled(true);
			if (c instanceof Amigo) {
				fecha_nac.setText(String.valueOf(((Amigo) c).getFecha_nac()));
				empresa.setText("");
				empresa.setEnabled(false);
				fecha_nac.setEnabled(true);
				tipo.setSelectedIndex(0);
			} else {
				fecha_nac.setText("");
				fecha_nac.setEnabled(false);
				empresa.setText(((Profesional) c).getEmpresa());
				empresa.setEnabled(true);
				tipo.setSelectedIndex(1);
			}
			tipo.setEnabled(false);
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(frame, e.getMessage());
		} catch (ExisteExcepcion e) {
			JOptionPane.showMessageDialog(frame, e);
		}
	}

	private void baja(String nombre) {
		try {
			controlador.baja(nombre);
			this.setLista();
			this.reset();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(frame, e.getMessage());
		} catch (ExisteExcepcion e) {
			JOptionPane.showMessageDialog(frame, e);
		}
	}

	private void alta(String nombre, String telefono, String email, String fecha_nac, String empresa, String tipo) {
		try {
			controlador.setContacto(nombre, telefono, email, fecha_nac, empresa, tipo);
			this.setLista();
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(frame, "El formato del telefono es erroneo.");
		} catch (CampoVacioExcepcion e) {
			JOptionPane.showMessageDialog(frame, e);
		} catch (ParseException e) {
			JOptionPane.showMessageDialog(frame, "El formato de la fecha es erroneo.");
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(frame, e);
		} catch (ExisteExcepcion e) {
			JOptionPane.showMessageDialog(frame, e);
		}
	}

	private void tipoContacto(Object selectedItem) {

		if (selectedItem.equals("Amigo")) {
			empresa.setEnabled(false);
		} else {
			fecha_nac.setEnabled(false);
		}

	}

	private void tipoContactoAlta(Object selectedItem) {
		if (selectedItem.equals("Amigo")) {
			empresa.setEnabled(false);
			fecha_nac.setEnabled(true);

		} else {
			fecha_nac.setEnabled(false);
			empresa.setEnabled(true);

		}

	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 436, 325);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		lista = new List();
		lista.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (lista.getSelectedItem().equals("<nuevo>")) {
					nombre.setText("");
					nombre.setEnabled(true);
					telefono.setText("");
					telefono.setEnabled(true);
					email.setText("");
					email.setEnabled(true);
					fecha_nac.setText("");
					fecha_nac.setEnabled(true);
					empresa.setText("");
					empresa.setEnabled(true);
					baja.setEnabled(false);
					modificar.setEnabled(false);
					tipo.setEnabled(true);
					alta.setEnabled(true);
					tipoContacto(tipo.getSelectedItem());
				} else {
					baja.setEnabled(true);
					modificar.setEnabled(true);
					alta.setEnabled(false);
					setDatos(lista.getSelectedItem());
				}
			}
		});
		lista.setFont(new Font("Arial", Font.BOLD, 13));
		lista.setBounds(10, 10, 136, 237);
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
		tipo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tipoContactoAlta(tipo.getSelectedItem());

			}
		});
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
		alta.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				alta(nombre.getText(), telefono.getText(), email.getText(), fecha_nac.getText(), empresa.getText(),
						tipo.getSelectedItem().toString());

			}
		});
		alta.setFont(new Font("Arial", Font.BOLD, 13));
		alta.setBackground(SystemColor.activeCaption);
		alta.setBounds(340, 225, 70, 22);
		frame.getContentPane().add(alta);

		baja = new Button("Baja");
		baja.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				baja(nombre.getText());
			}
		});
		baja.setFont(new Font("Arial", Font.BOLD, 13));
		baja.setBackground(new Color(255, 128, 128));
		baja.setBounds(188, 225, 70, 22);
		frame.getContentPane().add(baja);

		modificar = new Button("Modificar");
		modificar.setFont(new Font("Arial", Font.BOLD, 13));
		modificar.setBackground(Color.ORANGE);
		modificar.setBounds(264, 225, 70, 22);
		frame.getContentPane().add(modificar);

		JMenuBar menuBar = new JMenuBar();
		menuBar.setBackground(SystemColor.activeCaption);
		frame.setJMenuBar(menuBar);

		JMenu mnNewMenu = new JMenu("Archivo");
		menuBar.add(mnNewMenu);

		JMenuItem mntmNewMenuItem = new JMenuItem("Cargar");
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				importar();
			}

		});

		mnNewMenu.add(mntmNewMenuItem);
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
