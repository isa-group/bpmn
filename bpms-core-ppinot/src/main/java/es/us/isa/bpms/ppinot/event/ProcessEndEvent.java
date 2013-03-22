package es.us.isa.bpms.ppinot.event;

import java.util.Date;

/**
 * Evento Esper que indica que se finalizo la ejecucion de una instancia de proceso 
 * 
 * @author Edelia Garcia Gonzalez
 * @version 1.0
 *
 */
public class ProcessEndEvent  extends Event {
	
	/**
	 * Constructor de la clase
	 * 
	 * @param processDefId Id del proceso en la plataforma BPM
	 * @param instanceId Id de la instancia de proceso
	 * @param time Momento en que se lanzo el evento
	 */
	public ProcessEndEvent(String processDefId, String instanceId, Date time) {
		
		super(processDefId, instanceId, "", time);
	}

}
