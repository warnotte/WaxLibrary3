package io.github.warnotte.waxlib3.W2D.PanelGraphique.tests;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

import io.github.warnotte.waxlib3.W2D.PanelGraphique.CurrentSelectionContext;
import io.github.warnotte.waxlib3.W2D.PanelGraphique.PanelGraphiqueBaseBase;
import io.github.warnotte.waxlib3.W2D.PanelGraphique.SelectionChangeable;
import io.github.warnotte.waxlib3.W2D.PanelGraphique.SelectionChangedEvent;
import io.github.warnotte.waxlib3.W2D.PanelGraphique.SelectionTuple;
import io.github.warnotte.waxlib3.W2D.PanelGraphique.Nurbs.BezierPath;
import io.github.warnotte.waxlib3.W2D.PanelGraphique.Nurbs.NurbsCurve;
import io.github.warnotte.waxlib3.W2D.PanelGraphique.Nurbs.NurbsPath;
import io.github.warnotte.waxlib3.W2D.PanelGraphique.Nurbs.NurbsPoint;

/*
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
*/

/***
 * 
 * Le tooltips des truc semble faire chier.
 * 
 * @author Warnotte Renaud
 *
 */
public class VUE2D_BezierNurbsPath extends PanelGraphiqueBaseBase
		implements KeyListener, MouseListener, MouseMotionListener, SelectionChangeable, Cloneable, ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4676712333053517713L;
	CurrentSelectionContext contxt;

	// Nurbs testing...

	// List<NurbsCurve> curves = new ArrayList<NurbsCurve>();
	// NurbsCurve curve2;
	// NurbsCurve curve3;

	BezierPath bpath = new BezierPath();

	NurbsPath npath = new NurbsPath();
	//private final int MBUTTON_ACTION_MASK;

	// Ptet a deplacer vers le basebase
	boolean DrawNode = true;
	boolean DrawControlNode = true;
	boolean DrawCurve = true;

	/**
	 * @param contxt
	 */
	public VUE2D_BezierNurbsPath(CurrentSelectionContext contxt) {
		super(contxt);
		setDrawFPSInfos(false);

	//	MBUTTON_ACTION_MASK = InputEvent.BUTTON1_MASK;
	//	MBUTTON_SELECTION_MASK = InputEvent.BUTTON3_MASK;

		config.setDrawGrid(true);
		config.setDrawHelpLines(true);

		this.contxt = contxt;

		Zoom = 1.0;

		ScrollX = 0;
		ScrollX = 0;

		if (contxt != null)
			contxt.addXXXListener(this);

		// ScrollX = -168;
		// ScrollY = -55.5;

		// ZoomMin = 0.05;
		// ZoomMax = 2.5;

		setToolTipText("<html>" + "Roulette : Zoom<br>" + "Bt Millieu+Drag : Deplacer vue<br>"
				+ "Bt Droit : Selectionner element<br>" + "Bt Droit+Drag : Creer rectangle de selection<br>"
				+ "</html>");

		/*
		 * NurbsCurve curve1 = new NurbsCurve(); curve1.translate(25,25);
		 * curves.add(curve1);
		 * 
		 * NurbsCurve curve2 = new NurbsCurve(); NurbsCurve curve3 = new
		 * NurbsCurve();
		 * 
		 * 
		 * curve3.setPt1(curve2.getPt4());
		 * 
		 * curves.add(curve2); curves.add(curve3);
		 */

		bpath.getPoints().clear();

		bpath.getPoints().add(new NurbsPoint(-12, -16));
		bpath.getPoints().add(new NurbsPoint(-1, -15));
		bpath.getPoints().add(new NurbsPoint(-0, -7));
		bpath.getPoints().add(new NurbsPoint(7.5, -1.5));
		bpath.getPoints().add(new NurbsPoint(15, 5));
		bpath.getPoints().add(new NurbsPoint(22, 0));
		bpath.getPoints().add(new NurbsPoint(27, 7));

		NurbsPoint np1 = new NurbsPoint(100, 50);
		NurbsPoint np2 = new NurbsPoint(150, 50);
		NurbsPoint np3 = new NurbsPoint(200, 50);
		NurbsPoint np4 = new NurbsPoint(150, 00);
		NurbsPoint np5 = new NurbsPoint(150, 100);
		NurbsPoint np6 = new NurbsPoint(200, 100);

		npath.addPoint(np1);
		npath.addPoint(np2);
		npath.addPoint(np3);
		npath.addPoint(np4);
		npath.addPoint(np5);
		npath.addPoint(np6);

		npath.addCurve(np1, np2).setThicknessDetection(8.0f);
		npath.addCurve(np2, np3).setThicknessDetection(4.0f);
		npath.addCurve(np3, np6).setThicknessDetection(8.0f);
		npath.addCurve(np2, np5).setThicknessDetection(12.0f);
		npath.addCurve(np2, np4).setThicknessDetection(10.0f);

		Timer timer = new Timer(25, this);
		timer.start();

	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * WaxLibrary.W2D.PanelGraphique.PanelGraphiqueBaseBase#doPaint(java.awt
	 * .Graphics2D)
	 */

	float ang = 0;

	@Override
	public void doPaint(Graphics2D g) {

		ang += 0.5f;
		// Angle=Angle+1.0;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		float timeMS = System.currentTimeMillis() % 5000 / 5000.0f;

		{
			// Test 1 : Affichage Swing, pas de selection possible.
			/*
			 * for (int i = 0; i < curves.size(); i++) { NurbsCurve curve1 =
			 * curves.get(i); drawNurbsCurve(g, curve1, true); }
			 * 
			 * g.setColor(new Color(0,0,255,16)); // Dessin un beateau qui se
			 * prom�ne...
			 * 
			 * 
			 * for (int i = -16; i < 16; i++) { Point2D.Double posBateau =
			 * curves.get(0).interpolate(timeMS+((float)i/(float)125)/4.0); //
			 * Petit bateau Shape ship = at.createTransformedShape(new
			 * Rectangle2D.Double(posBateau.getX()-2, posBateau.getY()-2, 4,
			 * 4)); g.fill(ship); }
			 */
			// Test 2 : Bpath
			g.setColor(Color.BLACK);
			for (int i = 0; i < bpath.getPoints().size(); i++) {
				NurbsPoint np = bpath.getPoints().get(i);
				Rectangle2D rect = new Rectangle2D.Double(np.getX() - 0.5, np.getY() - 0.5, 1.0, 1.0);

				if (contxt.getSelection().contains(np) == true)
					g.setColor(Color.MAGENTA);
				else
					g.setColor(Color.BLUE);
				g.fill(at.createTransformedShape(rect));
				addToSelectableObject(new SelectionTuple<Shape, Object>(rect, np));
			}

			g.setColor(Color.BLUE);
			List<Line2D.Double> listLine1 = bpath.getLines();
			for (int i = 0; i < listLine1.size(); i++) {
				Line2D.Double line = listLine1.get(i);
				g.draw(at.createTransformedShape(line));
			}

			// #1 Passe du fond
			List<Line2D.Double> listLine = bpath.getCurveLines(64);
			for (int i = 0; i < listLine.size(); i++) {
				Line2D.Double line = listLine.get(i);
				g.setColor(Color.BLACK);
				g.setStroke(new BasicStroke(8.0f));
				g.draw(at.createTransformedShape(line));
				// g.translate(50, 50);
				// g.scale(5, 5);
				// g.draw(line);
				// g.scale(1/5f, 1/5f);
				// g.translate(-50, -50);

			}

			double sumlen = 0;

			// #2 Passe du foreground
			for (int i = 0; i < listLine.size(); i++) {
				Line2D.Double line = listLine.get(i);

				if (contxt.getSelection().contains(bpath) == true)
					g.setColor(Color.MAGENTA);
				else
					g.setColor(Color.GREEN);

				g.setStroke(new BasicStroke(5.0f));
				Shape shp2 = at.createTransformedShape(line);
				g.draw(shp2);
				// addToSelectableObject(new SelectionTuple<Shape, Object>(rect,
				// np));
				g.setStroke(new BasicStroke(1.0f));

				GeneralPath polyline = NurbsCurve.generateRectangleFromLine(line, 0.5);
				addToSelectableObject(new SelectionTuple<Shape, Object>(polyline, bpath));

			}
			// Petit bateau
			NurbsPoint np = bpath.interpolate(timeMS);
			Rectangle2D rect = new Rectangle2D.Double(np.getX() - 1.5, np.getY() - 1.5, 3.0, 3.0);
			g.setColor(Color.RED);
			g.fill(at.createTransformedShape(rect));

		}
			
			
	//	setEnableSelectionDrawDebug(true);
			
		// TEST 3 : Methode builtin, la bonne :)
		{
			
			
			
			// TODO : Boolean resizeable ...
			boolean resizeableWithZoom = false;
			
			drawNurbsPath(g, npath, DrawCurve, DrawControlNode, DrawNode, resizeableWithZoom);

			// Dessines des "voitures"
			for (int i = 0; i < npath.getCurves().size(); i++) {
				NurbsCurve curve = npath.getCurves().get(i);

				for (int j = 0; j < 5; j++) {
					
					double scale = 3.0;
					NurbsPoint peonpos = curve.interpolate((timeMS+j/5.0)%1.0);
					Shape peon = new Rectangle2D.Double((scale*-0.5) + peonpos.x, (scale*-0.5) + peonpos.y, 1*scale, 1*scale);
					g.setStroke(new BasicStroke(3.0f));
					g.setColor(Color.black);
					g.draw(at.createTransformedShape(peon));
					g.setColor(Color.ORANGE);
					g.fill(at.createTransformedShape(peon));
					g.setStroke(new BasicStroke(1.0f));
				}
			}

			// if (npath.getCurves().size()>0)
			// drawString(g, "L="+npath.getLength(),
			// (float)npath.getCurves().get(0).getPt1().x,
			// (float)npath.getCurves().get(0).getPt1().y-3);

			// TODO : Arriver qd on selectionne un Noeud rouge a selectionner
			// les control point aussi
		}

		g.setColor(Color.BLACK);
		g.drawString(String.format("Pos : %3.2f %3.2f", MouseX, MouseY), 10, 10); //$NON-NLS-1$

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * WaxLibrary.W2D.PanelGraphique.SelectionChangeable#somethingNeedRefresh
	 * (WaxLibrary.W2D.PanelGraphique.SelectionChangedEvent)
	 */
	@Override
	public void somethingNeedRefresh(SelectionChangedEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(java.awt.event.MouseEvent e) {
		super.mousePressed(e);
		repaint();
	}

	@Override
	public void mouseReleased(java.awt.event.MouseEvent e) {
		super.mouseReleased(e);
		repaint();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		repaint();
	}

	@Override
	public void mouseDragged(java.awt.event.MouseEvent e) {
		super.mouseDragged(e);

		int modif = e.getModifiers();
		// MBUTTON_SELECTION_MASK = -1;
		// if (demoNurbsTest)
		if (e.getButton()==MouseEvent.BUTTON3) {
			List<NurbsPoint> sel = (List<NurbsPoint>) getContxt().getSelection(NurbsPoint.class);

			List<NurbsCurve> listcurve = (List<NurbsCurve>) getContxt().getSelection(NurbsCurve.class);
			for (int i = 0; i < listcurve.size(); i++) {
				NurbsCurve curve = listcurve.get(i);
				if (sel.contains(curve.getPt1()) == false)
					sel.add(curve.getPt1());
				if (sel.contains(curve.getPt2()) == false)
					sel.add(curve.getPt2());
				if (sel.contains(curve.getPt3()) == false)
					sel.add(curve.getPt3());
				if (sel.contains(curve.getPt4()) == false)
					sel.add(curve.getPt4());
			}

			for (int i = 0; i < sel.size(); i++) {
				Point2D.Double pt = sel.get(i);
				pt.setLocation((float) pt.getX() - MouseDX, (float) pt.getY() - MouseDY);
			}

			// break;
		}
		repaint();
	}

	@Override
	public void mouseMoved(java.awt.event.MouseEvent e) {
		super.mouseMoved(e);
		repaint();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		super.keyPressed(e);

		// TODO : TO remove after selection wax
		/*
		 * if (e.getKeyCode() == KeyEvent.VK_N) { curves.add(new NurbsCurve());
		 * repaint(); }
		 */

		if (e.getKeyCode() == KeyEvent.VK_NUMPAD1) {
			List<NurbsPoint> list = (List<NurbsPoint>) getContxt().getSelection(NurbsPoint.class);
			NurbsPoint np = list.get(0);
			NurbsPoint neonp = new NurbsPoint(MouseX, MouseY);
			bpath.addPoint(neonp);
			getContxt().clear_selection(this);
			getContxt().addObjectToSelection(neonp, false, false, this, false);
			repaint();
		}

		if ((e.getKeyCode() == KeyEvent.VK_NUMPAD7)
				|| (e.getKeyCode() == KeyEvent.VK_A)) {
			NurbsPoint neonp = new NurbsPoint(MouseX, MouseY);
			npath.addPoint(neonp);

		}
		if ((e.getKeyCode() == KeyEvent.VK_NUMPAD8)
			|| (e.getKeyCode() == KeyEvent.VK_Z)) {
			List<NurbsPoint> list = (List<NurbsPoint>) getContxt().getSelection(NurbsPoint.class);
			NurbsPoint np1 = list.get(0);
			NurbsPoint np2 = list.get(1);
			npath.addCurve(np1, np2);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	public static void main(String args[]) {

		CurrentSelectionContext ctxt1 = new CurrentSelectionContext() {
			@Override
			public boolean isFiltred(Class<?> classK) {
				return false;
			}

		};

		JFrame frame1 = new JFrame();
		frame1.setLayout(new BorderLayout());
		VUE2D_BezierNurbsPath panel1 = new VUE2D_BezierNurbsPath(ctxt1);
		panel1.invertXAxis = false;
		panel1.invertYAxis = false;
		frame1.add(panel1, BorderLayout.CENTER);
		frame1.setSize(640, 480);
		frame1.setTitle("X=false;Y=false");

		JPanel panel_north = new JPanel();
		JButton bt1 = new JButton("Bouton");
		bt1.setToolTipText("eokreopk");
		JButton bt2 = new JButton("Bouton");
		bt2.setToolTipText("eokreopk");
		JButton bt3 = new JButton("Bouton");
		bt3.setToolTipText("eokreopk");
		JButton bt4 = new JButton("Bouton");
		bt4.setToolTipText("eokreopk");
		JButton bt5 = new JButton("Bouton");
		bt5.setToolTipText("eokreopk");
		JButton bt6 = new JButton("Bouton");
		bt6.setToolTipText("eokreopk");
		panel_north.add(bt1);
		panel_north.add(bt2);
		panel_north.add(bt3);
		panel_north.add(bt4);
		panel_north.add(bt5);
		panel_north.add(bt6);

		JPanel panel_east = new JPanel();
		JTextField txt1 = new JTextField("4654");
		txt1.setToolTipText("Text Tool");
		JTextField txt2 = new JTextField("987987");
		txt2.setToolTipText("Texioj Tool");
		JTextField txt3 = new JTextField("4656546544");
		txt3.setToolTipText("Text oijoool");
		panel_east.add(txt1);
		panel_east.add(txt2);
		panel_east.add(txt3);

		frame1.add(panel_north, BorderLayout.NORTH);
		frame1.add(panel_east, BorderLayout.EAST);

		frame1.doLayout();
		frame1.setVisible(true);
		frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// frame1.setLocation(0, 0);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		repaint();

	}

	public boolean isDrawNode() {
		return DrawNode;
	}

	public void setDrawNode(boolean drawNode) {
		DrawNode = drawNode;
		repaint();
	}

	public boolean isDrawControlNode() {
		return DrawControlNode;
	}

	public void setDrawControlNode(boolean drawControlNode) {
		DrawControlNode = drawControlNode;
		repaint();
	}

}