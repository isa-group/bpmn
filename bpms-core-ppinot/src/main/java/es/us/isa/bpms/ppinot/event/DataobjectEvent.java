package es.us.isa.bpms.ppinot.event;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Evento Esper que indica que un dataobject fue modificado
 * 
 * @author Edelia Garcia Gonzalez
 * @version 1.0
 * @see es.us.isa.bpms.ppinot.event.EsperProperty
 * @see es.us.isa.bpms.ppinot.event.EsperState
 *
 */
public class DataobjectEvent extends Event {

	// id de la actividad en la que se modifico el dataobject
	private String activityId;
	// id del Dataobject al cual se le realiza la accion.
	private String dataobjectId;
	// clase a la que pertenece el dataobject
	private String type;
	// colecciones a las que pertence el dataobject
	private List<String> collections;
	// conjunto de propiedades modificadas en el Dataobject
	private Map<String, EsperProperty> changedProperties;
	// conjunto de estados modificados en el Dataobject
	private Map<String, EsperState> changedStates;
	
	/**
	 * Constructor de la clase
	 * 
	 * @param processDefId Id del proceso en la plataforma BPM
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
	public DataobjectEvent(String processDefId, String instanceId, String executionId, Date time, 
			String activityId, String dataobjectId, String type, List<String> collections, Map<String, EsperProperty> changedProperties, Map<String, EsperState> changedStates) {
		super(processDefId, instanceId, executionId, time);
		
		this.activityId = activityId;
		this.dataobjectId = dataobjectId;
		this.type = type;
		this.collections = collections;
		this.changedProperties = changedProperties;
		this.changedStates = changedStates;
	}

	/**
	 * Devuelve el valor de la propiedad activityId:
	 * id de la actividad en la que se modifico el dataobject
	 * 
	 * @return Valor de la propiedad
	 */
	public String getActivityId() {
		return this.activityId;
	}

	/**
	 * Obtener el atributo dataobjectId: 
	 * id del Dataobject al cual se le realiza la accion.
	 *
	 * @return Valor del atributo
	 */
	public String getDataobjectId() {
		return this.dataobjectId;
	}

	/**
	 * Obtener el atributo type: 
	 * clase a la que pertenece el dataobject.
	 *
	 * @return Valor del atributo
	 */
	public String getType() {
		return this.type;
	}

	/**
	 * Obtener el atributo collections: 
	 * colecciones a las que pertenece el dataobject.
	 *
	 * @return Valor del atributo
	 */
	public List<String> getCollections() {
		return this.collections;
	}

	/**
	 * Devuelve el atributo changedProperties:
	 * conjunto de propiedades modificadas en el dataobject.
	 * <p>
	 * La key del mapa es el id de la propiedad.
	 * 
	 * @return Valor del atributo
	 */
	public Map<String, EsperProperty> getPropertyMap() {
		return this.changedProperties;
	}

	/**
	 * Devuelve el atributo changedStates:
	 * conjunto de estados modificados en el dataobject. 
	 * <p>
	 * La key del mapa es el id del punto de vista desde el cual fue evaluado el dataobject.
	 * 
	 * @return Valor del atributo
	 */
	public Map<String, EsperState> getStateMap() {
		return this.changedStates;
	}

	
}
