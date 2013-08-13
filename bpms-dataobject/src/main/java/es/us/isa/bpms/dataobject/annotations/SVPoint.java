package es.us.isa.bpms.dataobject.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Punto de vista desde el cual es evaluado el estado 
 * <a href="../../../../javadocs-example/simple/dataobject/SolicitudObject.html">del dataobject</a> al que esta asociada la anotacion
 * 
 * @author Edelia
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SVPoint {
	/**
	 * Id del punto de vista
	 */
	String id();
	/**
	 * Si los estados son evaluados siempre desde el inicio o desde el estado en que se encuentra el dataobject
	 */
	String fromBeginning();
	/**
	 * Lista de identificadores de los estados excluyentes del punto de vista, en el orden que seran evaluados 
	 */
	String[] states();
}
