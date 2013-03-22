package es.us.isa.bpms.ppinot;


import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.us.isa.bpms.BpmsConfig;
import es.us.isa.bpms.BpmsException;
import es.us.isa.bpms.BpmsUtils;
import es.us.isa.bpms.ppinot.PpinotController;
import es.us.isa.bpms.ppinot.event.EsperProperty;
import es.us.isa.bpms.ppinot.event.EsperState;
import es.us.isa.bpms.ppinot.event.EventFactory;



/**
 * Esta es la clase que garantiza el funcionamiento del paquete es.us.isa.bpms con una plataforma BPM. No tiene que ser utlizada
 * por el usuario final. Tiene las siguientes funciones:
 * <ul>
 * <li>Permite el acceso a la configuracion del paquete; lo cual es utilizado en los paquetes 
 * <a href="esper/package-summary.html">es.us.isa.bpms.esper</a> y  
 * <a href="dataobject/package-summary.html">es.us.isa.bpms.dataobject</a>, asi como en algunos metodos de la 
 * <a href="PpinotUtils.html">clase utilitaria</a>. Para ello define constantes cuyos valores se obtienen del archivo de 
 * configuracion bpms.properties que tiene que estar en la carpeta de las clases.
 * <li>Define los metodos que se ejecutan en los eventos de 
 * <a href="BpmsCenter.html#executeProcessStartSendEvent(java.lang.Object)">inicio</a> y
 * <a href="BpmsCenter.html#executeProcessEndSendEvent(java.lang.Object)">fin</a> de proceso, e
 * <a href="BpmsCenter.html#executeActivityStartSendEvent(java.lang.Object)">inicio</a> y
 * <a href="BpmsCenter.html#executeActivityEndSendEvent(java.lang.Object)">fin</a> de actividad, y al 
 * <a href="BpmsCenter.html#executeServiceClassCall(java.lang.Object)">ejecutar</a> una tarea de servicio. Estos metodos son 
 * invocados desde las clases definidas en el paquete 
 * <a href="../../javadocs-activiti/es/us/isa/bpms/platform/activiti/sendevent/package-summary.html">es.us.isa.bpms.platform.activiti.sendevent</a> 
 * en el caso de Activiti. 
 * <li>Define el <a href="#executeDataobjectSendEvent(java.lang.Object, java.lang.String, java.lang.String, java.util.List, java.util.Map, java.util.Map)">metodo que se ejecuta</a> 
 * cuando se modifica las propiedades o los estados de dataobjects.
 * Este metodo se invoca desde la clase <a href="dataobject/DataobjectSpace.html">DataobjectSpace</a> cuando se detecta que 
 * ha sido modificado algun dataobject de la instancia de proceso en ejecucion. 
 * <li>Permite el acceso al <a href="../../javadocs-activiti/es/us/isa/bpms/platform/activiti/PlatformController.html">controlador de la interaccion con la 
 * plataforma BPM</a>, que es utilizado en los paquetes <a href="esper/package-summary.html">es.us.isa.bpms.esper</a> y  
 * <a href="dataobject/package-summary.html">es.us.isa.bpms.dataobject</a>.
 * </ul>
 * 
 * @author Edelia Garcia Gonzalez
 * @version 1.0
 * @see es.us.isa.bpms.ppinot.PpinotController
 * @see es.us.isa.bpms.platform.PlatformControllerInterface
 * @see es.us.isa.bpms.dataobject.DataobjectController
 * @see es.us.isa.bpms.dataobject.DataobjectSpace
 * @see es.us.isa.bpms.dataobject.Dataobject
 * @see es.us.isa.bpms.ral.RalController
 */
public class PpinotCenter {

    // log de la aplicacion
    private static final Log log = LogFactory.getLog(PpinotCenter.class);
	
	// controlador de Esper
	private static PpinotController esperController = null;
	
    static {
    	
    	if (esperController==null) {
    		
    		esperController = new PpinotController(BpmsConfig.ENGINEURI);

    	}
    }  	
  
    /**
     * Ejecuta un evento de inicio de proceso. Al ejecutarlo se adicionan los dataobjects que se hayan declarado en las anotaciones del espacio de dataobjects del proceso. Es utilizado para el enlace con la plataforma BPM.
     * <p>
     * Captura la excepcion PpinotException en el caso que se produzca y muestra su mensaje en el log.
     * 
     * @param object Objeto de la plataforma BPM que permite interactuar con ella en tiempo de ejecucion
     */
    public static void executeProcessStartSendEvent(Object object) {
    	
		try {
	    	// envia un evento a Esper
	    	esperController.sendEvent(
		    			EventFactory.createProcessStartEvent( 
		    					BpmsConfig.getPlatformController().getProcessId(object), 
		    					BpmsConfig.getPlatformController().getProcessDefId(object), 
		    					BpmsConfig.getPlatformController().getInstanceId(object), 
		    					BpmsUtils.getCurrentTime() ) );
		} catch (BpmsException e) {
			
	      	log.error(
					e.getMessage());  		
		} catch (Exception e) {
			
			e.printStackTrace();
		} 
    	
    }
    
