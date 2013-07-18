package es.us.isa.bpms.dataobject.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Puntos de vista desde los que son evaluados los estados  
 * <a href="../../../../javadocs-example/simple/dataobject/SolicitudObject.html">del dataobject</a> al que esta asociada la anotacion
 * 
 * @author Edelia
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface StateViewPoints {
	/**
	 * Lista de puntos de vista
	 */
	SVPoint[] value();
}
