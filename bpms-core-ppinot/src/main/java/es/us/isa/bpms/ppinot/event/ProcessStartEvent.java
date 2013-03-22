package es.us.isa.bpms.ppinot.event;

import java.util.Date;

/**
 * Evento Esper que indica que se inicio la ejecucion de una instancia de proceso 
 * 
 * @author Edelia Garcia Gonzalez
 * @version 1.0
 *
 */
public class ProcessStartEvent extends Event {

	// id del proceso en el diagrama
	private String processId;
	
	/**
	 * Constructor de la clase
	 * 
	 * @param processId Id del proceso en el diagrama
	 * @param processDefId Id del proceso en la plataforma BPM
	 * @param instanceId Id de la instancia de proceso
	 * @param time Momento en que se lanzo el evento
	 */
	public ProcessStartEvent(String processId, String processDefId, String instanceId, Date time) {

		super(processDefId, instanceId, "", time);
		
		this.processId = processId;
	}

	/**
	 * Devuelve el valor de la propiedad processId:
	 * id del proceso en el diagrama
	 * 
	 * @return Valor de la propiedad
	 */
	public String getprocessId() {
		return this.processId;
	}

}
