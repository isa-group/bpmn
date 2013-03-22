package es.us.isa.bpms.ppinot.event;

import java.io.Serializable;

/**
 * Clase con informacion de una propiedad modificada en un dataobject.
 * 
 * @author Edelia Garcia Gonzalez
 * @version 1.0
 *
 */
public class EsperProperty implements Serializable {

	/**
	 * Para serializar
	 */
	private static final long serialVersionUID = -4590369509923807774L;
	
	/**
	 * id de la propiedad
	 * @serial
	 */
	private String propertyId;
	/**
	 * valor actual de la propiedad
	 * @serial
	 */
	private Object value;
	/**
	 * valor anterior de la propiedad
	 * @serial
	 */
	private Object oldvalue;
	/**
	 * tipo del valor de la propiedad
	 * @serial
	 */
	private String type;
	
	/**
	 * Constructor de la clase
	 * 
	 * @param propertyId Id de la propiedad
	 * @param value Valor de la propiedad
	 */
	public EsperProperty(String propertyId, Object value, Object oldvalue) {
		
		this.propertyId = propertyId;
		this.value = value;
		this.oldvalue = oldvalue;
		// obtiene el tipo del valor de la propiedad
		if (this.getValue()!=null)
			this.type = this.getValue().getClass().getName();
	}


	/**
	 * Devuelve el atributo propertyId:
	 * id de la propiedad
	 * 
	 * @return Valor del atributo
	 */
	public String getPropertyId() {
		return this.propertyId;
	}

	/**
	 * Devuelve el atributo value:
	 * valor actual de la propiedad
	 * 
	 * @return Valor del atributo
	 */
	public Object getValue() {
		return this.value;
	}

	/**
	 * Devuelve el atributo value:
	 * valor anterior de la propiedad
	 * 
	 * @return Valor del atributo
	 */
	public Object getOldvalue() {
		return this.oldvalue;
	}


	/**
	 * Devuelve el atributo type:
	 * tipo del valor de la propiedad
	 * 
	 * @return Valor del atributo
	 */
	public String getType() {
		return this.type;
	}
	
	
}
