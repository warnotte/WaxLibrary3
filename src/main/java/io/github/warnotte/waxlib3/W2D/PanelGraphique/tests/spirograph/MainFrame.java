package io.github.warnotte.waxlib3.W2D.PanelGraphique.tests.spirograph;
import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import io.github.warnotte.waxlib3.OBJ2GUI.JWPanel;
import io.github.warnotte.waxlib3.OBJ2GUI.ParseurAnnotations;
import io.github.warnotte.waxlib3.W2D.PanelGraphique.CurrentSelectionContext;
import net.miginfocom.swing.MigLayout;

public class MainFrame extends JFrame implements TableModelListener {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel panel_vue2D;
	private JPanel panel_settings;
	TLableModel model;

	Manager manager = new Manager();
	
	CurrentSelectionContext ctxt1 = new CurrentSelectionContext(){
		@Override
		public boolean isFiltred(Class<?> classK)
		{
			return false;
		}
		
	};
	private JTable table;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		initialize();
	}
	private void initialize() {
		ctxt1.setManager(manager);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		//contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[grow][grow]", "[grow]"));
		contentPane.add(getPanel_vue2D(), "cell 0 0,grow");
		contentPane.add(getPanel_settings(), "cell 1 0,grow");
	}

	private JPanel getPanel_vue2D() {
		if (panel_vue2D == null) {
			panel_vue2D = new JPanel();
			panel_vue2D.setLayout(new BorderLayout(0, 0));
			// Hide me for windowsBuilder
			panel_vue2D.add(new VUE2D_Spiro(ctxt1), BorderLayout.CENTER);
			
		}
		return panel_vue2D;
	}
	private JPanel getPanel_settings() {
		if (panel_settings == null) {
			panel_settings = new JPanel();
			panel_settings.setLayout(new BorderLayout(0, 0));
			
			JWPanel panel;
			try {
				panel = (JWPanel) ParseurAnnotations.CreatePanelFromObject("Main configuration", manager.getModel());
				panel_settings.add(new JScrollPane(panel), BorderLayout.CENTER);
				panel_settings.add(getTable(), BorderLayout.NORTH);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			/*
			List list = new ArrayList();
			list.add(manager.getModel());
			try {
				JPanelMagique panelMagique = JPanelMagique.GenerateJPanelFromSelectionAndBindings(null, list, true, null, null, true, false);
				panelMagique.addMyEventListener(e -> {
					System.err.println("Seomthing changefd");
					manager.recompute();
					repaint();
				});
				panel_settings.add(new JScrollPane(panelMagique), BorderLayout.CENTER);
				panel_settings.add(getTable(), BorderLayout.NORTH);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			*/
			
			

		}
		return panel_settings;
	}
	
	private JTable getTable() {
		if (table == null) {
			model = new TLableModel(manager);
			table = new JTable(model);
			model.addTableModelListener(this);
		}
		return table;
	}

	@Override
	public void tableChanged(TableModelEvent e) {
		System.err.println("Table changed : "+e);
		manager.recompute();
		repaint();
	}
}
