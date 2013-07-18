package es.us.isa.bpms.ppinot.event;

import java.util.Date;

/**
 * Evento Esper de fin de actividad
 * 
 * @author Edelia Garcia Gonzalez
 * @version 1.0
 *
 */
public class ActivityEndEvent extends Event {

	// id de la actividad
	private String activityId;
	
	/**
	 * Constructor de la clase
	 * 
	 * @param processDefId Id del proceso en la plataforma BPM
	 * @param instanceId Id de la instancia de proceso que se esta ejecutando
	 * @param executionId Id de la ejecucion
	 * @param time Momento en que se lanzo el evento
	 * @param activityId Id de la actividad que lanza el evento
	 */
	public ActivityEndEvent(String processDefId, String instanceId, String executionId, Date time,
			String activityId) {
		super(processDefId, instanceId, executionId, time);
		
		this.activityId = activityId;
	}

	/**
	 * Devuelve el valor de la propiedad activityId:
	 * id de la actividad
	 * 
	 * @return Valor de la propiedad
	 */
	public String getActivityId() {
		return this.activityId;
	}

}
