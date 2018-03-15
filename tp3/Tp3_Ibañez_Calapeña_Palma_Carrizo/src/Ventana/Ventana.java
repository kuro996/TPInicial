package Ventana;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;
import org.openstreetmap.gui.jmapviewer.MapPolygonImpl;
import org.openstreetmap.gui.jmapviewer.interfaces.MapMarker;
import org.openstreetmap.gui.jmapviewer.interfaces.MapPolygon;

import Grafo.Ciudad;
import Grafo.Grafo;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class Ventana {
	Grafo grafo;
	JMapViewer map;
	private JFrame frame;
	String[] Ciudades;
	Timer busquedaLocal;
	JComboBox<String> comboBoxSoluciones;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Ventana window = new Ventana();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("algo");
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @throws UnsupportedLookAndFeelException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws ClassNotFoundException 
	 */
	public Ventana() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		
		grafo=new Grafo();
		busquedaLocal=new Timer(50, new BusquedaLocal());
		
		Ciudades= new String[grafo.cantCiudades()+1];
		Ciudades[0]="Seleccione Ciudad";
		for(int i=0;i<grafo.cantCiudades();i++) Ciudades[i+1]=grafo.getCiudad(i).toString();
		
		map=new JMapViewer();
		JOptionPane.showMessageDialog(frame, "Para agregar ciudades se debe hacer clic en el mapa", "Aviso", 1);
		initialize();
	}


	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 800, 600);
		frame.setTitle("Viajando por el mundo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setLayout(null);
			
		
		JPanel panelSoluciones = new JPanel();
		panelSoluciones.setOpaque(false);
		panelSoluciones.setBounds(413, 378, 361, 144);
		frame.getContentPane().add(panelSoluciones);
		panelSoluciones.setLayout(null);
		
		
		JComboBox<String> comboBoxCiudadOrigen = new JComboBox<String>();
		comboBoxCiudadOrigen.setBounds(23, 39, 135, 20);
		comboBoxCiudadOrigen.setModel(new DefaultComboBoxModel<String>(Ciudades));
		panelSoluciones.add(comboBoxCiudadOrigen);
		
		JComboBox<String> comboBoxCiudades = new JComboBox<String>();
		comboBoxCiudades.setBounds(10, 24, 142, 20);
		comboBoxCiudades.setModel(new DefaultComboBoxModel<String>(Ciudades));
	

		JComboBox<String> comboBoxCiudadUno = new JComboBox<String>();
		comboBoxCiudadUno.setBounds(10, 11, 117, 23);
		comboBoxCiudadUno.setModel(new DefaultComboBoxModel<String>(Ciudades));
		
		comboBoxSoluciones = new JComboBox<String>();
		comboBoxSoluciones.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) 
			{
				camino(comboBoxSoluciones.getSelectedIndex());
			}
		});
		comboBoxSoluciones.setBounds(182, 39, 143, 20);
		comboBoxSoluciones.setVisible(false);
		panelSoluciones.add(comboBoxSoluciones);
		
		JButton btnFrenar = new JButton("Frenar");
		btnFrenar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				btnFrenar.setVisible(false);
				busquedaLocal.stop();
			}
		});
		btnFrenar.setBounds(182, 85, 143, 23);
		btnFrenar.setVisible(false);
		panelSoluciones.add(btnFrenar);
		
		JButton btnInicioSoluciones = new JButton("Dar Soluciones");
		btnInicioSoluciones.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				if(comboBoxCiudadOrigen.getSelectedIndex()!=0)
				{
				grafo.solucionGololsa((String)comboBoxCiudadOrigen.getSelectedItem());
				busquedaLocal.stop();
				comboBoxSoluciones.removeAllItems();
				comboBoxSoluciones.addItem("Solucion 1");
				comboBoxSoluciones.setSelectedIndex(0);
				camino(comboBoxSoluciones.getSelectedIndex());
				btnFrenar.setVisible(true);
				comboBoxSoluciones.setVisible(true);
				busquedaLocal.start();
				}
				else
				{
					JOptionPane.showMessageDialog(frame, "Elija una ciudad", "Error", 0);
				}
			}
			
			
		});
		btnInicioSoluciones.setBounds(23, 85, 135, 23);
		panelSoluciones.add(btnInicioSoluciones);
			
		JPanel panelEliminar = new JPanel();
		panelEliminar.setOpaque(false);
		panelEliminar.setLayout(null);
		panelEliminar.setBounds(87, 392, 162, 144);
		panelEliminar.setVisible(false);
		panelEliminar.add(comboBoxCiudades);
		
		JPanel panelDistancias = new JPanel();
		panelDistancias.setOpaque(false);
		panelDistancias.setBounds(31, 384, 269, 138);
		panelDistancias.setVisible(false);
		frame.getContentPane().add(panelDistancias);
		panelDistancias.setLayout(null);
		panelDistancias.add(comboBoxCiudadUno);
		
		JComboBox<String> comboBoxCiudadDos = new JComboBox<String>();
		comboBoxCiudadDos.setBounds(142, 11, 117, 23);
		panelDistancias.add(comboBoxCiudadDos);
		JTextField textFieldDistancia = new JTextField();
		textFieldDistancia.setText("Distancia");
		textFieldDistancia.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent arg0) 
			{
				textFieldDistancia.setText("");
			}
			@Override
			public void focusLost(FocusEvent arg0) 
			{
				if(textFieldDistancia.getText().equals(""))textFieldDistancia.setText("Distancia");
			}
		});
		textFieldDistancia.setBounds(10, 40, 244, 20);
		panelDistancias.add(textFieldDistancia);
		textFieldDistancia.setColumns(10);
		
		JButton btnDistancia = new JButton("Cambiar distancia");
		btnDistancia.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				if(busquedaLocal.isRunning()){btnFrenar.doClick();  comboBoxSoluciones.setVisible(false); map.removeAllMapPolygons();}
				if(eleccionComboBox(comboBoxCiudadUno.getSelectedIndex()) && eleccionComboBox(comboBoxCiudadDos.getSelectedIndex())&&verificarDistancia(textFieldDistancia.getText()))
				grafo.editarDistancia((String)comboBoxCiudadUno.getSelectedItem(), (String)comboBoxCiudadDos.getSelectedItem(), Integer.valueOf(textFieldDistancia.getText()));
				actualizarComboCiudades();
				textFieldDistancia.setText("");
				
			}
		});
		btnDistancia.setBounds(10, 66, 244, 23);
		panelDistancias.add(btnDistancia);
		
		frame.getContentPane().add(panelEliminar);
		
		JButton btnEliminar = new JButton("Eliminar");
		btnEliminar.setBounds(10, 68, 142, 23);
		panelEliminar.add(btnEliminar);
		
		btnEliminar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0)
			{
				if(busquedaLocal.isRunning()){btnFrenar.doClick(); comboBoxSoluciones.setVisible(false);}
				if(eleccionComboBox(comboBoxCiudades.getSelectedIndex()))
				{
					grafo.eliminarCiudad((String)comboBoxCiudades.getSelectedItem());
					actualizarComboCiudades();
					dibujarMapa();
					comboBoxCiudades.setModel(new DefaultComboBoxModel<String>(Ciudades));
					comboBoxCiudadUno.setModel(new DefaultComboBoxModel<String>(Ciudades));
					comboBoxCiudadOrigen.setModel(new DefaultComboBoxModel<String>(Ciudades));
				}
			}
		});
				
		JPanel panelEleccion = new JPanel();
		panelEleccion.setOpaque(false);
		panelEleccion.setBounds(31, 365, 281, 144);
		frame.getContentPane().add(panelEleccion);
		panelEleccion.setVisible(false);
		panelEleccion.setLayout(null);
		
		JButton button = new JButton("Atras");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				panelEleccion.setVisible(true);
				panelEliminar.setVisible(false);	
			}
		});
		button.setBounds(10, 102, 142, 23);
		panelEliminar.add(button);
		
		JButton btnAtras3 = new JButton("Atras");
		btnAtras3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				panelEleccion.setVisible(true);
				panelDistancias.setVisible(false);
			}
		});
		btnAtras3.setBounds(10, 104, 244, 23);
		panelDistancias.add(btnAtras3);
		
		JButton btnEliminarCiudad = new JButton("Eliminar ciudad");
		btnEliminarCiudad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				panelEliminar.setVisible(true);
				panelEleccion.setVisible(false);
			}
		});
		btnEliminarCiudad.setBounds(150, 50, 121, 23);
		panelEleccion.add(btnEliminarCiudad);
		
		JButton btnEditarDistancias = new JButton("Editar Distancias");
		btnEditarDistancias.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				panelEleccion.setVisible(false);
				panelDistancias.setVisible(true);
				comboBoxCiudadUno.addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent e) 
					{
						LinkedList<String>aux= grafo.vecinos((String)comboBoxCiudadUno.getSelectedItem()); 
						String[] vecinos=new String[aux.size()+1];
						vecinos[0]="Selecciona Ciudad";
						for(int i=0;i<aux.size();i++)
						{
							vecinos[i+1]=aux.get(i);
						}
						comboBoxCiudadDos.setModel(new DefaultComboBoxModel<String>(vecinos));
					}
				});
			}
		});
		btnEditarDistancias.setBounds(10, 50, 130, 23);
		panelEleccion.add(btnEditarDistancias);
		
		JPanel panelmenuCiudad = new JPanel();
		panelmenuCiudad.setOpaque(false);
		panelmenuCiudad.setBounds(42, 378, 250, 144);
		frame.getContentPane().add(panelmenuCiudad);
		panelmenuCiudad.setLayout(null);
		
		JButton btnEditarCiudades = new JButton("Editar Ciudades");
		btnEditarCiudades.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				panelEleccion.setVisible(true);
				panelmenuCiudad.setVisible(false);
			}
		});
		btnEditarCiudades.setBounds(59, 64, 132, 23);
		panelmenuCiudad.add(btnEditarCiudades);
		
		map.setBounds(10,10,774,357);
		frame.getContentPane().add(map);
		map.setZoomContolsVisible(false);
		map.setDisplayPositionByLatLon(-34.6354677878332, -58.51318359375, 10);
			map.addMouseListener(new MouseAdapter() 
			{
				@Override
				public void mouseClicked(MouseEvent arg0) 
				{	
					String e=JOptionPane.showInputDialog(null, "Ingrese Nombre de Ciudad");
					if(e!=null && !e.equals(""))
					{
						if(grafo.existeCiudad(e))
						{
							JOptionPane.showMessageDialog(frame, "Ya Existe la ciudad, Ingrese otra", "Error", 0);
						}
						else
						{
							if(busquedaLocal.isRunning()){btnFrenar.doClick();  comboBoxSoluciones.setVisible(false);}
							Coordinate p=map.getPosition(map.getMousePosition());
							grafo.agregarCiudad(e,p.getLon(),p.getLat());
							dibujarMapa();
							actualizarComboCiudades();
							comboBoxCiudades.setModel(new DefaultComboBoxModel<String>(Ciudades));
							comboBoxCiudadUno.setModel(new DefaultComboBoxModel<String>(Ciudades));
							comboBoxCiudadOrigen.setModel(new DefaultComboBoxModel<String>(Ciudades));
						}
					}
				}
			});
		
			JPanel fondo = new Panel("/imagenes/fondo.jpg");
			fondo.setBounds(0, 0, 790, 570);
			frame.getContentPane().add(fondo);
				
	}
	
	public void dibujarMapa()
	{
		map.removeAllMapMarkers();
		map.removeAllMapPolygons();
		for(int c=0;c<grafo.cantCiudades();c++)
		{
			Ciudad ciudad=grafo.getCiudad(c);
			Coordinate coord = new Coordinate(ciudad.getY(), ciudad.getX());
			MapMarker marker = new MapMarkerDot(ciudad.toString(), coord);
			map.addMapMarker(marker);
		}
	}
	
	public void actualizarComboCiudades()
	{
		Ciudades= new String[grafo.cantCiudades()+1];
		Ciudades[0]="Seleccione Ciudad";
		for(int i=0;i<grafo.cantCiudades();i++) Ciudades[i+1]=grafo.getCiudad(i).toString();
	}
	
	
	private boolean verificarDistancia(String text) {
		try
		{
		Integer i=Integer.valueOf(text);
		if(i<0){ JOptionPane.showMessageDialog(frame, "La distancia debe ser un numero positivo", "Error", 0);return false;}
		}
		catch (NumberFormatException e) 
		{
			JOptionPane.showMessageDialog(frame, "La distancia debe ser un numero", "Error", 0);
			return false;
		}
		return true;
	}
	
	private boolean eleccionComboBox(Integer eleccion) 
	{
		if(eleccion==0)
		{
			JOptionPane.showMessageDialog(frame, "Falta Elegir Una Ciudad", "Error", 0);
			return false;
		}
		
		return true;
	}
	
	private void camino(int nSolucion) 
	{
		if(nSolucion==-1) nSolucion=0; //se debio añadir este if para prevenir un error cuando se cambia de ciudad origen
		map.removeAllMapPolygons();
		ArrayList<Ciudad> aux=grafo.getSoluciones().get(nSolucion);
		ArrayList<Coordinate> lista=new ArrayList<Coordinate>();
		for(Ciudad c:aux)
		{
			lista.add(new Coordinate(c.getY(), c.getX()));
		}
		MapPolygon linea = new MapPolygonImpl(lista);
		map.addMapPolygon(linea);		
		
	}
	
	public class BusquedaLocal implements ActionListener 
	{
		public void actionPerformed(ActionEvent e) 
		{
			grafo.busquedaLocal();
			while(grafo.getSoluciones().size()>comboBoxSoluciones.getItemCount())
			{
				comboBoxSoluciones.addItem("Solucion "+(comboBoxSoluciones.getItemCount()+1));
			}
		}; 
	}
}
