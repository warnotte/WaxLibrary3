package io.github.warnotte.waxlib3.waxlibswingcomponents.Swing.Component.CurveEditor;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import io.github.warnotte.waxlib3.waxlibswingcomponents.Utils.Curve.Curve;


public class CurveFrameEditor extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	Curve curve = null;
	CurvePanel parent;
	private JPanel panel_boutons;
	private JButton btnPlay;
	//private Object	clip;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Curve cp=null;
				try {
					cp = new Curve ();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				CurveFrameEditor thisClass;
				try {
					thisClass = new CurveFrameEditor(cp, new CurvePanel());
					thisClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					thisClass.setVisible(true);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
	}

	/**
	 * This is the default constructor
	 * @param parent 
	 * @throws Exception 
	 */
	public CurveFrameEditor(Curve cp, CurvePanel parent) throws Exception {
		super();
		this.curve=cp;
		this.parent=parent;
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 * @throws Exception 
	 */
	private void initialize() throws Exception {
		this.setSize(640, 640);
		this.setContentPane(getJContentPane());
		this.setTitle("Curve Editor");
		getJContentPane().add(getPanel_boutons(), BorderLayout.NORTH);
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 * @throws Exception 
	 */
	private JPanel getJContentPane() throws Exception {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(new CurvePanel(curve, parent), BorderLayout.CENTER);
		}
		return jContentPane;
	}

	private JPanel getPanel_boutons() {
		if (panel_boutons == null) {
			panel_boutons = new JPanel();
			panel_boutons.add(getBtnPlay());
		}
		return panel_boutons;
	}
	private JButton getBtnPlay() {
		if (btnPlay == null) {
			btnPlay = new JButton("Play sound with that enveloppe");
			btnPlay.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try
					{
						playSequence();
					} catch (LineUnavailableException e1)
					{
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			});
		}
		return btnPlay;
	}

	/**
	 * 
	 */
	
	protected static final int SAMPLE_RATE = 44100;//16 * 1024;
	
	  public byte[] createSinWaveBuffer(double freq, long time) {
	       int samples = (int) ((time * SAMPLE_RATE) / 1000);
	       byte[] output = new byte[samples];
	           //
	       double period = SAMPLE_RATE / freq;
	       for (int i = 0; i < samples; i++) {
	    	   
	    	   float timeM = (float)i/(float)samples;
	    	   
	           double angle = 2.0 * Math.PI * i / period;
	           
	           float amp = curve.getValue(timeM);
	           
	         //  System.err.println(timeM+"=="+amp);
	           output[i] = (byte)(Math.sin(angle) * 127f * amp);  
	      }
	       

	       return output;
	   }
	

	protected void playSequence() throws LineUnavailableException
	{
		long time = 1000;
		
		final AudioFormat af = new AudioFormat(SAMPLE_RATE, 8, 1, true, true);
	       SourceDataLine line = AudioSystem.getSourceDataLine(af);
	       line.open(af, SAMPLE_RATE);
	       line.start();

	   //    boolean forwardNotBack = true;

	    //   for(double freq = 400; freq <= 800;)  {
	       byte [] toneBuffer = createSinWaveBuffer(440, time);
	       line.write(toneBuffer, 0, toneBuffer.length);

	       line.drain();
	       line.close();
		
	}
}
