package es.us.isa.bpms.dataobject.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indica las variables de la plataforma que hay que proporcionar al metodo updateIn al que esta asociada la anotacion 
 * en <a href="../../../../javadocs-example/simple/dataobject/SolicitudObject.html">un dataobject</a>.
 * 
 * @author Edelia
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BPMVariables {
	
	/**
	 * Lista de nombres de variables de la plataforma
	 */
	String[] value();
}
