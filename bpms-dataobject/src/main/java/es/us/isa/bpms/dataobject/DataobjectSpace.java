package es.us.isa.bpms.dataobject;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import es.us.isa.bpms.BpmsConfig;
import es.us.isa.bpms.BpmsException;
import es.us.isa.bpms.dataobject.annotations.DataobjectClass;
import es.us.isa.bpms.dataobject.annotations.DataobjectClasses;
import es.us.isa.bpms.dataobject.annotations.DataobjectObject;
import es.us.isa.bpms.dataobject.annotations.Dataobjects;
import es.us.isa.bpms.ppinot.PpinotCenter;
import es.us.isa.bpms.ppinot.event.EsperProperty;
import es.us.isa.bpms.ppinot.event.EsperState;
import es.us.isa.bpms.ral.RalCenter;



/**
 * Clase que conserva los dataobjects de las instancias de un proceso que se esta ejecutando. Proporciona al 
 * usuario final facilidades para adicionar y obtener dataobjects, organizarlos en colecciones y darle valor a 
 * variables de la plataforma BPM.
 * <p>
 * Para cada proceso se debe crear <a href="../../../javadocs-example/simple/ProcessDataobjectSpace.html">una subclase</a> de esta clase, que 
 * es utilizada para implementar para un proceso los listeners de las actividades e indicar mediante anotaciones en que 
 * forma se ejecutan. Esta clase debe estar en la <a href="../../../javadocs-example/simple/package-summary.html">raiz del paquete del proceso</a>.
 * <p>
 * Las subclases tienen que incluir anotaciones de clase 
 * <a href="annotations/DataobjectClasses.html">DataobjectClasses</a>, 
 * <a href="annotations/Dataobjects.html">Dataobjects</a>, 
 * <a href="annotations/ObjectUpdates.html">ObjectUpdates</a> y 
 * <a href="annotations/VariableUpdates.html">VariableUpdates</a>.
 * <p>
 * Los metodos de las subclases utilizados en los listeners tienen que tener anotaciones 
 * <a href="annotations/Listeners.html">Listeners</a>.
 *
 * @author Edelia Garcia Gonzalez
 * @version 1.0
 */
public class DataobjectSpace implements Serializable {
	 
    /**
	 * Para serializar
	 */
	private static final long serialVersionUID = -7034779787096345998L;
	
	/**
	 * Informacion de las clases de los dataobjects
	 * @serial
	 */
    private Map<String, String> classDefs;
    /**
     * dataobjects de cada una de las instancias de un proceso. Mapea los Id de las instancias de procesos
     * con un mapa con los Dataobject. En el mapa de los Dataobject se utiliza como identificador el id de estos.
	 * @serial
     */
    private Map<String, Map<String, Object>> space;
    /**
     * Clases a las que pertenecen los dataobjects
	 * @serial
     */
    private Map<String, Map<String, String>> classRel;
    /**
     * Espacio de las colecciones de dataobjects
	 * @serial
     */
    private Map<String, Map<String, List<String>>> collections;
    
    /**
     * Constructor de la clase.
     * 
     */
	protected DataobjectSpace() {
		
		// inicializa la informacion de las clases de los dataobjects
		this.classDefs = new HashMap<String, String>();
		// inicializa el espacio de los dataobjects
		this.space = new HashMap<String, Map<String, Object>>();
		// inicializa la informacion con las clases a los que pertenecen los dataobjects
		this.classRel = new HashMap<String, Map<String, String>>();
		// inicializa el espacio de las colecciones de dataobjects
		this.collections = new HashMap<String, Map<String, List<String>>>();
	}
	
	/**
	 * Da valor a una variable de la plataforma BPM.
	 * 
     * @param object Objeto de la plataforma BPM que permite interactuar con ella en tiempo de ejecucion
	 * @param variableName Nombre de la variable
	 * @param value Valor de la variable
	 */
	public void setVariable(Object object, String variableName, Object value) {
		
		// da valor a la variable de la plataforma BPM que conserva el valor
		BpmsConfig.getPlatformController().setVariableValue(object, variableName, value);
	}
	
