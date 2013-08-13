package es.us.isa.bpms.dataobject;


import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import es.us.isa.bpms.BpmsConfig;
import es.us.isa.bpms.BpmsException;
import es.us.isa.bpms.dataobject.annotations.Listener;
import es.us.isa.bpms.dataobject.annotations.Listeners;
import es.us.isa.bpms.dataobject.annotations.ObjectUpdate;
import es.us.isa.bpms.dataobject.annotations.ObjectUpdates;
import es.us.isa.bpms.dataobject.annotations.ServiceTask;
import es.us.isa.bpms.dataobject.annotations.ServiceTasks;
import es.us.isa.bpms.dataobject.annotations.VariableUpdate;
import es.us.isa.bpms.dataobject.annotations.VariableUpdates;


/**
 * Clase que controla la manipulacion de dataobjects.
 * <p>
 * La clase <a href="../BpmsCenter.html">BpmsCenter</a> crea una instancia de esta clase que se utiliza 
 * internamente en el paquete. El usuario final que implementa un proceso no necesita acceder a esta clase.
 * 
 * @author Edelia Garcia Gonzalez
 * @version 1.0
 */
public final class DataobjectController implements Serializable {

	/**
	 * Para serializar
	 */
	private static final long serialVersionUID = -1804175940921219835L;
	
	/**
	 * los espacios de dataobjects de los procesos
	 * @serial
	 */
	private Map<String, Object> dataobjectSpaceList;
	/**
	 * las clases de los espacios de dataobjects de los procesos
	 * @serial
	 */
	private Map<String, String> dataobjectSpaceClassList;
    
 	/**
	 * Constructor de la clase
	 */
	public DataobjectController() {
		
		this.dataobjectSpaceList = new HashMap<String, Object>();
		this.dataobjectSpaceClassList = new HashMap<String, String>();
    }
	
	/**
	 * Ejecuta un evento de una actividad.
	 * <p>
	 * Localiza las anotaciones en la clase del espacio de dataobjects del proceso, que estan relacionadas con el tipo de 
	 * evento y la actividad indicados, y realiza lo siguiente:
	 * <ul>
	 * <li>Actualiza las propiedades indicadas en la anotacion ObjectUpdates, de los dataobjects indicados, a partir de las 
	 * variables de la plataforma BPM.
	 * <li>Invoca el metodo ligado a la actividad y el tipo de evento, indicados en la anotacion Listeners.
	 * <li>Actualiza las variables de la plataforma BPM a partir de los dataobjects indicados en la anotacion VariableUpdates.
	 * </li>
	 * </ul>
	 * 
	 * @param object Objeto de la plataforma BPM que permite interactuar con ella en tiempo de ejecucion
	 * @param eventType Tipo de evento que se ejecuta
	 * @throws BpmsException 
	 */
	private void executeEvent(Object object, String eventType) throws BpmsException {

		// obtiene el id del diagrama del proceso
		String processId = BpmsConfig.getPlatformController().getProcessId(object);

		if (!this.dataobjectSpaceList.containsKey(processId))
			return;

		// obtiene el espacio de dataobjects del proceso
		DataobjectSpace dataobjectSpace = (DataobjectSpace) this.dataobjectSpaceList.get( processId );
		
		// obtiene el id de la actividad a partir del objeto que representa la ejecucion del proceso en la plataforma BPM
		String activityId = BpmsConfig.getPlatformController().getActivityId(object);
		
		// actualiza los dataobjects indicados a partir de las variables de la plataforma BPM
		ObjectUpdates objectUpdates = dataobjectSpace.getClass().getAnnotation(ObjectUpdates.class);
		if (objectUpdates!=null)
			for (ObjectUpdate anot : Arrays.asList(objectUpdates.value()) ) {
				
				// si se indica una actualizacion del tipo de evento que se ejecuta y para la actividad que se ejecuta
				if (anot.event().contentEquals( eventType ) && anot.activityid().contentEquals(activityId)) {
					
					// actualiza a partir de las variables de la plataforma BPM
					if (anot.updatetype().contentEquals(BpmsConfig.DATAOBJECTUPDATE)) {
					
						dataobjectSpace.updateObjectFromVariables(object, anot.id(), anot.updates());
					} else
					if (anot.updatetype().contentEquals(BpmsConfig.CLASSUPDATE)) {
						
						dataobjectSpace.updateClassFromVariables(object, anot.id(), anot.updates());
					} else
					if (anot.updatetype().contentEquals(BpmsConfig.COLLECTIONUPDATE)) {
						
						dataobjectSpace.updateCollectionFromVariables(object, anot.id(), anot.updates());
					}
				}
			}
		
		// invoca los listeners
		for (Method method : Arrays.asList(dataobjectSpace.getClass().getMethods()) ) {
			
			// obtiene la informacion de listeners indicada para cada metodo
			Listeners listeners = method.getAnnotation(Listeners.class);
			if (listeners!=null)
				for (Listener listener : Arrays.asList(listeners.value()) ) {
					
					// si se indica un listener del tipo del evento que se ejecuta y para la actividad que se ejecuta
					if (listener.event().contentEquals( eventType ) && listener.activityid().contentEquals(activityId)) {
	
						try {
							
							method.invoke(dataobjectSpace,	object);
						} catch (Exception e) {

							throw new BpmsException("Error invocando listener de actividad. Verifica que el metodo tenga el tipo de argumento requerido para los listeners de la plataforma BPM." +
									" Proceso: "+processId+ 
									" Actividad: "+activityId +" Tipo de evento: "+eventType);
						}
					}
				}
		}
		
		// actualiza las variables de la plataforma BPM a partir de los dataobjects indicados
		VariableUpdates variableUpdates = dataobjectSpace.getClass().getAnnotation(VariableUpdates.class);
		if (variableUpdates!=null)
			for (VariableUpdate anot : Arrays.asList(variableUpdates.value()) ) {
				
				// si se indica una actualizacion del tipo de evento que se ejecuta y para la actividad que se ejecuta
				if (anot.event().contentEquals( eventType ) && anot.activityid().contentEquals(activityId)) {
					
					// actualiza las variables de la plataforma BPM
					if (anot.updatetype().contentEquals(BpmsConfig.DATAOBJECTUPDATE)) {
						
						dataobjectSpace.updateVariablesFromObject(object, anot.id());
					} else
					if (anot.updatetype().contentEquals(BpmsConfig.CLASSUPDATE)) {
						
						dataobjectSpace.updateVariablesFromClass(object, anot.id());
					} else
					if (anot.updatetype().contentEquals(BpmsConfig.COLLECTIONUPDATE)) {
						
						dataobjectSpace.updateVariablesFromCollection(object, anot.id());
					}
				}
			}
	}
	