    /**
     * Ejecuta un evento de fin de proceso. Es utilizado para el enlace con la plataforma BPM.
     * <p>
     * Captura la excepcion PpinotException en el caso que se produzca y muestra su mensaje en el log.
     * 
     * @param object Objeto de la plataforma BPM que permite interactuar con ella en tiempo de ejecucion
     */
    public static void executeProcessEndSendEvent(Object object) {
    	
		try {

	    	// envia un evento a Esper
	  		esperController.sendEvent(
		  				EventFactory.createProcessEndEvent( 
		  						BpmsConfig.getPlatformController().getProcessDefId(object), 
		  						BpmsConfig.getPlatformController().getInstanceId(object), 
		  						BpmsUtils.getCurrentTime() ) );
		} catch (BpmsException e) {
			
	      	log.error(
					e.getMessage());  		
		} 
    	
    }
    
    /**
     * Ejecuta un evento de inicio de actividad, de acuerdo a lo declarado en las notificaciones del espacio de dataobjects del proceso implementado. Es utilizado para el enlace con la plataforma BPM.
     * <p>
     * Captura la excepcion PpinotException en el caso que se produzca y muestra su mensaje en el log.
     * 
     * @param object Objeto de la plataforma BPM que permite interactuar con ella en tiempo de ejecucion
     */
    public static void executeActivityStartSendEvent(Object object) {
    	
		try {

	    	// envia un evento a Esper
    		esperController.sendEvent(
						EventFactory.createActivityStartEvent( 
								
								BpmsConfig.getPlatformController().getProcessDefId(object), 
								BpmsConfig.getPlatformController().getInstanceId(object), 
								BpmsConfig.getPlatformController().getExecutionId(object),

								BpmsUtils.getCurrentTime(),
								BpmsConfig.getPlatformController().getActivityId(object),
								
								BpmsConfig.getPlatformController().getActivityName(object),
								BpmsConfig.getPlatformController().getActivityType(object) ) );
		} catch (BpmsException e) {
			
	      	log.error(
					e.getMessage());  		
		} 
    	
    }
   
    /**
     * Ejecuta un evento de fin de actividad, de acuerdo a lo declarado en las notificaciones del espacio de dataobjects del proceso implementado. Es utilizado para el enlace con la plataforma BPM.
     * <p>
     * Captura la excepcion PpinotException en el caso que se produzca y muestra su mensaje en el log.
     * 
     * @param object Objeto de la plataforma BPM que permite interactuar con ella en tiempo de ejecucion
     */
    public static void executeActivityEndSendEvent(Object object) {
    	
		try {

	    	// envia un evento a Esper
			esperController.sendEvent(
						EventFactory.createActivityEndEvent( 
								BpmsConfig.getPlatformController().getProcessDefId(object), 
								BpmsConfig.getPlatformController().getInstanceId(object), 
								BpmsConfig.getPlatformController().getExecutionId(object),
								
								BpmsUtils.getCurrentTime(),
								BpmsConfig.getPlatformController().getActivityId(object)
								) );
		} catch (BpmsException e) {
			
	      	log.error(
					e.getMessage());  		
		} 
    	
    }
    
    /**
     * Ejecuta un evento de asignacion de usuario a una actividad. Es utilizado para el enlace con la plataforma BPM.
     * <p>
     * Captura la excepcion PpinotException en el caso que se produzca y muestra su mensaje en el log.
     * 
     * @param object Objeto de la plataforma BPM que permite interactuar con ella en tiempo de ejecucion
     */
    public static void executeUserTaskAsignedUserEvent(Object object) {
    	
		String performer = BpmsConfig.getPlatformController().getUser(object);
		if (performer!=null && performer!="")
	    	try {
	
		    	// envia un evento a Esper
		    	if (BpmsConfig.getPlatformController().getActivityType(object).contentEquals("userTask"))
						esperController.sendEvent(
								EventFactory.createUserTaskAsignedUserEvent( 
										BpmsConfig.getPlatformController().getProcessDefId(object), 
										BpmsConfig.getPlatformController().getInstanceId(object), 
										BpmsConfig.getPlatformController().getExecutionId(object),
	
										BpmsUtils.getCurrentTime(),
										BpmsConfig.getPlatformController().getActivityId(object),
										
										performer ) );
			} catch (BpmsException e) {
				
		      	log.error(
						e.getMessage());  		
			} 
	    	
    }
    
    /**
     * Ejecuta un evento que indica que un dataobject fue modificado. Es utilizado internmente en el paquete.
     * <p>
     * Captura la excepcion PpinotException en el caso que se produzca y muestra su mensaje en el log.
     * 
     * @param object Objeto de la plataforma BPM que permite interactuar con ella en tiempo de ejecucion
     * @param dataobjectId Id del dataobject
     * @param type Clase a la que pertenece el dataobject
     * @param collections Colecciones a la que pertenece el dataobject
     * @param changedProperties Propiedades modificadas (la key del mapa es el id de la propiedad)
     * @param changedStates Estados modificados (la key del mapa es el id del punto de vista desde cual se evaluo el dataobject)
     */
    public static void executeDataobjectSendEvent(Object object, String dataobjectId, String type, List<String> collections, Map<String, EsperProperty> changedProperties, Map<String, EsperState> changedStates) {
    	
		try {
			
			esperController.sendEvent(
						EventFactory.createDataobjectEvent(
								BpmsConfig.getPlatformController().getProcessDefId(object), 
								BpmsConfig.getPlatformController().getInstanceId(object), 
								BpmsConfig.getPlatformController().getExecutionId(object), 
								BpmsUtils.getCurrentTime(),
								BpmsConfig.getPlatformController().getActivityId(object),
								dataobjectId, 
								type, 
								collections,
								changedProperties, 
								changedStates));
		} catch (BpmsException e) {
			
	      	log.error(
					e.getMessage());  		
		} 
    	
    }

}
