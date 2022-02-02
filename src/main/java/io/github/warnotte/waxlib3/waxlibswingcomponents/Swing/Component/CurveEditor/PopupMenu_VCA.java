package io.github.warnotte.waxlib3.waxlibswingcomponents.Swing.Component.CurveEditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import io.github.warnotte.waxlib3.waxlibswingcomponents.Dialog.DialogDivers;
import io.github.warnotte.waxlib3.waxlibswingcomponents.Utils.Curve.Curve;
import io.github.warnotte.waxlib3.waxlibswingcomponents.Utils.Curve.Vca_Preset;
import io.github.warnotte.waxlib3.waxlibswingcomponents.Utils.Curve.Vca_PresetManager;

public class PopupMenu_VCA extends JPopupMenu {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3405892937894591222L;
	private JMenu jMenu = null;
	static Vca_PresetManager manager_preset = null;
	Curve curve = null;
	CurvePanel parent;
	private JMenuItem jMenuItem_ADD_TO_FACTORY = null;
	private JMenuItem jMenuItem1_INVERT_X = null;
	private JMenuItem jMenuItem1_INVERT_Y = null;
	private JMenuItem jMenuItem1_INVERT_XY = null;
	private JMenuItem jButton_SUBDIV_AND_SMOOTH = null;
	//private final JMenu jMenuIte = null;
	private JMenuItem jButton_SHOW_FRAME = null;
	private JMenu mnType;
	private JMenuItem mntmLinear;
	private JMenuItem mntmSpline;

	/**
	 * This method initializes 
	 * @throws Exception 
	 * 
	 */
	public PopupMenu_VCA(CurvePanel parent, Curve curve) throws Exception {
		super();
		this.parent=parent;
		this.curve=curve;
		if (manager_preset == null)
			manager_preset = new Vca_PresetManager();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
        this.add(getJMenu());
        add(getMnType());
        this.add(getJMenuItem_ADD_TO_FACTORY());
        this.add(getJMenuItem1_INVERT_X());
        this.add(getJMenuItem1_INVERT_Y());
        this.add(getJMenuItem1_INVERT_XY());
        this.add(getJButton_SHOW_FRAME());
        this.add(getJButton_SUBDIV_AND_SMOOTH());
			
	}

	/**
	 * This method initializes jMenu	
	 * 	
	 * @return javax.swing.JMenu	
	 */
	private JMenu getJMenu() {
		if (jMenu == null) {
			jMenu = new JMenu();
			jMenu.setText("Factory Preset");
			
			Vector<Vca_Preset> vect = Vca_PresetManager.getVca_presets();
			for (int i = 0; i < vect.size(); i++) {
				Vca_Preset v = vect.get(i);
				jMenu.add(getJMenuItem_1(v));
			}
			
		}
		return jMenu;
	}

