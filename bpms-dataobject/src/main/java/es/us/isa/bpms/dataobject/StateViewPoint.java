package es.us.isa.bpms.dataobject;

import java.io.Serializable;
import java.util.List;


/**
 * Punto de vista desde el cual se evalua un dataobject para determinar el estado en que se encuentra. 
 * Los estados que le pertenecen tienen que ser excluyentes.
 *
 * @author Edelia Garcia Gonzalez
 * @version 1.0
 */
class StateViewPoint implements Serializable {
	
	/**
	 * Para serializar
	 */
	private static final long serialVersionUID = -1466866562155703305L;

	/**
	 * Id con el que se identifica el punto de vista.
	 * @serial
	 */
	private String id;

	/**
	 * Indica si los estados se evaluan desde el inicio o a partir del estado siguiente al actual.
	 * @serial
	 */
	private Boolean fromBeginning;
	
	/**
	 * Los estados excluyentes en los que puede estar un Dataobject al ser considerado desde este punto de vista
	 * en el orden en que deben ser evaluados.
	 * @serial
	 */
	private List<String> stateList;

	/**
	 * Estado actual
	 * @serial
	 */
	private String currentState;

	/**
	 * Constructor de la clse
	 * 
	 * @param id Id del punto de vista
	 * @param fromBeginning Indica si se evaluan los estados siempre desde el primero o a partir del estado actual
	 * @param statesList Estados excluyentes del punto de vista
	 */
	StateViewPoint(String id, Boolean fromBeginning, List<String> statesList) {
		super();
		this.setId(id);
		this.fromBeginning = fromBeginning;
		this.stateList = statesList;
		this.setCurrentState("");
	}
	
	/**
	 * Obtener el atributo id:
	 * Id con el que se identifica el punto de vista.
	 *
	 * @return El valor del atributo 
	 */
	String getId() {
		return this.id;
	}

	/**
	 * Actualizar el atributo id:
	 * Id con el que se identifica el punto de vista.
	 *
	 * @param id El valor del atributo
	 */
	void setId(String id) {
		this.id = id;
	}

	/**
	 * Obtener el atributo fromBeginning:
	 * Indica si los estados se evaluan desde el inicio o a partir del estado siguiente al actual.
	 *
	 * @return El valor del atributo 
	 */
	Boolean getFromBeginning() {
		return this.fromBeginning;
	}
	
	/**
	 * Obtener el valor del atributo currentState:
	 * Estado actual
	 * 
	 * @return Valor del atributo
	 */
	String getCurrentState() {
		return this.currentState;
	}

	/**
	 * Da valor al atributo currentState:
	 * Estado actual
	 * 
	 * @param currentState Valor del atributo
	 */
	void setCurrentState(String currentState) {
		this.currentState = currentState;
	}
	
	/**
	 * Obtener el atributo stateList:
	 * Lista de los estados del punto de vista.
	 *
	 * @return El valor del atributo 
	 */
	List<String> getStateList() {
		return this.stateList;
	}
}
