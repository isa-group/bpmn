package es.us.isa.bpms.ppinot.event;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Factory de los eventos Esper utilizados en el paquete es.us.isa.bpms. 
 * 
 * @author Edelia Garcia Gonzalez
 * @version 1.0
 *
 */
public class EventFactory {
	
	/**
	 * Crea un evento que indica que un dataobject fue modificado
	 * 
	 * @param processId Id del proceso en la plataforma BPM
	 * @param instanceId Id de la instancia de proceso que se esta ejecutando
	 * @param executionId Id de la ejecucion
	 * @param time Momento en que se lanzo el evento
	 * @param activityId Id de la actividad en la que se modifico el dataobject
	 * @param dataobjectId Id del dataobject modificado
	 * @param type Clase del dataobject
	 * @param collections Ids de las colecciones a las que pertenece el dataobject
	 * @param changedProperties Propiedades modificadas en el dataobject (la key del mapa es el id de la propiedad)
	 * @param changedStates Estados modificados en el dataobject (la key del mapa es el id del punto de vista desde el cual fue evaluado el dataobject)
	 */
	public static DataobjectEvent createDataobjectEvent(String processId, String instanceId, String executionId, Date time, 
			String activityId, String dataobjectId, String type, List<String> collections, Map<String, EsperProperty> changedProperties, Map<String, EsperState> changedStates) {

		return new DataobjectEvent(processId, instanceId, executionId, time, activityId, dataobjectId, type, collections, changedProperties, changedStates);
	}
	
	/**
	 * Crea un evento Esper que indica que se inicio una instancia de proceso 
	 * 
	 * @param processId Id del proceso en el diagrama
	 * @param processDefId Id del proceso en la plataforma BPM
	 * @param instanceId Id de la instancia de proceso
	 * @param time Momento en que se lanzo el evento
	 */
	public static ProcessStartEvent createProcessStartEvent(
			String processId, String processDefId, String instanceId, Date time) {
		
		return new ProcessStartEvent(processId, processDefId, instanceId, time);
	}
	
	/**
	 * Crea un evento Esper que indica que se finalizo una instancia de proceso 
	 * 
	 * @param processId Id del proceso
	 * @param instanceId Id de la instancia de proceso
	 * @param time Momento en que se lanzo el evento
	 */
	public static ProcessEndEvent createProcessEndEvent(
			String processId, String instanceId, Date time) {
		
		return new ProcessEndEvent(processId, instanceId, time);
	}
	
	/**
	 * Crea un evento Esper de inicio de actividad
	 * 
	 * @param processId Id del proceso en la plataforma BPM
	 * @param instanceId Id de la instancia de proceso que se esta ejecutando
	 * @param executionId Id de la ejecucion
	 * @param time Momento en que se lanzo el evento
	 * @param activityId Id de la actividad que lanza el evento
	 * @param activityName Nombre de la actividad
	 * @param activityType Tipo de la actividad
	 */
	public static ActivityStartEvent createActivityStartEvent(
			String processId, String instanceId, String executionId, Date time,
			String activityId, String activityName, String activityType) {
		
		return new ActivityStartEvent(processId, instanceId, executionId, time, activityId, activityName, activityType);
	}
	
	/**
	 * Crea un evento Esper de fin de actividad
	 * 
	 * @param processId Id del proceso en la plataforma BPM
	 * @param instanceId Id de la instancia de proceso que se esta ejecutando
	 * @param executionId Id de la ejecucion
	 * @param time Momento en que se lanzo el evento
	 * @param activityId Id de la actividad que lanza el evento
	 */
	public static ActivityEndEvent createActivityEndEvent(
			String processId, String instanceId, String executionId, Date time,
			String activityId) {
		
		return new ActivityEndEvent(processId, instanceId, executionId, time, activityId);
	}
	
	/**
	 * Crea un evento Esper de asignacion de usuario a una actividad
	 * 
	 * @param processId Id del proceso en la plataforma BPM
	 * @param instanceId Id de la instancia de proceso que se esta ejecutando
	 * @param executionId Id de la ejecucion
	 * @param time Momento en que se lanzo el evento
	 * @param activityId Id de la actividad que lanza el evento
	 * @param username Usuario que ejecuto la actividad
	 */
	public static UserTaskAsignedUserEvent createUserTaskAsignedUserEvent(
			String processId, String instanceId, String executionId, Date time,
			String activityId, String username) {
		
		return new UserTaskAsignedUserEvent(processId, instanceId, executionId, time, activityId, username);
	}
}