	/**
	 * Crea y adiciona un dataobject. Lanza una excepcion si la clase del dataobject no fue declarada en el espacio de 
	 * dataobjects del proceso.
	 * 
     * @param object Objeto de la plataforma BPM que permite interactuar con ella en tiempo de ejecucion
	 * @param id Id del dataobject
	 * @param dataobjectClassId Id de la clase del dataobject
	 * @throws BpmsException 
	 */
	public void addDataobject(Object object, String id, String dataobjectClassId) throws BpmsException {
		
    	// obtiene el Id de la instancia de proceso que se esta ejecutando
		String processId = BpmsConfig.getPlatformController().getProcessId(object);
		String instanceId = BpmsConfig.getPlatformController().getInstanceId(object);

    	// crea un objeto de la clase especificada y lo inicializa
		Object dataobject;
		try {
			
			Class<?> cl = Class.forName( this.classDefs.get( dataobjectClassId ) );                                                  
			dataobject = cl.newInstance();
			((Dataobject) dataobject).baseinit(processId, instanceId, id);
		} catch (Exception e) {
			
			throw new BpmsException("Error adicionado un dataobject. Verifica que exista la clase del dataobject." +
					" Proceso: "+processId+ " Instancia: "+instanceId+ 
					" Dataobject: "+id);
		} 
		
    	// se adiciona el Dataobject
    	this.space.get(instanceId).put( id, dataobject );
    	
    	// se conserva la clase a la que pertenece el dataobject
    	String classref = dataobject.getClass().getName();
    	if (this.classDefs.containsValue(classref)) {
    		
    		// obtiene el id de la clase del dataobject
    		String classid = "";
			Iterator<Entry<String, String>> it = this.classDefs.entrySet().iterator();
		    while (it.hasNext()) {
		        Map.Entry<String, String> pairs = (Map.Entry<String, String>)it.next();
		        classid = (String) pairs.getKey();
		        String value = (String) pairs.getValue();
		        
		        if (value.contentEquals(classref))
		        	break;
		    }
		    // conserva la relacion entre el dataobject y su clase
    		this.classRel.get(instanceId).put( id, classid );
    	}
    	else
			throw new BpmsException("Error adicionado un dataobject. Verifica que exista la clase del dataobject." +
					" Proceso: "+processId+ " Instancia: "+instanceId+ 
					" Dataobject: "+id);
    	
    	// conserva el Dataobject serializado en una variable de la plataforma BPM
    	this.setObjectVariable(object, id);
	}
    
    /**
     * Devuelve un dataobject de una instancia de proceso. Devuelve null si el dataobject solicitado no existe.
     * 
     * @param object Objeto de la plataforma BPM que permite interactuar con ella en tiempo de ejecucion
     * @param id Id del dataobject
     * @return El dataobject solicitado
     */
    public Object getDataobject(Object object, String id) {
    	// obtiene el Id de la instancia de proceso que se esta ejecutando
    	String instanceId = BpmsConfig.getPlatformController().getInstanceId(object);
    	// devuelve el Dataobject solicitado
    	if (this.space.get(instanceId)==null || !this.space.get(instanceId).containsKey(id))
    		return null;
    	else
    		return this.space.get(instanceId).get(id);
    }
    
    /**
     * Adiciona un dataobject a una coleccion, si el dataobject existe en el espacio de dataobjects. 
     * Si la coleccion no existe, se crea.
     * 
     * @param object Objeto de la plataforma BPM que permite interactuar con ella en tiempo de ejecucion
     * @param id Id del dataobject
     * @param collectionId Id de la coleccion
     */
    public void addToCollection(Object object, String id, String collectionId) {
    	
    	// obtiene el Id de la instancia de proceso que se esta ejecutando
    	String instanceId = BpmsConfig.getPlatformController().getInstanceId(object);
    	
    	// si la coleccion no existe la crea
    	if (!this.collections.get(instanceId).containsKey(collectionId))
    		this.collections.get(instanceId).put(collectionId, new ArrayList<String>());
    	
    	// si el dataobject no estaba en la coleccion, lo adiciona
    	if (this.space.get(instanceId).containsKey(id)) {
    		
	     	if (!this.collections.get(instanceId).get(collectionId).contains(id))
	    		this.collections.get(instanceId).get(collectionId).add(id);
    	} 
    }
    
