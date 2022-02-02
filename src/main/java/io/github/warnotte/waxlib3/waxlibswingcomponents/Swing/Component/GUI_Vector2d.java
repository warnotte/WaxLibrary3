/**
 * 
 */
package io.github.warnotte.waxlib3.waxlibswingcomponents.Swing.Component;

import java.awt.FlowLayout;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.vecmath.Vector2d;

/**
 * @author Warnotte Renaud
 *
 */
public class GUI_Vector2d extends JComponent
{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -6735390070946419493L;
	Vector2d value;
	private JTextField textField_X;
	private JTextField textField_Y;
	
	/**
	 * @param value
	 */
	public GUI_Vector2d(Vector2d value)
	{
		this.value = value;
		initialize();
	}
	private void initialize() {
		setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		add(getTextField_X());
		add(getTextField_Y());
	}

	/**
	 * @param value
	 */
	public void setValue(Vector2d value)
	{
		this.value = value;
		textField_X.setText(""+value.getX());
		textField_Y.setText(""+value.getY());
	}

	/**
	 * @return
	 */
	public Vector2d getValue()
	{
		return value;
	}
	private JTextField getTextField_X() {
		if (textField_X == null) {
			textField_X = new JTextField();
			textField_X.addFocusListener(new FocusAdapter() {
				@Override
				public void focusLost(FocusEvent e) {
					double v = Double.parseDouble(textField_X.getText());
					value.setX(v);
				}
			});
			textField_X.setColumns(10);
			textField_X.setText(""+value.getX());
		}
		return textField_X;
	}
	private JTextField getTextField_Y() {
		if (textField_Y == null) {
			textField_Y = new JTextField();
			textField_Y.setColumns(10);
			textField_Y.setText(""+value.getY());
			textField_Y.addFocusListener(new FocusAdapter() {
				@Override
				public void focusLost(FocusEvent e) {
					double v = Double.parseDouble(textField_Y.getText());
					value.setY(v);
				}
			});
		}
		return textField_Y;
	}
}
