package io.github.warnotte.waxlib3.waxlibswingcomponents.Swing.Component.DateChooser;


import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.UIManager;

import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.JXHyperlink;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.calendar.DateSpan;

/*
 * Created by IntelliJ IDEA.
 * User: Richard Osbaldeston
 * Date: 05-Apr-2007
 * Time: 15:30:25
 */

public class JXDateTimePicker extends JXDatePicker {

    /**
	 * 
	 */
	private static final long serialVersionUID = -5505899613574657313L;
	private TodayPanel linkPanel;

    public JXDateTimePicker() {
        this(System.currentTimeMillis());
    }

    public JXDateTimePicker(long millis) {
        super();
        setFormats(new String[] { "dd/MM/yy HH:mm:ss" });
        
        linkPanel = new TodayPanel();
    }

    @Override
	public TodayPanel getLinkPanel() {
    	if (linkPanel==null)
    		linkPanel = new TodayPanel();
        return linkPanel;
    }

    public void setLinkDate(long linkDate, String linkFormatString) {
    	super.setLinkDay(new Date(linkDate), linkFormatString);
	
	
    }

    public Date getTime() {
    	if (linkPanel==null)
    		linkPanel = new TodayPanel();
        return linkPanel.getTime();
    }

    @Override
	public Date getDate() {
        Date date = super.getDate();
        if (date != null) {
            Calendar dt = Calendar.getInstance();
            dt.setTime(date);
            Calendar tm = Calendar.getInstance();
            tm.setTime(getTime());
            dt.set(Calendar.HOUR_OF_DAY, tm.get(Calendar.HOUR_OF_DAY));
            dt.set(Calendar.MINUTE, tm.get(Calendar.MINUTE));
            dt.set(Calendar.SECOND, tm.get(Calendar.SECOND));
            return dt.getTime();
        }
        return date;
    }

    /* time spinner hack ontop of the same class in JXDatePicker */

    public class TodayPanel extends JXPanel {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1413948055533110122L;
		MessageFormat linkFormat = null;
        JSpinner spinner;

        protected TodayPanel() {
            super(new FlowLayout());
            linkFormat=new MessageFormat("Today is {0,date, dd MMMM yyyy}");//UIManager.getString("JXDatePicker.linkFormat"));
         //  setBackgroundPainter(new MattePainter<Object>(new GradientPaint(0, 0, new Color(238, 238, 238), 0, 1, Color.WHITE)));
            JXHyperlink todayLink = new JXHyperlink(new TodayAction());
            Color textColor = new Color(16, 66, 104);
            todayLink.setUnclickedColor(textColor);
            todayLink.setClickedColor(textColor);

            add(new JLabel("Time: "));
            spinner = new JSpinner(new SpinnerDateModel(new Date(), null, null, Calendar.MINUTE));
            spinner.setEditor(new JSpinner.DateEditor(spinner, "HH:mm:ss"));
            spinner.setFont(JXDateTimePicker.this.getFont());                                           // jse5.0 buggy
            (spinner.getEditor()).setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));  // insets?
            (spinner.getEditor()).setBackground(UIManager.getColor("TextField.background"));
            add(spinner);
            //pending - quick entry for times? popup JSlider? tick granularity??

            //NB re-adding today link over-extends bounds of the popup (white edges to jxmonthview)
            add(todayLink);
       }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(new Color(187, 187, 187));
            g.drawLine(0, 0, getWidth(), 0);
            g.setColor(new Color(221, 221, 221));
            g.drawLine(0, 1, getWidth(), 1);
        }

        public Date getTime() {
            return (Date) spinner.getValue();
        }

        class TodayAction extends AbstractAction {
            /**
			 * 
			 */
			private static final long serialVersionUID = -7444059993556000223L;

			TodayAction() {
			    
                super(linkFormat.format(new Object[]{getLinkDay()}));
            }

            public void actionPerformed(ActionEvent ae) {
            	
                DateSpan span = new DateSpan(getLinkDay(), getLinkDay());
                
                getMonthView().ensureDateVisible(span.getStartAsDate());
            }
        }
    }
}