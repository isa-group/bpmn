package es.us.isa.bpms.dataobject.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Service tasks en las que se invoca el metodo al que esta asociada la anotacion en el 
 * <a href="../../../../javadocs-example/simple/ProcessDataobjectSpace.html">espacio de dataobjects</a>.
 * 
 * @author Edelia
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ServiceTasks {
	/**
	 * Lista de executors
	 */
	ServiceTask[] value();
}