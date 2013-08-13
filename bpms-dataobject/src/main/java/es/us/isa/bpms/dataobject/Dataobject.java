package es.us.isa.bpms.dataobject;


import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.us.isa.bpms.BpmsException;
import es.us.isa.bpms.dataobject.annotations.BPMVariables;
import es.us.isa.bpms.dataobject.annotations.SVPoint;
import es.us.isa.bpms.dataobject.annotations.StateViewPoints;
import es.us.isa.bpms.ppinot.event.EsperProperty;
import es.us.isa.bpms.ppinot.event.EsperState;



/**
 * Clase de la que heredan los dataobjects, permitiendo asi estructurar como objetos los datos manipulados en un proceso.
 *  
 * @author Edelia Garcia Gonzalez
 * @version 1.0
 * @see StateViewPoint
 */
public abstract class Dataobject implements Serializable {

	/**
	 * Para serializar
	 */
	private static final long serialVersionUID = -4518643020282516689L;

	/**
	 * Id del diagrama del proceso del dataobject.
	 * @serial
	 */
	private String processId;

	/**
	 * Id de la instancia de proceso del dataobject.
	 * @serial
	 */
	private String instanceId;
	
	/**
	 * Id con el que se identifica la instancia del dataobject a los efectos de realizar los registros en la BD.
	 * @serial
	 */
	private String id;
	
	/**
	 * Lista de los puntos de vista (StateViewPoint) no excluyentes desde los cuales debe ser evaluado el dataobject.
	 * @serial
	 */
	private List<StateViewPoint> viewPointList;
	
	/**
	 * Cambios realizados a las propiedades del dataobject
	 * @serial
	 */
	private Map<String, EsperProperty> changedProperties;
	
	/**
	 * Constructor de la clase
	 * 
	 */
	protected Dataobject() {

	}

	/**
	 * Devuelve el atributo changedProperties:
	 * Cambios realizados a las propiedades del dataobject
	 * 
	 * @return Valor del atributo
	 */
	Map<String, EsperProperty> getChangedProperties() {
		
		if (this.changedProperties==null)
			this.changedProperties = new HashMap<String, EsperProperty>();
		return this.changedProperties;
	}

	/**
	 * Da valor al atributo changedProperties:
	 * Cambios realizados a las propiedades del dataobject
	 * 
	 * @param changedProperties Valor del atributo
	 */
	void setChangedProperties(Map<String, EsperProperty> changedProperties) {
		this.changedProperties = changedProperties;
	}
	
	/**
	 * Devuelve el nombre de un metodo a partir del prefijo y el identificador
	 * 
	 * @param prefix Prefijo
	 * @param id Id del metodo
	 * @return Nombre del metodo
	 */
	private String getMethodName(String prefix, String id) {
		
		StringBuilder result = new StringBuilder( prefix.length() + id.length() ); 
		result.append(prefix).append( Character.toUpperCase(id.charAt(0)) ).append( id.substring(1) ); 
		
		return result.toString();
	}
	
	/**
	 * Inicializa el dataobject.
	 * <p>
	 * El id del dataobject es aquel con el que se identifica en las medidas DataMeasure y DataConditionMeasure, en el espacio
	 * de dataobjects del proceso y en el XML del proceso.
	 * 
	 * @param processId Id del proceso
	 * @param instanceId Id de la instancia de proceso a la que pertenece
	 * @param dataobjectId Id del dataobject
	 */
	void baseinit(String processId, String instanceId, String dataobjectId) {
		
		// inicializaciones
		this.processId = processId;
		this.instanceId = instanceId;
		this.setId(dataobjectId);
			
		// inicializa la estructura de datos que conserva los estados del dataobject a partir de la anotacion StateViewPoints
		this.viewPointList = new ArrayList<StateViewPoint>();
		StateViewPoints stateViewPoints = this.getClass().getAnnotation(StateViewPoints.class);
		if (stateViewPoints!=null)
			for (SVPoint anot : Arrays.asList(stateViewPoints.value()) ) {
				
				this.viewPointList.add(
					new StateViewPoint(anot.id(), anot.fromBeginning().contentEquals("true"), Arrays.asList(anot.states())) );
			}
		
		// inicializaciones propias de la subclase
		this.init();
		
	}
	
	/**
	 * Inicializaciones propias de las subclases
	 * 
	 */
	protected abstract void init();

	/**
	 * Valida si los atributos del dataobject cumplen con las condiciones requeridas
	 * 
	 * @return El mensaje de error en caso que se detecte alguno, en caso contrario se devuelve la cadena vacia
	 */
	public abstract String validate();
	
	/**
	 * Registra si cambio el valor de una propiedad. Debe ser invocado antes de actualizar el valor de las propiedades 
	 * <a href="../../../javadocs-example/simple/dataobject/SolicitudObject.html#setNombre(java.lang.String)">en los metodos set</a>.
	 * 
	 * @param property Id de la propiedad
	 * @param oldValue Valor anterior
	 * @param newValue Nuevo valor
	 */
	protected void recordChange(String property, Object oldValue, Object newValue) {
		
		// se registra si se modifico el valor de la propiedad
		if ((oldValue==null && newValue!=null) || !oldValue.equals(newValue)) {
			
			this.getChangedProperties().put(property, new EsperProperty(property, newValue, oldValue));
		}
	}