    /**
     * Elimina un dataobject de una coleccion.
     * 
     * @param object Objeto de la plataforma BPM que permite interactuar con ella en tiempo de ejecucion
     * @param id Id del dataobject
     * @param collectionId Id de la coleccion
     */
    public void removeFromCollection(Object object, String id, String collectionId) {
    	
    	// obtiene el Id de la instancia de proceso que se esta ejecutando
    	String instanceId = BpmsConfig.getPlatformController().getInstanceId(object);
    	
    	// elimina el dataobject de la coleccion
    	if (this.collections.get(instanceId).containsKey(collectionId)) {
    		
    		this.collections.get(instanceId).get(collectionId).remove(id);
    	}
    }
    
    /**
     * Devuelve los dataobjects de una coleccion. La key del mapa es el id de los dataobjects.
     * 
     * @param object Objeto de la plataforma BPM que permite interactuar con ella en tiempo de ejecucion
     * @param collectionId Id de la coleccion
     * @return Dataobjects de la coleccion
     */
    public Map<String, Object> getCollection(Object object, String collectionId) {
    	
    	// obtiene el Id de la instancia de proceso que se esta ejecutando
    	String instanceId = BpmsConfig.getPlatformController().getInstanceId(object);
    	
    	// obtiene los dataobjects de la coleccion
    	Map<String, Object> map = new HashMap<String, Object>();
    	if (this.collections.get(instanceId).containsKey(collectionId)) {
    	
			for (String dataobjectId : this.collections.get(instanceId).get(collectionId)) {
				
				map.put(dataobjectId, this.getDataobject(object, dataobjectId));
			}
       	}
    	return map;
    }
    
    /**
     * Elimina una coleccion
     * 
     * @param object Objeto de la plataforma BPM que permite interactuar con ella en tiempo de ejecucion
     * @param collectionId Id de la coleccion
     */
   public void removeCollection(Object object, String collectionId) {
    	
    	// obtiene el Id de la instancia de proceso que se esta ejecutando
    	String instanceId = BpmsConfig.getPlatformController().getInstanceId(object);
    	
    	// elimina la coleccion
    	this.collections.get(instanceId).remove(collectionId);
    }
   
	/**
	 * Conserva el dataobject dataobjectId serializado en una variable de la plataforma BPM con el nombre dataobjectId.
	 * Envia una notificacion a Esper si se produjo algun cambio en los estados y/o las propiedades del dataobject.
	 * 
	 * @param object Objeto propio de la plataforma BPM mediante el cual se conserva los cambios de estado en la BD. 
	 * Ej: DelegateExecution y DelegateTask en Activiti
	 * @param dataobjectId Id del Dataobject
	 * @throws BpmsException 
	 */
	private void setObjectVariable(Object object, String dataobjectId) throws BpmsException {
		
		Dataobject dataobject = (Dataobject) this.getDataobject(object, dataobjectId );
		if (dataobject!=null) {
			
			// actualiza los estados del dataobject
			Map<String, EsperState> changedStates = dataobject.updateDataobjectStates();
			
			// da valor a la variable de la plataforma BPM que conserva el dataobject
			BpmsConfig.getPlatformController().setVariableValue(object, dataobjectId, dataobject );
			
			// si se produjeron cambios en el dataobject, se envia un evento a Esper 
			Map<String, EsperProperty> changedProperties = dataobject.getChangedProperties();
			if (changedStates.size()>0 || changedProperties.size()>0) {
				
				if (BpmsConfig.PPINOTACTIVE) 
					PpinotCenter.executeDataobjectSendEvent(
							object, 
							dataobject.getId(), 
							dataobject.getClass().getName(), 
							this.getDataobjectCollections(object, dataobjectId), 
							changedProperties, 
							changedStates);
				
				if (BpmsConfig.RALACTIVE) {
					
					Iterator<Entry<String, EsperProperty>> it = changedProperties.entrySet().iterator();
				    while (it.hasNext()) {
				        Map.Entry<String, EsperProperty> pairs = (Map.Entry<String, EsperProperty>)it.next();
				        EsperProperty esperProperty = (EsperProperty) pairs.getValue();
				        String propertyId = esperProperty.getPropertyId();
				        Object value = esperProperty.getValue();
				        Object oldvalue = esperProperty.getOldvalue();

						if (value instanceof String)
							RalCenter.getRalController().updateDataobjectField(object, dataobjectId, propertyId, (String) value, (String) oldvalue);
				    }
				}
			}
			dataobject.setChangedProperties(null);
		}
	}

