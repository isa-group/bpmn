package es.us.isa.bpms.dataobject.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indica que el metodo al que esta asociada la anotacion en el 
 * <a href="../../../../javadocs-example/simple/ProcessDataobjectSpace.html">espacio de dataobjects</a>, se invoque 
 * en el evento indicado de la actividad indicada; 
 * 
 * @author Edelia
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Listener {
	/**
	 * Id de la actividad
	 */
	String activityid();
	/**
	 * Tipo de evento (create, complete o assignment)
	 */
	String event();		
}
