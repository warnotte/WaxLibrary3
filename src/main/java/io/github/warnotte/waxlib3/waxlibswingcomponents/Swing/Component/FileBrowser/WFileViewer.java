package io.github.warnotte.waxlib3.waxlibswingcomponents.Swing.Component.FileBrowser;

import java.awt.GridBagLayout;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;


public class WFileViewer extends JTable {

    static String[] columnNames = { "FileName", "Size", "Date" };

    private static final long serialVersionUID = 1L;
  
    Vector<File> files = new Vector<File>();
    DefaultTableModel dtm = null;
    public static DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
	
    /*
     * This is the default constructor)
     */
    public WFileViewer()
    {
	dtm = new DefaultTableModel(new Object[][]{}, columnNames);
	this.setModel(dtm);
	initialize();
	files = getFiles(".");
	setDatas(files);
	
	setShowHorizontalLines(false);
	setShowVerticalLines(false);
	
	
	getColumnModel().getColumn(0).setPreferredWidth(300);
	getColumnModel().getColumn(1).setPreferredWidth(40);
	getColumnModel().getColumn(2).setPreferredWidth(150);
    
    }
  
    Vector<File> getSelectedFiles()
    {
	Vector<File> selection = new Vector<File>();
	int [] rows = getSelectedRows();
	for (int i = 0 ; i < rows.length;i++)
	
	    selection.add(files.get(rows[i]));
	
	return selection;
    }
    
/*  public String getColumnName(int col)
    {
         return columnNames[col];
    }*/
    
    /**
     * Affiche le fichiers du repertoire courant.
     * @return 
     */
    public void refresh(String directory)
    {
	
	files = getFiles(directory);
	setDatas(files);
    }
    
    private Vector<File> getFiles(String string) {
	Vector<File> files = new Vector<File>();
	String[] children = new File(string).list();
	    if (children == null) {
	        return null;
	    } else {
	        for (int i=0; i<children.length; i++) {
	            File f =new File(string+File.separator +children[i]);
	            if (f.isDirectory()==false)
	        	files.add(f);
	        }
	    }
	    
	return files;
    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize() {
	//this.setSize(300, 200);
	this.setLayout(new GridBagLayout());
	this.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    }

    private void setDatas(Vector<File> files) {
	while(dtm.getRowCount()!=0)
	    dtm.removeRow(0);
	if (files!=null)
	for (int i = 0; i < files.size(); i++) {
	    File f = files.get(i);
	    //Object[] str = new Object[] { f.getName(), (f.length()/1000), new Date(f.lastModified()) };
	    
	    Date date = new Date(f.lastModified());
	    Object[] str = new Object[] { f.getName(), (f.length()/1000), dateFormat.format(date) };
	    
	    dtm.insertRow(i, str);
	}
    }
}
