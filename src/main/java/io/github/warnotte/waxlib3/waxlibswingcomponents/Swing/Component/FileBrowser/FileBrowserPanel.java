package io.github.warnotte.waxlib3.waxlibswingcomponents.Swing.Component.FileBrowser;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.tree.TreePath;

public class FileBrowserPanel extends JPanel implements ComponentListener {

    private static final long serialVersionUID = 1L;
    FileSystemModel fs =null;
    JTreeTable Tree = new JTreeTable(fs);
    WFileViewer WFV = new WFileViewer();  //  @jve:decl-index=0:visual-constraint="0,490"
    private JComboBox<File> jComboBox_DRIVELETTER;
    File[] roots = null;
    JPanel panel2 = null;
    JSplitPane jsp = null;
    
    /**
     * This is the default constructor
     */
    public FileBrowserPanel() {
	super();
	initialize();
	this.addComponentListener(this);
	
    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize() {
    	if (System.getProperty("os.name").toUpperCase().indexOf("WINDOWS") == -1) {
    		fs = new FileSystemModel("/");  //  @jve:decl-index=0:
    		}
    	else
    		fs = new FileSystemModel("C:\\");  //  @jve:decl-index=0:
	GridLayout gridLayout = new GridLayout();
	gridLayout.setRows(1);
	//GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
	this.setLayout(gridLayout);
	this.setSize(300, 200);
	
	roots = File.listRoots();
	  
	    Tree = new JTreeTable(fs);
	    Tree.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
	        public void valueChanged(javax.swing.event.TreeSelectionEvent e) {
	            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
	    	//	TreePath tp = e.getPath();
	            String path = "";
	            TreePath tp = e.getPath();
	            while (tp!=null)
	            {
	        	path = tp.getLastPathComponent().toString()+File.separator+path;
	        	tp = tp.getParentPath();
	            }
	            File f = (File) jComboBox_DRIVELETTER.getSelectedItem();
	            path = f.getAbsolutePath()+File.separator+path;;
	            WFV.refresh(path);
	    	System.err.println(""+path);
	    	setCursor(null);
	        }
	    });
	    WFV = new WFileViewer();
	    WFV.setSize(new Dimension(225, 162));
	    
	    jsp = new JSplitPane();
	    jsp.setDividerSize(3);
	    jsp.setDividerLocation(getWidth()/4);
	   // jsp.setDividerLocation(0.5);
	    jsp.setLeftComponent(getJPanel2());
	    jsp.setRightComponent(new JScrollPane(WFV));
	    this.add(jsp, BorderLayout.CENTER);
	//this.add(getJContentPane(), BorderLayout.CENTER);
	
    }

    
    

	/**
	 * This method initializes jComboBox_DRIVELETTER	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox<File> getJComboBox_DRIVELETTER() {
	    if (jComboBox_DRIVELETTER == null) {
		jComboBox_DRIVELETTER = new JComboBox<File>();
		jComboBox_DRIVELETTER.setMaximumSize(new Dimension(1024,32));
		for (int i = 0 ; i < roots.length;i++)
		{
			File f = roots[i];
		    
		    jComboBox_DRIVELETTER.insertItemAt(f,i);
		    
		}
		jComboBox_DRIVELETTER.setSelectedItem(roots[0]);
		jComboBox_DRIVELETTER.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
			    File f = (File) jComboBox_DRIVELETTER.getSelectedItem();
			    fs =new FileSystemModel(f.getAbsolutePath());
			    Tree.setModel(fs);
			    WFV.refresh(f.getAbsolutePath());
			//    treeTable = new JTreeTable(fs);
			//    jPanel_CENTRAL.add(new JScrollPane(treeTable), BorderLayout.CENTER);
			}
		});
	    }
	    return jComboBox_DRIVELETTER;
	}
	
	JPanel getJPanel2()
	{
	    if (panel2==null)
	    {
		panel2 = new JPanel();
	    	panel2.setLayout(new BoxLayout(getJPanel2(), BoxLayout.Y_AXIS));
	    	panel2.add(getJComboBox_DRIVELETTER(), null);
	    	panel2.add(new JScrollPane(Tree), null);
	    }
	    return panel2;
	}

	//@Override
	public void componentHidden(ComponentEvent arg0) {
	    // TODO Auto-generated method stub
	    
	}

	//@Override
	public void componentMoved(ComponentEvent arg0) {
	    // TODO Auto-generated method stub
	    
	}

	//@Override
	public void componentResized(ComponentEvent arg0) {
	    
	    jsp.setDividerLocation(getWidth()/4);
	    
	}

	//@Override
	public void componentShown(ComponentEvent arg0) {
	    // TODO Auto-generated method stub
	    
	}

	
	public Vector<File> getSelectedItems() {
	    return WFV.getSelectedFiles();
	}
}
