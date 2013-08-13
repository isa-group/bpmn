package es.us.isa.bpms.dataobject.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Listeners en los que se invoca el metodo al que esta asociada la anotacion en el 
 * <a href="../../../../javadocs-example/simple/ProcessDataobjectSpace.html">espacio de dataobjects</a>.
 * 
 * @author Edelia
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Listeners {
	/**
	 * Lista de listeners
	 */
	Listener[] value();
}