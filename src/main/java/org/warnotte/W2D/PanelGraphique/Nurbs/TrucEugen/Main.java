package org.warnotte.W2D.PanelGraphique.Nurbs.TrucEugen;
import java.util.ArrayList;
import java.util.List;

public class Main {

	public static void main(String[] args) {
		List<Vertex> controlPoints = new ArrayList<>();
		controlPoints.add(new Vertex(0f, 0f));
		controlPoints.add(new Vertex(1f, 0.5f));
		controlPoints.add(new Vertex(1.5f, 1.5f));
		controlPoints.add(new Vertex(2f, 2f));
		controlPoints.add(new Vertex(2.5f, 1.5f));
		
		Spline s = new Spline(controlPoints);
		s.insertControlPoint(1, 0.5f);
		s.build();
		
		//System.out.println(s);
		//verif graphe in excel
		for (int i = 0; i < s.getCurve().size(); i++) {
			System.out.println(s.getCurve().get(i).getX() + "; " + s.getCurve().get(i).getY());
		}
		
		System.out.println("Length = " + s.getLength());
	}
}