	/**
	 * Obtener el atributo id: 
	 * Id del dataobject.
	 *
	 * @return Valor del atributo
	 */
	String getId() {
		
		return this.id;
	}
	
	/**
	 * Actualizar el atributo id: 
	 * Id del dataobject.
	 *
	 * @param id Valor del atributo
	 */
	void setId(String id) {
		
		this.id = id;
	}
	
	/**
	 * Actualiza los estados de un dataobject.
	 * 
	 * @return Estados que fueron modificados 
	 * @throws BpmsException
	 * @see es.us.isa.bpms.ppinot.event.EsperState
	 */
	Map<String, EsperState> updateDataobjectStates() throws BpmsException {
		
		Map<String, EsperState> changedStates = new HashMap<String, EsperState>();
		
		// Determina el estado actual del dataobject para cada uno de los puntos de vista que tiene registrados  
		for( StateViewPoint stVP: this.viewPointList) {

			// obtiene el estado que tenia el dataobject desde el punto de vista stVP
			String oldStateId = stVP.getCurrentState();
			
			// evalua la lista de estados del punto de vista stVP hasta que se obtiene que uno de ellos se satisface
			byte statePos;						// posicion del estado que se esta evaluando
			Boolean already = false;			// indica si al recorrer la lista ya se paso el estado actual de dataobject
			Boolean satisfied = false;			// indica si ya un estado fue evaluado como true
			for(statePos=0; statePos < stVP.getStateList().size(); statePos++)  {
				
				String analyzedState = stVP.getStateList().get(statePos);
				if ( !already )
					already = oldStateId.contentEquals( analyzedState );
				if ( stVP.getFromBeginning() || already ) {
					
					try {

						String methodName = this.getMethodName("is", analyzedState);
						Method method = this.getClass().getMethod(methodName);
						satisfied = (Boolean) method.invoke(this);
					} catch (Exception e) {
						
						throw new BpmsException("No se encuentra el metodo para evaluar un estado de un dataobject. Verifica que en la clase del dataobject existe el metodo is del estado indicado." +
								" Proceso: "+this.processId+ " Instancia: "+this.instanceId+ 
								" Dataobject: "+this.getId() +
								" Punto de vista: "+stVP.getId() +" Estado: "+ analyzedState);
					} 

					if ( satisfied )
						break;
				}
			}
			String newStateId = "";
			if (statePos < stVP.getStateList().size())
				newStateId = stVP.getStateList().get(statePos);
			stVP.setCurrentState(newStateId);
			
			// si el estado anterior y el actual son diferentes, se conserva en changedStates
			if (oldStateId!=newStateId)
				changedStates.put(stVP.getId(), new EsperState(stVP.getId(), newStateId));
		}
		
		return changedStates;
	}
	
	/**
	 * Devuelve las variables de la plataforma que deben pasarse a un metodo updateIn
	 * 
	 * @param updateIn Id del metodo
	 * @return Nombres de variables
	 * @throws BpmsException 
	 */
	String[] getUpdateInVariables(String updateIn) throws BpmsException {

		try {
			
			return this.getClass().getMethod(this.getMethodName("updateIn", updateIn), Map.class).getAnnotation(BPMVariables.class).value();
		} catch (Exception e) {
			
			throw new BpmsException("No se encuentra un metodo updateIn. Verifica que en la clase del dataobject existe el metodo updateIn indicado." +
					" Proceso: "+this.processId+ " Instancia: "+this.instanceId+ 
					" Dataobject: "+this.getId() +
					" Identificador: "+updateIn);
		}
	}
	
	/**
	 * Ejecuta un metodo updateIn
	 * 
	 * @param updateIn Identificador del metodo
	 * @param variables Valores de variables de la plataforma BPM
	 * @throws BpmsException 
	 */
	void updateObjectFromVariables(String updateIn, Map<String, Object> variables) throws BpmsException {

		try {
			
			Method method = this.getClass().getMethod(this.getMethodName("updateIn", updateIn), Map.class);
			method.invoke(this, variables);
		} catch (Exception e) {
			
			throw new BpmsException("Error ejecutando un metodo updateIn. Verifica que en la clase del dataobject existe el metodo updateIn indicado." +
					" Proceso: "+this.processId+ " Instancia: "+this.instanceId+ 
					" Dataobject: "+this.getId() +
					" Identificador: "+updateIn);
		} 
	}
	
	/**
	 * Ejecuta todos los metodos updateOut y devuelve los valores de todas las variables de la plataforma que toman valor
	 * 
	 * @return Valores de variables de la plataforma BPM
	 * @throws BpmsException 
	 */
	@SuppressWarnings("unchecked")
	Map<String, Object> updateVariablesFromObject() throws BpmsException {
		
		Map<String, Object> map = new HashMap<String, Object>();
		for (Method method : Arrays.asList(this.getClass().getMethods()) ) {
			
			String methodName = method.getName();
			if (methodName.startsWith("updateOut")) {
				
				try {
					
					map.putAll( (Map<String, Object>) method.invoke(this) );
				} catch (Exception e) {
					
					throw new BpmsException("Error ejecutando un metodo updateOut. Verifica que en la clase del dataobject existe el metodo updateOut indicado." +
							" Proceso: "+this.processId+ " Instancia: "+this.instanceId+ 
							" Dataobject: "+this.getId() +
							" Metodo: "+methodName);
				} 
			}
		}
		return map;
	}
}