	/**
	 * Ejecuta un evento de fin de actividad.
	 * <p>
	 * Localiza las anotaciones en la clase del 
	 * <a href="../../../javadocs-example/simple/ProcessDataobjectSpace.html">espacio de dataobjects del proceso</a>, que estan relacionadas con 
	 * el evento de inicio y la actividad indicada, y realiza lo siguiente:
	 * <ul>
	 * <li>Actualiza las propiedades indicadas en la anotacion ObjectUpdates, de los dataobjects indicados, a partir de las 
	 * variables de la plataforma BPM.
	 * <li>Invoca el metodo ligado a la actividad y el evento de inicio, indicados en la anotacion Listeners.
	 * <li>Actualiza las variables de la plataforma BPM a partir de los dataobjects indicados en la anotacion VariableUpdates.
	 * </li>
	 * </ul>
	 * 
	 * @param object Objeto de la plataforma BPM que permite interactuar con ella en tiempo de ejecucion
	 * @throws BpmsException 
	 */
	public void executeEndEvent(Object object) throws BpmsException {
		
		this.executeEvent(object, BpmsConfig.ENDEVENT);
	}
	
	/**
	 * Ejecuta un evento de inicio de actividad.
	 * <p>
	 * Localiza las anotaciones en la clase del 
	 * <a href="../../../javadocs-example/simple/ProcessDataobjectSpace.html">espacio de dataobjects del proceso</a>, que estan relacionadas con 
	 * el evento de fin y la actividad indicada, y realiza lo siguiente:
	 * <ul>
	 * <li>Actualiza las propiedades indicadas en la anotacion ObjectUpdates, de los dataobjects indicados, a partir de las 
	 * variables de la plataforma BPM.
	 * <li>Invoca el metodo ligado a la actividad y el evento de fin, indicados en la anotacion Listeners.
	 * <li>Actualiza las variables de la plataforma BPM a partir de los dataobjects indicados en la anotacion VariableUpdates.
	 * </li>
	 * </ul>
	 * 
	 * @param object Objeto de la plataforma BPM que permite interactuar con ella en tiempo de ejecucion
	 * @throws BpmsException 
	 */
	public void executeStartEvent(Object object) throws BpmsException {
		
		this.executeEvent(object, BpmsConfig.STARTEVENT);
	}
	
