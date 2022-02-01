package org.warnotte.waxlibswingcomponents.Swing.Component;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.ColorHighlighter;
//import org.jdesktop.swingx.decorator.Filter;
//import org.jdesktop.swingx.decorator.FilterPipeline;
import org.jdesktop.swingx.decorator.HighlightPredicate;
import org.jdesktop.swingx.decorator.Highlighter;
import org.jdesktop.swingx.decorator.HighlighterFactory;
//import org.jdesktop.swingx.decorator.PatternFilter;
import org.jdesktop.swingx.decorator.SearchPredicate;

public class DialogConfigurationJXTable extends JDialog {

	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;

	private JPanel jPanel = null;

	private JTextField jTextField_PATTERNHIGHLIGHT = null;

	private JTextField jTextField_PATTERNFILTER = null;

	private JCheckBox jCheckBox_ENABLE_PATTERNHIGHLIGHT = null;

	private JCheckBox jCheckBox_ENABLE_PATTERNFILTER = null;
	//ColorHighlighter matchHighlighter=null;

	private final JXTable table;

	private JComboBox<String> jComboBox_COLUMNS = null;

	private JButton jButton_OK = null;
	
	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
			gridBagConstraints21.gridx = 0;
			gridBagConstraints21.fill = GridBagConstraints.BOTH;
			gridBagConstraints21.gridwidth = 3;
			gridBagConstraints21.gridy = 2;
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.fill = GridBagConstraints.BOTH;
			gridBagConstraints11.gridy = 1;
			gridBagConstraints11.weightx = 1.0;
			gridBagConstraints11.gridx = 2;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.fill = GridBagConstraints.BOTH;
			gridBagConstraints3.gridy = 1;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.fill = GridBagConstraints.BOTH;
			gridBagConstraints2.gridy = 0;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.fill = GridBagConstraints.BOTH;
			gridBagConstraints1.gridy = 1;
			gridBagConstraints1.weightx = 1.0;
			gridBagConstraints1.gridx = 1;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.gridx = 1;
			jPanel = new JPanel();
			jPanel.setLayout(new GridBagLayout());
			jPanel.add(getJTextField_PATTERNHIGHLIGHT(), gridBagConstraints);
			jPanel.add(getJTextField_PATTERNFILTER(), gridBagConstraints1);
			jPanel.add(getJCheckBox_ENABLE_PATTERNHIGHLIGHT(), gridBagConstraints2);
			jPanel.add(getJCheckBox_ENABLE_PATTERNFILTER(), gridBagConstraints3);
			jPanel.add(getJComboBox_COLUMNS(), gridBagConstraints11);
			jPanel.add(getJButton_OK(), gridBagConstraints21);
		}
		return jPanel;
	}

	/**
	 * This method initializes jTextField_PATTERNHIGHLIGHT	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextField_PATTERNHIGHLIGHT() {
		if (jTextField_PATTERNHIGHLIGHT == null) {
			jTextField_PATTERNHIGHLIGHT = new JTextField("");
			jTextField_PATTERNHIGHLIGHT
			.addActionListener(new java.awt.event.ActionListener() {
				

				public void actionPerformed(java.awt.event.ActionEvent e) {
					doHighLight();
					
				}
			});
			jTextField_PATTERNHIGHLIGHT
					.addCaretListener(new javax.swing.event.CaretListener() {
						public void caretUpdate(javax.swing.event.CaretEvent e) {
							System.out.println("caretUpdate()"); // TODO Auto-generated Event stub caretUpdate()
							doHighLight();
							
						}
					});
		
		}
		return jTextField_PATTERNHIGHLIGHT;
	}

	

	/**
	 * This method initializes jTextField_PATTERNFILTER	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextField_PATTERNFILTER() {
		if (jTextField_PATTERNFILTER == null) {
			jTextField_PATTERNFILTER = new JTextField("");
			jTextField_PATTERNFILTER.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					doFilter();
				
				     
				}
			});
			jTextField_PATTERNFILTER
					.addCaretListener(new javax.swing.event.CaretListener() {
						public void caretUpdate(javax.swing.event.CaretEvent e) {
							doFilter();
						}
					});
			
		}
		return jTextField_PATTERNFILTER;
	}

	
	

	/**
	 * This method initializes jCheckBox_ENABLE_PATTERNHIGHLIGHT	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getJCheckBox_ENABLE_PATTERNHIGHLIGHT() {
		if (jCheckBox_ENABLE_PATTERNHIGHLIGHT == null) {
			jCheckBox_ENABLE_PATTERNHIGHLIGHT = new JCheckBox();
			jCheckBox_ENABLE_PATTERNHIGHLIGHT.setText("HightLight");
			jCheckBox_ENABLE_PATTERNHIGHLIGHT
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							doHighLight();
						}
					});
		}
		return jCheckBox_ENABLE_PATTERNHIGHLIGHT;
	}

	/**
	 * This method initializes jCheckBox_ENABLE_PATTERNFILTER	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getJCheckBox_ENABLE_PATTERNFILTER() {
		if (jCheckBox_ENABLE_PATTERNFILTER == null) {
			jCheckBox_ENABLE_PATTERNFILTER = new JCheckBox();
			jCheckBox_ENABLE_PATTERNFILTER.setText("Filter");
			jCheckBox_ENABLE_PATTERNFILTER
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							doFilter();
						}
					});
		}
		return jCheckBox_ENABLE_PATTERNFILTER;
	}

	/**
	 * This method initializes jComboBox_COLUMNS	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox<String> getJComboBox_COLUMNS() {
		if (jComboBox_COLUMNS == null) {
			jComboBox_COLUMNS = new JComboBox<>();
			for (int i = 0 ; i < table.getColumnCount();i++)
			{
				String name = ""+table.getColumn(i).getHeaderValue();
				jComboBox_COLUMNS.insertItemAt(name, i);
			}
			jComboBox_COLUMNS.setSelectedIndex(0);
			jComboBox_COLUMNS.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					doFilter();
				}
			});
		}
		return jComboBox_COLUMNS;
	}

	/**
	 * This method initializes jButton_OK	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton_OK() {
		if (jButton_OK == null) {
			jButton_OK = new JButton();
			jButton_OK.setText("OK");
			jButton_OK.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					setVisible(false);
				}
			});
		}
		return jButton_OK;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	
	}

	public DialogConfigurationJXTable(JFrame frame, JXTable table) {
		
		super(frame);
		this.table= table;
		this.setModal(true);
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(300,100);
		this.setContentPane(getJContentPane());
		this.setTitle("Configuration de la table "+table);
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getJPanel(), BorderLayout.CENTER);
		}
		return jContentPane;
	}

	
	
	protected void doHighLight() {
		String text = jTextField_PATTERNHIGHLIGHT.getText();
		
		final Pattern PatternHighLight = Pattern.compile(text);
		 final ColorHighlighter matchHighlighter = new ColorHighlighter(HighlightPredicate.NEVER,Color.WHITE, Color.MAGENTA);
		 HighlightPredicate predicate = new SearchPredicate(PatternHighLight, -1, -1);
		 matchHighlighter.setHighlightPredicate(predicate);
		
		 if(jCheckBox_ENABLE_PATTERNHIGHLIGHT.isSelected()==true)
		 {
		 // Highlights
			 HighlighterFactory.createAlternateStriping(Color.gray, Color.WHITE);
			 table.setHighlighters(HighlighterFactory.createSimpleStriping());
			/* table.setHighlighters(
					 new Highlighter[]{AlternateRowHighlighter.genericGrey,
				new RolloverHighlighter(new Color(0,0,255,127), Color.WHITE ),
				matchHighlighter
				
				});*/
		 }
		 else
		 {
			 table.setHighlighters(new Highlighter[]{});
		 }
	    
	}
	protected void doFilter() {
		//String text = jTextField_PATTERNFILTER.getText();
	/*	final PatternFilter PatternFilt = new PatternFilter(text,0,jComboBox_COLUMNS.getSelectedIndex());
		if (jCheckBox_ENABLE_PATTERNFILTER.isSelected()==true)
		{
			table.setFilters(new FilterPipeline(new Filter [] {PatternFilt}));
		}
		else
			table.setFilters(new FilterPipeline(new Filter [] {}));
*/		System.err.println("A regarde wax ...");
		}
}