	private JMenuItem getJMenuItem_1(final Vca_Preset v) {
		JMenuItem jMenuItem = new JMenuItem();
		jMenuItem.setText(v.getNamed());
		jMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				curve.setData(v.getValeurs());
				parent.repaint();
			}
		});
		
		return jMenuItem;
		
	}

	/**
	 * This method initializes jMenuItem_ADD_TO_FACTORY	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getJMenuItem_ADD_TO_FACTORY() {
		if (jMenuItem_ADD_TO_FACTORY == null) {
			jMenuItem_ADD_TO_FACTORY = new JMenuItem();
			jMenuItem_ADD_TO_FACTORY.setText("Add to factory");
			jMenuItem_ADD_TO_FACTORY.addActionListener(new java.awt.event.ActionListener() {
				

				public void actionPerformed(java.awt.event.ActionEvent e) {
					float [] values = curve.getDatas();
					String named = getNameofPreset();
					manager_preset.addPreset(named, values);
					try {
						manager_preset.Save();
					} catch (IOException e1) {
						DialogDivers.Show_dialog(e1, "Pas bon dutout :(");
						e1.printStackTrace();
					}
					refreshmenu();
				}

				private String getNameofPreset() {
				//	Object[] possibilities = {"ham", "spam", "yam"};
					String s = (String)JOptionPane.showInputDialog(
					                    new JFrame(),
					                    "Complete the sentence:\n"
					                    + "\"Green eggs and...\"",
					                    "Customized Dialog",
					                    JOptionPane.PLAIN_MESSAGE,
					                    null,
					                    null,
					                    "MyEnveloppe");

					//If a string was returned, say so.
					if ((s != null) && (s.length() > 0)) {
					    setLabel("Green eggs and... " + s + "!");
					    return s;
					}

					//If you're here, the return value was null/empty.
					setLabel("Come on, finish the sentence!");
					return null;
					
				}
			});
		}
		return jMenuItem_ADD_TO_FACTORY;
	}

	protected void refreshmenu() {
		remove(jMenu);
		jMenu=null;
		initialize();
		
	}

	/**
	 * This method initializes jMenuItem1_INVERT_X	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getJMenuItem1_INVERT_X() {
		if (jMenuItem1_INVERT_X == null) {
			jMenuItem1_INVERT_X = new JMenuItem();
			jMenuItem1_INVERT_X.setText("Invert X");
			jMenuItem1_INVERT_X.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					curve.invertX();
					parent.repaint();
					
				}
			});
		}
		return jMenuItem1_INVERT_X;
	}

	/**
	 * This method initializes jMenuItem1_INVERT_Y	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getJMenuItem1_INVERT_Y() {
		if (jMenuItem1_INVERT_Y == null) {
			jMenuItem1_INVERT_Y = new JMenuItem();
			jMenuItem1_INVERT_Y.setText("Invert Y");
			jMenuItem1_INVERT_Y.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					curve.invertY();
					parent.repaint();
				}
			});
		}
		return jMenuItem1_INVERT_Y;
	}

	/**
	 * This method initializes jMenuItem1_INVERT_XY	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getJMenuItem1_INVERT_XY() {
		if (jMenuItem1_INVERT_XY == null) {
			jMenuItem1_INVERT_XY = new JMenuItem();
			jMenuItem1_INVERT_XY.setText("Invert All");
			jMenuItem1_INVERT_XY.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					curve.invertX();
					curve.invertY();
					parent.repaint();
				}
			});
		}
		return jMenuItem1_INVERT_XY;
	}

	/**
	 * This method initializes jButton_SUBDIV_AND_SMOOTH	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JMenuItem getJButton_SUBDIV_AND_SMOOTH() {
		if (jButton_SUBDIV_AND_SMOOTH == null) {
			jButton_SUBDIV_AND_SMOOTH = new JMenuItem();
			jButton_SUBDIV_AND_SMOOTH.setText("Subdiv&Smooth");
			jButton_SUBDIV_AND_SMOOTH.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					curve.SubdivAndSmooth();
					parent.repaint();
				}
			});
		}
		return jButton_SUBDIV_AND_SMOOTH;
	}

	/**
	 * This method initializes jButton_SHOW_FRAME	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JMenuItem getJButton_SHOW_FRAME() {
		if (jButton_SHOW_FRAME == null) {
			jButton_SHOW_FRAME = new JMenuItem();
			jButton_SHOW_FRAME.setText("Show Bigger Editor");
			jButton_SHOW_FRAME.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					show_editor();
				}
			});
		}
		return jButton_SHOW_FRAME;
	}

	protected void show_editor() {
		CurveFrameEditor thisClass;
		try {
			thisClass = new CurveFrameEditor(curve, parent);
			thisClass.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			thisClass.setVisible(true);
		} catch (Exception e) {
			DialogDivers.Show_dialog(e, "Some obscure error occured");
			e.printStackTrace();
		}
		
	}

	private JMenu getMnType() {
		if (mnType == null) {
			mnType = new JMenu("Type");
			mnType.add(getMntmLinear());
			mnType.add(getMntmSpline());
		}
		return mnType;
	}
	private JMenuItem getMntmLinear() {
		if (mntmLinear == null) {
			mntmLinear = new JMenuItem("Linear");
			mntmLinear.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					curve.setSpline(false);
					parent.repaint();
					
				}
			});
		}
		return mntmLinear;
	}
	private JMenuItem getMntmSpline() {
		if (mntmSpline == null) {
			mntmSpline = new JMenuItem("Spline");
			mntmSpline.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					curve.setSpline(true);
					parent.repaint();
				}
			});
		}
		return mntmSpline;
	}
}
