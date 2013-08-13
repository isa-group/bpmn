package es.us.isa.bpms.dataobject.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indica que se adicione un dataobject al 
 * <a href="../../../../javadocs-example/simple/ProcessDataobjectSpace.html">espacio de dataobjects</a> cuando se inicia una instancia de proceso
 * 
 * @author Edelia
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataobjectObject {
	
	/**
	 * Id del dataobject
	 */
	String id();
	/**
	 * Id de la clase del dataobject
	 * @see DataobjectClass
	 */
	String dataobjectclassid();
}