   /**
    * Devuelve los ids de las colecciones a las que pertenece un dataobject
    * 
    * @param object Objecto que permite interactuar con la plataforma BPM
    * @param dataobjectId Id del dataobject
    * @return Lista de los ids de las colecciones
    */
   private List<String> getDataobjectCollections(Object object, String dataobjectId) {
   	
		List<String> collec = new ArrayList<String>();
		String instanceId = BpmsConfig.getPlatformController().getInstanceId(object);
		Iterator<Entry<String, List<String>>> it = this.collections.get(instanceId).entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String, List<String>> pairs = (Map.Entry<String, List<String>>)it.next();
	        String key = (String) pairs.getKey();
	        List<String> value = (List<String>) pairs.getValue();
	        
	        if (value.contains(dataobjectId))
	        	collec.add(key);
	    }
	    
	    return collec;
   }
   
   /**
    * Crea y adiciona todos los dataobjects indicados en la anotacion Dataobjects.
    * 
    * @param object Objeto de la plataforma BPM que permite interactuar con ella en tiempo de ejecucion
    * @param dataobject Dataobject a adicionar
    * @throws BpmsException 
    */
	void addDataobjects(Object object) throws BpmsException {

	   	// obtiene el Id de la instancia de proceso que se esta ejecutando
	   	String instanceId = BpmsConfig.getPlatformController().getInstanceId(object);
	   	// si en el mapa no se encuentra una entrada para esa instancia, se crea una
	   	if (this.space.get(instanceId)==null) {
	   		
	   		this.space.put(instanceId, new HashMap<String, Object>());
	   		this.classRel.put(instanceId, new HashMap<String, String>());
	   		this.collections.put(instanceId, new HashMap<String, List<String>>());
	   	}
	
		// obtiene la informacion de las clases de dataobjects en el proceso
	   	DataobjectClasses dataobjectclasses = this.getClass().getAnnotation(DataobjectClasses.class);
		if (dataobjectclasses!=null) {
			
	    	// para cada una de las clases indicadas
			for (DataobjectClass anot : Arrays.asList(dataobjectclasses.value()) ) {
			
				this.classDefs.put(anot.id(), anot.classref());
				
				if (BpmsConfig.RALACTIVE) {
					
					// adiciona a la ontologia la relacion entre la clase del dataobject y el proceso
					RalCenter.getRalController().addDataobjectClass(object, anot.id());
				}
			}
		}
  	
		// obtiene la informacion de los dataobjects en el proceso
		Dataobjects dataobjects = this.getClass().getAnnotation(Dataobjects.class);
		if (dataobjects!=null) {	
			
	    	// para cada uno de los dataobjects indicados
			for (DataobjectObject anot : Arrays.asList(dataobjects.value()) ) {

				if (this.classDefs.containsKey(anot.dataobjectclassid())) {
					
					// adiciona el objeto al espacio de dataobjects
					this.addDataobject(object, anot.id(), anot.dataobjectclassid());
				}
				
				if (BpmsConfig.RALACTIVE) {
					
					// adiciona a la ontologia la relacion entre el dataobject y el proceso
					RalCenter.getRalController().addDataobject(object, anot.id(), anot.dataobjectclassid());
				}
			}
		}
   }
    
    /**
     * Elimina los dataobjects de una instancia de proceso
     * 
     * @param object Objeto de la plataforma BPM que permite interactuar con ella en tiempo de ejecucion
     */
    void clear(Object object) {
       	// obtiene el Id de la instancia de proceso que se esta ejecutando
    	String instanceId = BpmsConfig.getPlatformController().getInstanceId(object);
    	// elimina la entrada de esa instancia de proceso
    	this.space.remove(instanceId);
    	this.classRel.remove(instanceId);
    	this.collections.remove(instanceId);
    }
    
    /**
     * Indica si el espacio esta vacio, es decir, que no hay instancias de proceso en ejecucion 
     * 
     * @return
     */
    Boolean isEmpty() {
    	
    	return this.space.size()==0;
    }
	
	/**
	 * Actualiza variables de la plataforma BPM a partir de las propiedades de un dataobject; previamente
	 * lo conserva serializado en una variable de la plataforma BPM con el nombre dataobjectId
	 * 
	 * @param object Objeto propio de la plataforma BPM mediante el cual se conserva los cambios de estado en la BD
	 * @param dataobjectId Id del dataobject
	 * @throws BpmsException 
	 */
	void updateVariablesFromObject(Object object, String dataobjectId) throws BpmsException {
		
		// actualiza la variable de la plataforma BPM que conserva el objeto
		this.setObjectVariable(object, dataobjectId);
		
		// actualiza las variables de la plataforma BPM que conservan atributos del objeto que se muestran en formularios
		Dataobject dataobject = (Dataobject) this.getDataobject(object, dataobjectId);
		if (dataobject!=null) {

			Iterator<Entry<String, Object>> it = dataobject.updateVariablesFromObject().entrySet().iterator();
		    while (it.hasNext()) {
		        Map.Entry<String, Object> pairs = (Map.Entry<String, Object>)it.next();
		        String variable = (String) pairs.getKey();
		        Object value = (Object) pairs.getValue();
				
		        BpmsConfig.getPlatformController().setVariableValue(object, variable, value);
		    }
		}
	}
	
	/**
	 * Actualiza variables de la plataforma BPM a partir de las propiedades de todos los dataobjects de una clase
	 * 
	 * @param object Objeto propio de la plataforma BPM mediante el cual se conserva los cambios de estado en la BD
	 * @param classId Id de la clase
	 * @throws BpmsException 
	 */
	void updateVariablesFromClass(Object object, String classId) throws BpmsException {
		
    	// actualiza los dataobjects de la clase indicada
    	String instanceId = BpmsConfig.getPlatformController().getInstanceId(object);
		Iterator<Entry<String, String>> it = this.classRel.get(instanceId).entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String, String> pairs = (Map.Entry<String, String>)it.next();
	        String dataobjectid = (String) pairs.getKey();
	        String value = (String) pairs.getValue();
	        
	        if (value.contentEquals(classId)) {
	        	
	        	this.updateVariablesFromObject(object, dataobjectid);
	        }
	    }
	}
	
	/**
	 * Actualiza variables de la plataforma BPM a partir de las propiedades de todos los dataobjects de una coleccion
	 * 
	 * @param object Objeto propio de la plataforma BPM mediante el cual se conserva los cambios de estado en la BD
	 * @param collectionId Id de la coleccion
	 * @throws BpmsException 
	 */
	void updateVariablesFromCollection(Object object, String collectionId) throws BpmsException {

		String instanceId = BpmsConfig.getPlatformController().getInstanceId(object);
    	if (this.collections.get(instanceId).containsKey(collectionId)) {
    		
        	// actualiza los dataobjects de la coleccion indicada
    		for (String dataobjectid : this.collections.get(instanceId).get(collectionId)) {
    			
	        	this.updateVariablesFromObject(object, dataobjectid);
    		}
    	}
	}
	
	/**
	 * Actualiza el dataobject dataobjectId a partir de los valores de variables en la plataforma BPM y
	 * lo conserva serializado en una variable de la plataforma BPM con el nombre dataobjectId.
	 * 
	 * @param object Objeto que permite interactuar con la plataforma BPM
	 * @param dataobjectId Id del dataobject
	 * @param inUpdates Arreglo con los id de los metodos updateIn que se invocaran para actualizar el dataobject
	 * @throws BpmsException 
	 */
	void updateObjectFromVariables(Object object, String dataobjectId, String[] inUpdates) throws BpmsException {
		
		Dataobject dataobject = (Dataobject) this.getDataobject(object, dataobjectId);
		if (dataobject!=null) {

	    	// ejecuta los metodos updateIn
			for (int i = 0;i < inUpdates.length; i++ ){
				
				// obtiene los valores de las variables para ejecutar el metodo inUpdates[i]
				Map<String, Object> variables = new HashMap<String, Object>(); 
				String [] variableNames = dataobject.getUpdateInVariables(inUpdates[i]);
				if (variableNames!=null)
					for (String variableName : Arrays.asList( variableNames )) {
						
						variables.put(variableName,	BpmsConfig.getPlatformController().getVariableValue(object, variableName));
					}
				
				// ejecuta el metodo
				dataobject.updateObjectFromVariables(inUpdates[i], variables);
			}
			
			// actualiza la variable de la plataforma BPM que conserva el objeto
			this.setObjectVariable(object, dataobjectId);
		}
	}
	
	/**
	 * Actualiza los dataobjects de la clase classId a partir de los valores de variables en la plataforma BPM
	 * 
	 * @param object Objeto propio de la plataforma BPM mediante el cual se conserva los cambios de estado en la BD. Ej: DelegateExecution, DelegateTask en Activiti
	 * @param classId Id de la clase
	 * @param inUpdates Arreglo con los id de los metodos updateIn que se invocaran para actualizar el dataobject
	 * @throws BpmsException 
	 */
	void updateClassFromVariables(Object object, String classId, String[] inUpdates) throws BpmsException {

		// obtiene el Id de la instancia de proceso que se esta ejecutando
    	String instanceId = BpmsConfig.getPlatformController().getInstanceId(object);
		
    	// actualiza los dataobjects de la clase indicada
		Iterator<Entry<String, String>> it = this.classRel.get(instanceId).entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String, String> pairs = (Map.Entry<String, String>)it.next();
	        String dataobjectid = (String) pairs.getKey();
	        String value = (String) pairs.getValue();
	        
	        if (value.contentEquals(classId)) {
	        	
	        	this.updateObjectFromVariables(object, dataobjectid, inUpdates);
	        }
	    }
	}

	/**
	 * Actualiza los dataobjects de la coleccion collectionId a partir de los valores de variables en la plataforma BPM
	 * 
	 * @param object Objeto propio de la plataforma BPM mediante el cual se conserva los cambios de estado en la BD. Ej: DelegateExecution, DelegateTask en Activiti
	 * @param collectionId Id de la coleccion
	 * @param inUpdates Arreglo con los id de los metodos updateIn que se invocaran para actualizar el dataobject
	 * @throws BpmsException 
	 */
	void updateCollectionFromVariables(Object object, String collectionId, String[] inUpdates) throws BpmsException {

		// obtiene el Id de la instancia de proceso que se esta ejecutando
    	String instanceId = BpmsConfig.getPlatformController().getInstanceId(object);
		
    	if (this.collections.get(instanceId).containsKey(collectionId)) {
    		
        	// actualiza los dataobjects de la coleccion indicada
    		for (String dataobjectid : this.collections.get(instanceId).get(collectionId)) {
    			
	        	this.updateObjectFromVariables(object, dataobjectid, inUpdates);
    		}
    	}
	}
	
}
