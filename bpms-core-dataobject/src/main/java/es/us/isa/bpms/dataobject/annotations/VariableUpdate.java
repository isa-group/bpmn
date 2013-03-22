package es.us.isa.bpms.dataobject.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Actualizacion que hay que realizar a las variables de la plataforma a partir de un dataobject, en la actividad 
 * indicada, antes de la ejecucion del evento indicado; en el 
 * <a href="../../../../javadocs-example/simple/ProcessDataobjectSpace.html">espacio de dataobjects</a> con el que esta asociada la anotacion
 * 
 * @author Edelia
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface VariableUpdate {
	/**
	 * Id de la actividad
	 */
	String activityid();
	/**
	 * Tipo de evento (create, complete o assignment)
	 */
	String event();		
	/**
	 * Tipo de actualizacion (dataobject, class o collection)
	 */
	String updatetype();	
	/**
	 * Id del elemento a actualizar: dataobject, clase o coleccion, en dependencia del tipo de actualizacion indicada
	 */
	String id();
}
