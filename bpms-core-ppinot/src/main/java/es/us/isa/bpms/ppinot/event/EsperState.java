package es.us.isa.bpms.ppinot.event;

/**
 * Clase con informacion de un estado modificado en un dataobject. Los estados pertenecen a un punto de vista desde
 * el cual es evaluado el dataobject.
 * 
 * @author Edelia Garcia Gonzalez
 * @version 1.0
 *
 */
public class EsperState {

	// nombre del punto de vista
	private String viewPointId;
	// id del estado
	private String stateId;
	
	/**
	 * Constructor de la clase
	 * 
	 * @param viewPointId Id del punto de vista desde el cual fue evaluado el dataobject
	 * @param stateId Id del estado en el que se encuentra actualmente
	 */
	public EsperState(String viewPointId, String stateId) {
		
		this.viewPointId = viewPointId;
		this.stateId = stateId;
	}


	/**
	 * Devuelve el atributo viewPointId:
	 * id del punto de vista
	 * 
	 * @return Valor del atributo
	 */
	public String getViewPointId() {
		return this.viewPointId;
	}

	/**
	 * Devuelve el atributo stateId:
	 * id del estado en el punto de vista
	 * 
	 * @return Valor del atributo
	 */
	public String getStateId() {
		return this.stateId;
	}
	
	
}
