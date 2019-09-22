package presentacion;

import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;

public class Vista extends JFrame {

	private Controlador control;
	private Modelo modelo;

	public Vista(Modelo m) {
		modelo = m;
		initComponents();
		capturarEventos();
	}

	public Modelo getModelo() {
		return modelo;
	}

	private void initComponents() {
		String[] columnNames = { "dataType", "description", "dimensions", "name", "shape", "units"};
		dataTable = new DefaultTableModel(null, columnNames);
		table = new JTable(dataTable);
		table.setPreferredScrollableViewportSize(new Dimension(500, 70));
		scrollPane = new JScrollPane(table);
		
		btnCargar = new JButton();
		btnVerDatos = new JButton();
		btnGraficar = new JButton();
		textArea = new JTextArea();

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);

		btnCargar.setText("Cargar");
		getContentPane().add(btnCargar);
		btnCargar.setBounds(520, 120, 100, 80);
		
		btnVerDatos.setText("Ver Datos");
		getContentPane().add(btnVerDatos);
		btnVerDatos.setBounds(520, 210, 100, 80);
		
		btnGraficar.setText("Ver Grafica");
		getContentPane().add(btnGraficar);
		btnGraficar.setBounds(520, 300, 100, 80);
		
		getContentPane().add(scrollPane);
		scrollPane.setBounds(10, 10, 500, 500);
		
		getContentPane().add(textArea);
		textArea.setBounds(630, 10, 700, 500);

		pack();
	}

	public Controlador getControl() {
		if (control == null) {
			control = new Controlador(this);
		}
		return control;
	}

	public void resetTable() {
		String[] columnNames = { "dataType", "description", "dimensions", "name", "shape", "units"};
		dataTable.setDataVector(null, columnNames);
	}
	
	public DefaultTableModel getDataTable() {
		return dataTable;
	}
	
	public JTextArea getTextArea() {
		return textArea;
	}
	
	public JTable getTable() {
		return table;
	}

	private JButton btnCargar;
	private JButton btnVerDatos;
	private JButton btnGraficar;
	private JTable table;
	private JScrollPane scrollPane;
	private DefaultTableModel dataTable;
	private JTextArea textArea;

	private void capturarEventos() {
		btnCargar.addActionListener(getControl());
		btnVerDatos.addActionListener(getControl());
		btnGraficar.addActionListener(getControl());
	}
}
