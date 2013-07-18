package es.us.isa.bpms.ppinot.event;

import java.util.Date;

/**
 * Evento Esper de asignacion de usuario a una actividad
 * 
 * @author Edelia Garcia Gonzalez
 * @version 1.0
 *
 */
public class UserTaskAsignedUserEvent extends Event {

	// id de la actividad
	private String activityId;
	private String username;
	
	/**
	 * Constructor de la clase
	 * 
	 * @param processDefId Id del proceso en la plataforma BPM
	 * @param instanceId Id de la instancia de proceso que se esta ejecutando
	 * @param executionId Id de la ejecucion
	 * @param time Momento en que se lanzo el evento
	 * @param activityId Id de la actividad que lanza el evento
	 */
	public UserTaskAsignedUserEvent(String processDefId, String instanceId, String executionId, Date time,
			String activityId, String username) {
		super(processDefId, instanceId, executionId, time);
		
		this.activityId = activityId;
		this.username = username;
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
	 * Devuelve el valor de la propiedad username:
	 * nombre de usuario que ejecuto la actividad
	 * 
	 * @return Valor de la propiedad
	 */
	public String getUsername() {
		return this.username;
	}

}
