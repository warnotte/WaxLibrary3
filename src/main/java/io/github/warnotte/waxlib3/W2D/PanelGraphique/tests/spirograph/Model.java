package io.github.warnotte.waxlib3.W2D.PanelGraphique.tests.spirograph;
import io.github.warnotte.waxlib3.OBJ2GUI.Annotations.GUI_CLASS;
import io.github.warnotte.waxlib3.OBJ2GUI.Annotations.GUI_FIELD_TYPE;
import io.github.warnotte.waxlib3.core.TemplatePropertyMerger.property_mode;
import io.github.warnotte.waxlib3.core.TemplatePropertyMerger.Annotations.PROPERTY_interface;


@GUI_CLASS(type=GUI_CLASS.Type.BoxLayout, BoxLayout_property=GUI_CLASS.Type_BoxLayout.Y)
public class Model {
	double divsX [] = {4.0, -2.1, 4.0, 1.0, 2.0, 4.0, 1.0, 2.0, 4.0};
	//double divsY [] = {4.0, -2.1, 4.0, 1.0, 2.0, 4.0, 1.0, 2.0, 4.0};
	
	double scalX [] = {30, 12.0, 5.0, 10, 5.0, 5.0, 10, 5.0, 5.0, 10};
	//double scalY [] = {30, 12.0, 5.0, 10, 5.0, 5.0, 10, 5.0, 5.0, 10};
	
	@GUI_FIELD_TYPE(type=GUI_FIELD_TYPE.Type.TEXTFIELD)
	int nbrIteration = 3;
	@GUI_FIELD_TYPE(type=GUI_FIELD_TYPE.Type.TEXTFIELD)
	int maxAngle = 360 * 4;

	public double[] getDivsX() {
		return divsX;
	}

	public void setDivsX(double[] divsX) {
		this.divsX = divsX;
	}


	public double[] getScalX() {
		return scalX;
	}

	public void setScalX(double[] scalX) {
		this.scalX = scalX;
	}

	@PROPERTY_interface(Operation = property_mode.PROPERTY_MERGEABLE, orderDisplay = 10)
	public int getNbrIteration() {
		return nbrIteration;
	}

	public void setNbrIteration(int nbrPogo) {
		this.nbrIteration = nbrPogo;
	}

	@PROPERTY_interface(Operation = property_mode.PROPERTY_MERGEABLE, orderDisplay = 20)
	public int getMaxAngle() {
		return maxAngle;
	}

	public void setMaxAngle(int maxAngle) {
		this.maxAngle = maxAngle;
	}
	
	
	
}
