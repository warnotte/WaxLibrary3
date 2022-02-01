package org.warnotte.W2D.PanelGraphique.tests;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.warnotte.W2D.PanelGraphique.CurrentSelectionContext;
import javax.swing.JCheckBox;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Frame_BezierNurbsPath extends JFrame {

	private final JPanel contentPane;
	private JPanel panel;
	private JPanel panel_1;
	private VUE2D_BezierNurbsPath panel1;
	
	
	CurrentSelectionContext ctxt1 = new CurrentSelectionContext(){
		@Override
		public boolean isFiltred(Class<?> classK)
		{
			return false;
		}
		
	};
	private JCheckBox chckbx_DrawNode;
	private JCheckBox chckbx_DrawControl;
	private JCheckBox chckbxDrawCurve;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Frame_BezierNurbsPath frame = new Frame_BezierNurbsPath();
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
	public Frame_BezierNurbsPath() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		contentPane.add(getPanel(), BorderLayout.NORTH);
		contentPane.add(getPanel_1(), BorderLayout.EAST);
		contentPane.add(getPanel1(), BorderLayout.CENTER);
	}

	private JPanel getPanel() {
		if (panel == null) {
			panel = new JPanel();
		}
		return panel;
	}
	
	private JPanel getPanel_1() {
		if (panel_1 == null) {
			panel_1 = new JPanel();
			panel_1.setLayout(new GridLayout(0, 1, 0, 0));
			panel_1.add(getChckbx_DrawNode());
			panel_1.add(getChckbx_DrawControl());
			panel_1.add(getChckbxDrawCurve());
		}
		return panel_1;
	}
	
	private VUE2D_BezierNurbsPath getPanel1() {
		if (panel1 == null) {
			
			// For windowsbuilder....
			if (ctxt1==null)
			{

				 ctxt1 = new CurrentSelectionContext(){
					@Override
					public boolean isFiltred(Class<?> classK)
					{
						return false;
					}
					
				};
				panel1 = (VUE2D_BezierNurbsPath) new JPanel();
			}
			else
			{
			panel1 = new VUE2D_BezierNurbsPath(ctxt1);
			panel1.setInvertXAxis(false);
			panel1.setInvertYAxis(false);
			}
		}
		return panel1;
	}
	private JCheckBox getChckbx_DrawNode() {
		if (chckbx_DrawNode == null) {
			chckbx_DrawNode = new JCheckBox("Draw node");
			chckbx_DrawNode.setSelected(getPanel1().DrawNode);
			chckbx_DrawNode.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					getPanel1().DrawNode=chckbx_DrawNode.isSelected();
				}
			});
		}
		return chckbx_DrawNode;
	}
	private JCheckBox getChckbx_DrawControl() {
		if (chckbx_DrawControl == null) {
			chckbx_DrawControl = new JCheckBox("Draw Control");
			chckbx_DrawControl.setSelected(getPanel1().DrawControlNode);
			chckbx_DrawControl.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					getPanel1().DrawControlNode=chckbx_DrawControl.isSelected();
					
				}
			});
		}
		return chckbx_DrawControl;
	}
	private JCheckBox getChckbxDrawCurve() {
		if (chckbxDrawCurve == null) {
			chckbxDrawCurve = new JCheckBox("Draw Curve");
			chckbxDrawCurve.setSelected(getPanel1().DrawCurve);
			chckbxDrawCurve.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					getPanel1().DrawCurve=chckbxDrawCurve.isSelected();
				}
			});
		}
		return chckbxDrawCurve;
	}
}
