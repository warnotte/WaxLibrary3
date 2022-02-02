package org.warnotte.waxlib3.waxlibswingcomponents.Dialog;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JDialog;
import javax.swing.JTextField;
import java.beans.*; //property change stuff
import java.awt.event.*;
import java.awt.Dimension;

/* 1.4 example used by DialogDemo.java. */
public class CustomDialog extends JDialog
                   implements ActionListener,
                              PropertyChangeListener {
    /**
	 * 
	 */
	private static final long	serialVersionUID	= 2077301308984118763L;
	private String typedText = null;
    private final JTextField textField;

    private final String magicWord;
    private final JOptionPane optionPane;

    private final String btnString1 = "Enter";
    private final String btnString2 = "Cancel";

    double value = 0;
	private final JCheckBox	checkbox;
	
	public boolean isChecked()
	{
		return checkbox.isSelected();
	}
	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
        this.setPreferredSize(new Dimension(180, 180));
			
	}
	/**
     * Returns null if the typed string was invalid;
     * otherwise, returns the string as the user entered it.
     */
    public String getValidatedText() {
        return typedText;
    }
    public double getValidateddouble() {
    	Double valueD = Double.parseDouble(""+value);
        return valueD;
    }
    

    /** Creates the reusable dialog. */
    public CustomDialog(JFrame aFrame, String aWord, double value) {
    	this(aFrame, aWord, value, false, false, "");
    }

    public CustomDialog(JFrame aFrame, String aWord, double value, boolean showCheckBox, boolean checkBoxValue, String checkBoxText)
	{
        super(aFrame, true);
		initialize();
      
        this.value=value;
        magicWord = aWord.toUpperCase();
        setTitle(aWord);

        textField = new JTextField(10);
        textField.setText(""+value);

        checkbox = new JCheckBox();
        if (checkBoxText!=null)
        	checkbox.setText(checkBoxText);
        
        Object[] array = {aWord, textField, new JLabel(checkBoxText), checkbox};
        
        if (showCheckBox==false)
        	array = new Object[]{aWord, textField};

        //Create an array specifying the number of dialog buttons
        //and their text.
        Object[] options = {btnString1, btnString2};

        //Create the JOptionPane.
        optionPane = new JOptionPane(array,
                                    JOptionPane.QUESTION_MESSAGE,
                                    JOptionPane.YES_OPTION,
                                    null,
                                    options,
                                    options[0]);

        //Make this dialog display it.
        setContentPane(optionPane);

        //Handle window closing correctly.
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
                @Override
				public void windowClosing(WindowEvent we) {
                /*
                 * Instead of directly closing the window,
                 * we're going to change the JOptionPane's
                 * value property.
                 */
                    optionPane.setValue(JOptionPane.CLOSED_OPTION);
            }
        });

        //Ensure the text field always gets the first focus.
        addComponentListener(new ComponentAdapter() {
            @Override
			public void componentShown(ComponentEvent ce) {
                textField.requestFocusInWindow();
            }
        });

        //Register an event handler that puts the text into the option pane.
        textField.addActionListener(this);
        textField.selectAll();

        //Register an event handler that reacts to option pane state changes.
        optionPane.addPropertyChangeListener(this);
	}
	/** This method handles events for the text field. */
    public void actionPerformed(ActionEvent e) {
        optionPane.setValue(btnString1);
    }

    /** This method reacts to state changes in the option pane. */
    public void propertyChange(PropertyChangeEvent e) {
        String prop = e.getPropertyName();

        if (isVisible()
         && (e.getSource() == optionPane)
         && (JOptionPane.VALUE_PROPERTY.equals(prop) ||
             JOptionPane.INPUT_VALUE_PROPERTY.equals(prop))) {
            Object value = optionPane.getValue();

            if (value == JOptionPane.UNINITIALIZED_VALUE) {
                //ignore reset
                return;
            }

            //Reset the JOptionPane's value.
            //If you don't do this, then if the user
            //presses the same button next time, no
            //property change event will be fired.
            optionPane.setValue(
                    JOptionPane.UNINITIALIZED_VALUE);

            if (btnString1.equals(value)) {
                    typedText = textField.getText();
                String ucText = typedText.toUpperCase();
                
                try
                {
                	Double valueD = Double.parseDouble(""+ucText);
                	this.value=valueD;
                	clearAndHide();
                }
                catch(NumberFormatException e1)
                {
                	//text was invalid
                    textField.selectAll();
                    JOptionPane.showMessageDialog(
                                    CustomDialog.this,
                                    "Sorry, \"" + typedText + "\" "
                                    + "isn't a valid response.\n"
                                    + "Please enter "
                                    + magicWord + ".",
                                    "Try again",
                                    JOptionPane.ERROR_MESSAGE);
                    typedText = null;
                    textField.requestFocusInWindow();
                }
                
            } else { //user closed dialog or clicked cancel
                typedText = null;
                clearAndHide();
            }
        }
    }

    /** This method clears the dialog and hides it. */
    public void clearAndHide() {
        textField.setText(null);
        setVisible(false);
    }
}