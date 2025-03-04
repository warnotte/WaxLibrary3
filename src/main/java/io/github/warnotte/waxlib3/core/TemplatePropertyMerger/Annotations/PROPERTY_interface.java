package io.github.warnotte.waxlib3.core.TemplatePropertyMerger.Annotations;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import io.github.warnotte.waxlib3.core.TemplatePropertyMerger.property_mode;


/**
 * Utilisée pour Merger des proprietés.
 * @author Warnotte Renaud.
 *
 */
@Documented
@Retention(RUNTIME)
@Target(ElementType.METHOD)
public @interface PROPERTY_interface {
	
	public enum gui_type {DEFAULTBYRETURNTYPE, FLATSLIDER, SLIDER, TEXTFIELD, DECIMALFORMATTEDTEXTFIELD, MASKFORMATTEDTEXTFIELD, REGEXPFORMATTEDFIELD, TEXTPANE, TEXTAREA, COMBO}
	
	// L'operation utilisée en cas de multiple selection.	
	property_mode Operation() default property_mode.PROPERTY_MERGEABLE;
	
	gui_type gui_type() default gui_type.DEFAULTBYRETURNTYPE;
	
	// Pour les formatted textfield.
	String displayFormat() default "";
	String editFormat() default "";
	// Pour les Sliders
	float max() default 100;
	float min() default 0;
	float divider() default 1;
	int orderDisplay() default 9999;
	
	boolean isDisplayLabel() default true;
	
	/**
	 * Utilisé avec un type file pour specifier si on vx uniquement les repertoire.
	 * @return
	 */
	boolean isDirectoryOnly() default false;
	/**
	 * Extension si utilisation d'un type File pour specifier
	 * @return
	 */
	String extension() default "*";
	
	boolean readOnly() default false;
	
}

