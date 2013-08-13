package es.us.isa.bpms.dataobject.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Actualizaciones de dataobjects a partir de variables de la plataforma en el 
 * <a href="../../../../javadocs-example/simple/ProcessDataobjectSpace.html">espacio de dataobjects</a> con que esta asociada 
 * la anotacion
 * 
 * @author Edelia
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ObjectUpdates {
	/**
	 * Lista de actualizaciones
	 */
	ObjectUpdate[] value();
}