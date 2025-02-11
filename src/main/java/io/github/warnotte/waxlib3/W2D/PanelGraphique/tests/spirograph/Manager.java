package io.github.warnotte.waxlib3.W2D.PanelGraphique.tests.spirograph;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class Manager {

	Model model = new Model();
	
	List<Point2D> points[];
		
	public Manager()
	{
		recompute();
	}
	
	public void recompute() {
		points = compute(model);
	}
	
	protected List<Point2D>[] compute(Model model)
	{
		
		List<Point2D> lst [] = new List [model.nbrIteration];
		for (int i = 0; i < lst.length; i++) {
			lst[i]= new ArrayList<>();
		}
		double ang = 0;
		double step = 1;
		
		while (ang < model.getMaxAngle())
		{
			double x,y, oldx=0, oldy=0;
			for (int i = 0; i < model.nbrIteration; i++) {
				x = model.scalX[i] * Math.cos(Math.toRadians(ang*model.divsX[i]))+oldx;
				y = model.scalX[i] * Math.sin(Math.toRadians(ang*model.divsX[i]))+oldy;
				oldx = x;
				oldy = y;
				lst[i].add(new Point2D.Double(x, y));
			}
			ang+=step;
		};
		return lst;
	}
	
	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
		recompute();
	}

	public List<Point2D>[] getPoints() {
		return points;
	}
	
	
	
}
