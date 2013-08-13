package es.us.isa.bpms.dataobject.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Identificadores de las clases de los dataobject en el 
 * <a href="../../../../javadocs-example/simple/ProcessDataobjectSpace.html">espacio de dataobjects</a> con el que esta asociada la anotacion.
 * 
 * @author Edelia
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataobjectClasses {
	/**
	 * Lista de identificadores de clases de dataobject
	 */
	DataobjectClass[] value();
}