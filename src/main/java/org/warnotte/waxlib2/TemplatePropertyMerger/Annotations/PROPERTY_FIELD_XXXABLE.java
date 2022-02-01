package org.warnotte.waxlib2.TemplatePropertyMerger.Annotations;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * A mettre devant une variable de type classe pour faire une recursion (objet dans objet = panneau dans panneau)
 * @author Warnotte Renaud
 *
 */
//@Target(ElementType.FIELD)
@Documented
@Retention(RUNTIME)
@Target(ElementType.FIELD)
public @interface PROPERTY_FIELD_XXXABLE {
	
	
}

