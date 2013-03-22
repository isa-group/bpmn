package es.us.isa.bpms.ppinot.event;

import java.util.Date;

/**
 * Evento Esper de inicio de actividad
 * 
 * @author Edelia Garcia Gonzalez
 * @version 1.0
 *
 */
public class ActivityStartEvent extends Event {

	// id de la actividad
	private String activityId;
	// nombre de la actividad
	private String activityName;
	// tipo de la actividad
	private String activityType;
	
	/**
	 * Constructor de la clase
	 * 
	 * @param processDefId Id del proceso en la plataforma BPM
	 * @param instanceId Id de la instancia de proceso que se esta ejecutando
	 * @param executionId Id de la ejecucion
	 * @param time Momento en que se lanzo el evento
	 * @param activityId Id de la actividad que lanza el evento
	 * @param activityName Nombre de la actividad
	 * @param activityType Tipo de la actividad
	 */
	public ActivityStartEvent(String processDefId, String instanceId, String executionId, Date time,
			String activityId, String activityName, String activityType) {
		super(processDefId, instanceId, executionId, time);
		
		this.activityId = activityId;
		this.activityName = activityName;
		this.activityType = activityType;
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

	/**
	 * Devuelve el valor de la propiedad activityName:
	 * nombre de la actividad
	 * 
	 * @return Valor de la propiedad
	 */
	public String getActivityName() {
		return this.activityName;
	}

	/**
	 * Devuelve el valor de la propiedad activityType:
	 * tipo de la actividad
	 * 
	 * @return Valor de la propiedad
	 */
	public String getActivityType() {
		return this.activityType;
	}

}
