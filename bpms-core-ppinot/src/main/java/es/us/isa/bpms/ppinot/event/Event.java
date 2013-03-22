package es.us.isa.bpms.ppinot.event;

import java.util.Date;

/**
 * Clase de la que heredan todos los eventos Esper utilizados en el paquete es.us.isa.bpms.
 * 
 * @author Edelia Garcia Gonzalez
 * @version 1.0
 *
 */
public class Event {

	// id del proceso
	private String processDefId;
	// id de la instancia de proceso en la cual se genero el evento
	private String instanceId;
	// id de la ejecucion
    private String executionId;
	// momento en que se genero el evento
	private Date time;

	/**
	 * Constructor de la clase
	 * 
	 * @param processDefId Id del proceso para la plataforma BPM
	 * @param instanceId Id de la instancia de proceso en ejecucion
	 * @param executionId Id de la ejecucion
	 * @param time Momento en que se lanzo el evento
	 */
	public Event(String processDefId, String instanceId, String executionId, Date time) {
		
		this.processDefId = processDefId;
		this.instanceId = instanceId;
		this.executionId = executionId;
		this.time = time;
	}

	/**
	 * Devuelve el atributo processId:
	 * id del proceso
	 * 
	 * @return Valor del atributo
	 */
	public String getProcessDefId() {
		return this.processDefId;
	}
	
	/**
	 * Devuelve el atributo instanceId:
	 * id de la instancia de proceso en la cual se genero el evento
	 * 
	 * @return Valor del atributo
	 */
	public String getInstanceId() {
		return this.instanceId;
	}
	
	/**
	 * Devuelve el atributo executionId:
	 * id de la ejecucion
	 * 
	 * @return Valor del atributo
	 */
	public String getExecutionId() {
		return this.executionId;
	}
	
	/**
	 * Devuelve el atributo time:
	 * momento en que se genero el evento
	 * 
	 * @return Valor del atributo
	 */
	public Date getTime() {
		return this.time;
	}
}
