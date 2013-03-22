package es.us.isa.bpms.dataobject.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Establece el identificador de una clase de dataobject en el 
 * <a href="../../../../javadocs-example/simple/ProcessDataobjectSpace.html">espacio de dataobjects</a> con el que esta asociada la anotacion
 * 
 * @author Edelia
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataobjectClass {
	/**
	 * Id de la clase
	 */
	String id();
	/**
	 * Referencia de la clase. Ej: simple.dataobject.SolicitudObject
	 */
	String classref();
}
