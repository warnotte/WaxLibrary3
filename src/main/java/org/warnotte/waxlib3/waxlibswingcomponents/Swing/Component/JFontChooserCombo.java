package org.warnotte.waxlib3.waxlibswingcomponents.Swing.Component;

import java.awt.AWTEvent;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;



public class JFontChooserCombo extends JPanel {

	static String fontNames[];
	GraphicsEnvironment ge;
	static
	{
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	    fontNames = ge.getAvailableFontFamilyNames();
	}
	Font currentFont;
	/**
	 * 
	 */
	private static final long serialVersionUID = 9093729231879874314L;
	private JComboBox<String> comboBox_fontName;
	private JTextField textField_size;
	private JComboBox<String> comboBox_FontProp;

	public JFontChooserCombo(Font currentFont)
	{
		super();
		this.currentFont=currentFont;
		setOpaque(false);
		initialize();
	}
	
	private void initialize() {
		setMaximumSize(new Dimension(640, 64));
		setLayout(new MigLayout("", "[70%,grow,fill][20%,grow]", "[grow,fill][]"));
		add(getComboBox_fontName(), "cell 0 0 2 1,grow");
		add(getComboBox_FontProp(), "cell 0 1,grow");
		add(getTextField_size(), "cell 1 1,grow");
	}

	
	public Font getValue()
	{
		return currentFont;
	}
	
	public void setValue(Font fnt)
	{
		this.currentFont=fnt;
		int idx = -1;
		// Iterate the font family names
		for (int i = 0; i < fontNames.length; i++)
		{
			if (fontNames[i].equalsIgnoreCase(currentFont.getName()))
				idx = i;
		}
		comboBox_fontName.setSelectedIndex(idx);
		textField_size.setText(""+fnt.getSize());
	}

	private JComboBox<String> getComboBox_fontName()
	{
		if (comboBox_fontName == null)
		{
			comboBox_fontName = new JComboBox<>();
			
			comboBox_fontName.setName("Font name");

			int idx = -1;
			// Iterate the font family names
			for (int i = 0; i < fontNames.length; i++)
			{
				comboBox_fontName.insertItemAt("" + fontNames[i], i);
				if (fontNames[i].equalsIgnoreCase(currentFont.getName()))
					idx = i;
			}
			comboBox_fontName.setSelectedIndex(idx);
			
			comboBox_fontName.addItemListener(new ItemListener(){

				public void itemStateChanged(ItemEvent e)
				{
					if (e.getStateChange()==ItemEvent.SELECTED)
					{
						//System.err.println("Event 1 : "+e);
						String font_name = (String) comboBox_fontName.getSelectedItem();
						currentFont = new Font(font_name, currentFont.getStyle(), currentFont.getSize());
						fireActionEvent();
					}
				}
				
			});

		}
		return comboBox_fontName;
	}
	private JTextField getTextField_size() {
		if (textField_size == null) {
			textField_size = new JTextField();
			textField_size.addFocusListener(new FocusAdapter() {
				@Override
				public void focusLost(FocusEvent e) {
					String valeur = textField_size.getText();
					int val = Integer.parseInt(""+valeur);
					currentFont = new Font(currentFont.getName(), currentFont.getStyle(), val);
					fireActionEvent();
				}
			});
			textField_size.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String valeur = textField_size.getText();
					int val = Integer.parseInt(""+valeur);
					currentFont = new Font(currentFont.getName(), currentFont.getStyle(), val);
					fireActionEvent();
				}
			});
			textField_size.setName("Font size");
			textField_size.setText(""+currentFont.getSize());
			textField_size.setColumns(10);
		}
		return textField_size;
	}
	
	HashMap<String, Integer> style = new HashMap<>();
	
	private JComboBox<String> getComboBox_FontProp() {
		if (comboBox_FontProp == null) {
			
			style.put("Bold", Font.BOLD);
			style.put("Italic", Font.ITALIC);
			style.put("Plain", Font.PLAIN);
			
			comboBox_FontProp = new JComboBox<>();
			comboBox_FontProp.setName("Font Props");
			
			comboBox_FontProp.insertItemAt("Plain", 0);
			comboBox_FontProp.insertItemAt("Italic", 1);
			comboBox_FontProp.insertItemAt("Bold", 2);
			
			comboBox_FontProp.addItemListener(new ItemListener(){

				public void itemStateChanged(ItemEvent e)
				{
					if (e.getStateChange()==ItemEvent.SELECTED)
					{
						String type = (String) comboBox_FontProp.getSelectedItem();
						int idx = style.get(type);
						currentFont = new Font(currentFont.getName(), idx, currentFont.getSize());
						fireActionEvent();
					}
				}
			});
			
			int style_current = currentFont.getStyle();
			if (style_current==Font.PLAIN)
				comboBox_FontProp.setSelectedIndex(0);
			if (style_current==Font.ITALIC)
				comboBox_FontProp.setSelectedIndex(1);
			if (style_current==Font.BOLD)
				comboBox_FontProp.setSelectedIndex(2);
				
		}
		return comboBox_FontProp;
	}
	
	public void addActionListener(ActionListener l) {
        listenerList.add(ActionListener.class,l);
    }

    /** Removes an <code>ActionListener</code>.
     *
     * @param l  the <code>ActionListener</code> to remove
     */
    public void removeActionListener(ActionListener l) {
        listenerList.remove(ActionListener.class, l);
        
    }

    /**
     * Returns an array of all the <code>ActionListener</code>s added
     * to this JComboBox with addActionListener().
     *
     * @return all of the <code>ActionListener</code>s added or an empty
     *         array if no listeners have been added
     * @since 1.4
     */
    public ActionListener[] getActionListeners() {
        return listenerList.getListeners(ActionListener.class);
    }
    
    protected void fireActionEvent() {
      //  if (!firingActionEvent) {
            // Set flag to ensure that an infinite loop is not created
      //      firingActionEvent = true;
            ActionEvent e = null;
            // Guaranteed to return a non-null array
            Object[] listeners = listenerList.getListenerList();
            long mostRecentEventTime = EventQueue.getMostRecentEventTime();
            int modifiers = 0;
            AWTEvent currentEvent = EventQueue.getCurrentEvent();
            if (currentEvent instanceof InputEvent) {
                modifiers = ((InputEvent)currentEvent).getModifiersEx();
            } else if (currentEvent instanceof ActionEvent) {
                modifiers = ((ActionEvent)currentEvent).getModifiers();
            }
            // Process the listeners last to first, notifying
            // those that are interested in this event
            for ( int i = listeners.length-2; i>=0; i-=2 ) {
                if ( listeners[i]==ActionListener.class ) {
                    // Lazily create the event:
                    if ( e == null )
                        e = new ActionEvent(this,ActionEvent.ACTION_PERFORMED,
                                            "0",
                                            mostRecentEventTime, modifiers);
                    ((ActionListener)listeners[i+1]).actionPerformed(e);
                }
            }
    //        firingActionEvent = false;
     //  }
    }
}
