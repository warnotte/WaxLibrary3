package io.github.warnotte.waxlib3.waxlibswingcomponents.Swing.Component;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

/**
 * Class that extends JTextField and add a clickable and tooltipable icon on left or right
 */
public class JTextFieldWithIcon extends JTextField {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -942784717592144733L;

	public static Icon			ATTENTION_icon;
	public static Icon			CORRECT_icon;
	public static Icon			ERROR_icon;
	public static Icon			INFO_icon;
	public static Icon			QUESTION_icon;
	static 
	{
		// UIManager.getIcon("OptionPane.warningIcon");
		//ATTENTION_icon = new ImageIcon(getClass().getResource("/images/icons/JTextFieldWithIcon/overlay_attention@2x.png"));
		ATTENTION_icon = new ImageIcon(JTextFieldWithIcon.class.getResource("/images/icons/JTextFieldWithIcon/overlay_attention.png"));
		CORRECT_icon = new ImageIcon(JTextFieldWithIcon.class.getResource("/images/icons/JTextFieldWithIcon/overlay_correct.png"));
		ERROR_icon = new ImageIcon(JTextFieldWithIcon.class.getResource("/images/icons/JTextFieldWithIcon/overlay_error.png"));
		INFO_icon = new ImageIcon(JTextFieldWithIcon.class.getResource("/images/icons/JTextFieldWithIcon/overlay_info.png"));
		QUESTION_icon = new ImageIcon(JTextFieldWithIcon.class.getResource("/images/icons/JTextFieldWithIcon/overlay_question.png"));
	}

	private JLabel	iconLabel;
	private boolean	iconVisible			= true;
	private boolean	iconPositionLeft	= false;

	public JTextFieldWithIcon(Icon icon, boolean isIconOnLeft, String tooltiptext) {
		setLayout(null); // Layout absolu pour contrôle précis
		this.iconPositionLeft = isIconOnLeft;
		iconLabel = new JLabel();
		iconLabel.setIcon(icon);
		iconLabel.setToolTipText(tooltiptext);
		iconLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		iconLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				onIconClick();
			}
		});

		add(iconLabel);
		adjustLayout();
	}

	@Override
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
		adjustLayout();
	}

	private void adjustLayout() {
	
		    
		if (iconLabel != null && iconLabel.getIcon() != null) {
			Icon	icon	= iconLabel.getIcon();    
			
			int		iconX	= getWidth() - icon.getIconWidth() - 5;
			int		iconY	= (getHeight() - icon.getIconHeight()) / 2;

			if (iconPositionLeft == true)
				iconX = 5;
			
			iconLabel.setBounds(iconX, iconY, icon.getIconWidth(), icon.getIconHeight());

			   
			// Ajuster le padding pour éviter que le texte passe sous l'icône
			  // setMargin(new Insets(0, 0, 0, icon.getIconWidth() + 10));
		}
	}

	protected void onIconClick() {
		System.out.println("Icône cliquée !");
	}

	public void setIconVisible(boolean visible) {
		iconVisible = visible;
		iconLabel.setVisible(visible);
		adjustLayout();
	}

	public boolean isIconVisible() {
		return iconVisible;
	}

}