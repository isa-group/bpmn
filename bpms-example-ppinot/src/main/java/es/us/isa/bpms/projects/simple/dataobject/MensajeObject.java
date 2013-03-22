package es.us.isa.bpms.projects.simple.dataobject;


import java.util.HashMap;
import java.util.Map;

import es.us.isa.bpms.dataobject.Dataobject;



/**
 * Dataobject con el mensaje de error que se puede generar cuando se valida la solicitud.
 * 
 * @author Edelia Garcia Gonzalez
 * @version 1.0
 */
@SuppressWarnings("serial")
public class MensajeObject extends Dataobject {

	/**
	 * Texto del mensaje
	 * @serial
	 */
	private String texto;

	/**
	 * Constructor de la clase 
	 * 
	 */
	public MensajeObject() {
		
		super();
	}

	/**
	 * Inicializa el dataobject
	 * 
	 */
	@Override
	protected void init() {

		this.setTexto("");
	}
	
	/**
	 * Valida el dataobject
	 */
	@Override
	public String validate() {
		return null;
	}
	
	/**
	 * Devuelve el valor del atributo texto: 
	 * Texto del mensaje.
	 * 
	 * @return Valor del atributo
	 */
	public String getTexto() {
		return texto;
	}
	
	/**
	 * Da valor al atributo mensaje:
	 * Texto del mensaje
	 * 
	 * @param newValue Valor del atributo
	 */
	public void setTexto(String newValue) {
		
		this.recordChange("texto", this.getTexto(), newValue);
		this.texto = newValue;
	}
	
	/**
	 * Devuelve el valor de variables de la plataforma BPM a partir de propiedades: 
	 * mensaje
	 * 
	 * @return Mapa con el nombres de las variables y su valor
	 */
	public Map<String, Object> updateOutEnviar() {
		
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("textomensaje", this.getTexto());
		
		return map;
	}

}
