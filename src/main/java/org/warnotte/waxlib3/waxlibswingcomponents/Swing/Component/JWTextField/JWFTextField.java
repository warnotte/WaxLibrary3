package org.warnotte.waxlib3.waxlibswingcomponents.Swing.Component.JWTextField;

import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;

import javax.swing.BoxLayout;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.event.EventListenerList;
import javax.swing.text.DefaultFormatter;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;


public class JWFTextField extends JFormattedTextField implements FocusListener, ActionListener, KeyListener
{
	private static final long	serialVersionUID	= 1415344723624881936L;
	EventListenerList	ValueChangedListeners	= new EventListenerList();
	
	public JWFTextField(String DisplayFormat, String EditFormat)
	{
		setValue(0);
		initialize(DisplayFormat, EditFormat);
		
	}
	public JWFTextField(String DisplayFormat, String EditFormat, Object value)
	{
		setValue(Double.parseDouble(""+value));
		initialize(DisplayFormat, EditFormat);
	}

	public void initialize(String DisplayFormat, String EditFormat)
	{
		DecimalFormatSymbols unusualSymbols = new DecimalFormatSymbols();
		unusualSymbols.setDecimalSeparator('.');
		unusualSymbols.setGroupingSeparator(',');
		
		DecimalFormat decimalFormat = new DecimalFormat(DisplayFormat/*"0.00"*/);
		decimalFormat.setDecimalFormatSymbols(unusualSymbols);
		DecimalFormat decimalFormat2 = new DecimalFormat(EditFormat/*"0.00 metres"*/);
		decimalFormat2.setDecimalFormatSymbols(unusualSymbols);
		
		DefaultFormatter fmt = new NumberFormatter(decimalFormat);
		DefaultFormatter fmt2 = new NumberFormatter(decimalFormat2);
		
		DefaultFormatterFactory fmtFactory = new DefaultFormatterFactory();
		//fmtFactory.setNullFormatter(fmt2);
		// fmtFactory.setDefaultFormatter(fmt2);
		fmtFactory.setDisplayFormatter(fmt);
		fmtFactory.setEditFormatter(fmt2);	 
		setFormatterFactory(fmtFactory);	
		addActionListener(this);	
		addFocusListener(this);
		addKeyListener(this);
	//	valide();	
		
		
		
		//this.setFocusLostBehavior(JFormattedTextField.PERSIST);
		
	}
	
	public void actionPerformed(java.awt.event.ActionEvent e)
	{
		//valide();
		if (valide()==true)
			fireStgNeedRefresh(new ValueChangedEvent(this));
	}
	
	/**
	 * Arrive quand on clique sur un autre truc.
	 */
	public void focusGained(FocusEvent arg0)
	{
	//	System.err.println("Gain");
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				selectAll();
			}
		});
        
	}
	
	
	/**
	 * Arrive quand on clique sur ce truc et qu'il "s'active"
	 */
	public void focusLost(FocusEvent arg0)
	{
		try
		{
		//	System.err.println("VAlue1 "+this.getValue());
			this.commitEdit();
		//	System.err.println("VAlue2 "+this.getValue());
		} catch (ParseException e)
		{
			SwingUtilities.invokeLater(new Runnable()
			{
				public void run()
				{
					System.err.println("Settext to "+getValue());
					setText(""+getValue());
				}
			});
			
			e.printStackTrace();
		}
		
		//System.err.println("Lost = Valide == "+isvalide());
		if (isvalide()==true)
			if (valide()==true)
			fireStgNeedRefresh(new ValueChangedEvent(this));
	}
	
	protected boolean valide()
	{
	//	System.err.println("==> ");
		if (this.getText()==null)
			return false;
		
		try
		{
			
			Double valueM = Double.parseDouble(""+this.getText());
			setValue(valueM);
			return true;
		}
		catch(NumberFormatException e)
		{
			setValue(this.getValue());
		}
		return false;
	}
	
	protected boolean isvalide()
	{
		if (this.getText()==null)
			return false;
		
		try
		{
		//	Double valueM = new Double(""+this.getText());
		//	setValue(valueM);
			return true;
		}
		catch(NumberFormatException e)
		{
		//	setValue(this.getValue());
		}
		return false;
	}
	
	public void addValueChangedListener(ValueChangedListener listener)
	{
		ValueChangedListeners.add(ValueChangedListener.class, listener);
	}

	public void removeValueChangedListener(ValueChangedListener listener)
	{
		ValueChangedListeners.remove(ValueChangedListener.class, listener);
	}

	public void fireStgNeedRefresh(ValueChangedEvent xxxEvent)
	{
		Object[] listeners = ValueChangedListeners.getListenerList();
		// loop through each listener and pass on the event if needed
		int numListeners = listeners.length;
		for (int i = 0; i < numListeners; i += 2)
		{
			if (listeners[i] == ValueChangedListener.class)
			{
				// pass the event to the listeners event dispatch method
				((ValueChangedListener) listeners[i + 1]).ValueChanged(xxxEvent);
			}
		}
	}
	
	public static void main(String args[])
	{
		JWFTextField jw1 = new JWFTextField("0.0 mètres", "0.0", 55.2);
		JWFTextField jw2 = new JWFTextField("0.0 mètres", "0.0", 66.879);
		JWFTextField jw3 = new JWFTextField("0.0 mètres", "0.0", 32);
		JWFTextField jw4 = new JWFTextField("0.0 p", "0.0", 32.345);
		JWFTextField jw5 = new JWFTextField("0.000 p", "0.000", 32.345);
		JFrame frame = new JFrame();
		BoxLayout gridL = new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS);
		frame.setLayout(gridL);
		frame.add(jw1);
		frame.add(jw2);
		frame.add(jw3);
		frame.add(jw4);
		frame.add(jw5);
		frame.doLayout();
		frame.pack();
		frame.setVisible(true);
	}
	public void keyPressed(KeyEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}
	public void keyReleased(KeyEvent arg0)
	{
		
		if (arg0.getKeyCode()==KeyEvent.VK_ENTER) 
		{
			try
			{
				commitEdit();
			} catch (ParseException e)
			{
				e.printStackTrace();
				SwingUtilities.invokeLater(new Runnable()
				{
					public void run()
					{
						System.err.println("Settext to " + JWFTextField.this.getValue());
						JWFTextField.this.setText(""+JWFTextField.this.getValue());
						fireStgNeedRefresh(new ValueChangedEvent(this));
					}
				});
				
			}

			if ((!(isvalide())) || (!(valide())))
				return;
			fireStgNeedRefresh(new ValueChangedEvent(this));
		}
		
	}
	public void keyTyped(KeyEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}
}
