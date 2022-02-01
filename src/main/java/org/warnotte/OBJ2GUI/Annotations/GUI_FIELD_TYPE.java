package org.warnotte.OBJ2GUI.Annotations;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

//@Target(ElementType.FIELD)
@Documented
@Retention(RUNTIME)
@Target(ElementType.FIELD)
public @interface GUI_FIELD_TYPE {
	
	/** Enum�ration des diff�rents niveaux de criticit�s. */
	public static enum Type { UNKNOWN, CHECKBOX, TEXTFIELD, SLIDER, COMBO, LIST, CALENDAR, COLOR, LISTLIKE, SETLIKE, MAPLIKE, ARRAYLIKE, PANELISABLE, FONT, VECTOR3D, METHOD_CALL, VECTOR4D, PROPERTIES, JPANEL };
	public static enum Type_SLIDER { NORMAL, FLAT, ROTATIVE };
	
	Type type() default Type.UNKNOWN;
	String tooltips() default "";
	/* Si Type Range */
	int min() default 0;
	int max() default 100;
	double divider() default 1;
	Type_SLIDER slider_type() default Type_SLIDER.NORMAL; // Uniquement si type == RANGE

	String method_name() default "";
	String jPanelLocation() default "";
	String jPanelParamDTOC() default ""; 

	boolean readonly() default false; 
	
	
}


/**
Pour l'analyseur de compatibilit�

boolean  	   :X: CHECKBOX, COMBO (2elements), LIST, ...
int, float,... :X: TEXTFIELD, SLIDER,...
String         :1: TEXTFIELD
Enum           :X: COMBO, SLIDER+LABEL, LIST,...
Color          :1: COLORChooserDeluxe
Date	       :1: CALENDARChooserDeluxe
Vector,List    :X: VECTORLIKE(LIST par exemple avec un panel pour l'objet)
[] et [][]     :1: ARRAYLIKE(JTABLE)
*/