	/**
     * En una tarea de servicio, ejecuta un metodo del espacio de dataobjects del proceso.
	 * 
	 * @param object Objeto de la plataforma BPM que permite interactuar con ella en tiempo de ejecucion
	 * @throws BpmsException 
	 */
	public void executeServiceClassCall(Object object) throws BpmsException {
		
		// obtiene el id del diagrama del proceso
		String processId = BpmsConfig.getPlatformController().getProcessId(object);

		if (!this.dataobjectSpaceList.containsKey(processId))
			return;

		// obtiene el espacio de dataobjects del proceso
		DataobjectSpace dataobjectSpace = (DataobjectSpace) this.dataobjectSpaceList.get( processId );
		
		// obtiene el id de la actividad a partir del objeto que representa la ejecucion del proceso en la plataforma BPM
		String activityId = BpmsConfig.getPlatformController().getActivityId(object);

		// ejecuta el metodo indicado para la actividad en la anotacion Executors 
		for (Method method : Arrays.asList(dataobjectSpace.getClass().getMethods()) ) {
			
			// obtiene la informacion de listeners indicada para cada metodo
			ServiceTasks serviceTasks = method.getAnnotation(ServiceTasks.class);
			if (serviceTasks!=null)
				for (ServiceTask serviceTask : Arrays.asList(serviceTasks.value()) ) {
					
					// si el metodo esta asociado con la actividad que se ejecuta
					if (serviceTask.value().contentEquals(activityId)) {
	
						try {
							
							method.invoke(dataobjectSpace,	object);
						} catch (Exception e) {

							throw new BpmsException("Error ejecutando un metodo en una service task. Verifica que el metodo tenga el tipo de argumento requerido para los listeners de la plataforma BPM." +
									" Proceso: "+processId+ 
									" Actividad: "+activityId);
						}
					}
				}
		}
	}
	
	/**
	 * Adiciona un proceso al controlador.
	 * 
	 * @param object Objeto de la plataforma BPM que permite interactuar con ella en tiempo de ejecucion
	 * @throws BpmsException 
	 */
	@SuppressWarnings("unchecked")
	public void addProcess(Object object) throws BpmsException  {
		
	    // obtiene el id del diagrama del proceso
		String processId = BpmsConfig.getPlatformController().getProcessId(object);

		try {
			
			Class<?> dataobjectSpaceClass;                                                  
			Object dataobjectSpace;
			if ( !this.dataobjectSpaceList.containsKey(processId) ) {
				
				// crea el espacio de dataobjects del proceso y lo conserva
				dataobjectSpaceClass = Class.forName("es.us.isa.bpms.projects."+processId+".ProcessDataobjectSpace");
				dataobjectSpace = dataobjectSpaceClass.newInstance();
				this.dataobjectSpaceList.put(processId, dataobjectSpace);
				this.dataobjectSpaceClassList.put(processId, "es.us.isa.bpms.projects."+processId+".ProcessDataobjectSpace");
			} else {
				
				// obtiene el espacio de dataobjects del proceso
				dataobjectSpace = this.dataobjectSpaceList.get(processId);
				dataobjectSpaceClass = Class.forName(this.dataobjectSpaceClassList.get( processId ));
			}
			
			// adiciona los dataobjects
			((Class<? extends DataobjectSpace>) dataobjectSpaceClass).cast(dataobjectSpace).addDataobjects(object);
		} catch (Exception e) {

			throw new BpmsException("Error adicionando un proceso a DataobjectController. Verifica que esta definida la clase ProcessDataobjectSpace en un paquete cuyo nombre sea el id del proceso." +
					" Proceso: "+processId);
		}
	}

	/**
	 * Elimina un proceso del controlador.
	 * 
	 * @param object Objeto de la plataforma BPM que permite interactuar con ella en tiempo de ejecucion
	 */
	public void removeProcess(Object object) {
		
	    // obtiene el id del diagrama del proceso
		String processId = BpmsConfig.getPlatformController().getProcessId(object);

		if (!this.dataobjectSpaceList.containsKey(processId))
			return;
		
		// elimina la instancia de proceso del espacio de dataobjects
		((DataobjectSpace) this.dataobjectSpaceList.get(processId)).clear(object);

		// si no hay instancias del proceso en ejecucion, elimina su espacio de dataobjects
		if (((DataobjectSpace) this.dataobjectSpaceList.get(processId)).isEmpty()) {
		
			this.dataobjectSpaceList.remove(processId);
			this.dataobjectSpaceClassList.remove(processId);
		}
	}
}
