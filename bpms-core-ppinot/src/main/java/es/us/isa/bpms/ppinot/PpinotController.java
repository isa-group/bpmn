package es.us.isa.bpms.ppinot;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.annotation.Tag;
import com.espertech.esper.client.deploy.DeploymentException;
import com.espertech.esper.client.deploy.DeploymentInformation;
import com.espertech.esper.client.deploy.DeploymentOptions;
import com.espertech.esper.client.deploy.DeploymentResult;
import com.espertech.esper.client.deploy.EPDeploymentAdmin;
import com.espertech.esper.client.deploy.Module;

import es.us.isa.bpms.BpmsConfig;
import es.us.isa.bpms.BpmsException;
import es.us.isa.bpms.ppinot.event.ActivityEndEvent;
import es.us.isa.bpms.ppinot.event.ActivityStartEvent;
import es.us.isa.bpms.ppinot.event.DataobjectEvent;
import es.us.isa.bpms.ppinot.event.Event;
import es.us.isa.bpms.ppinot.event.ProcessEndEvent;
import es.us.isa.bpms.ppinot.event.ProcessStartEvent;
import es.us.isa.bpms.ppinot.event.UserTaskAsignedUserEvent;
import es.us.isa.bpms.ppinot.listener.ActivityEndListener;
import es.us.isa.bpms.ppinot.listener.ActivityStartListener;
import es.us.isa.bpms.ppinot.listener.DataConditionMeasureListener;
import es.us.isa.bpms.ppinot.listener.DataMeasureListener;
import es.us.isa.bpms.ppinot.listener.ProcessEndListener;
import es.us.isa.bpms.ppinot.listener.ProcessStartListener;
import es.us.isa.bpms.ppinot.listener.UserTaskAsignedUserListener;

/**
 * Clase que controla el envio de eventos a Esper. Uilizando el metodo 
 * <a href="#sendEvent(es.us.isa.bpms.esper.event.Event)">sendEvent</a>, se envian eventos desde la clase 
 * <a href="../BpmsCenter.html">BpmsCenter</a>.
 * <p>
 * Las clases de los eventos que se reciben estan en el paquete 
 * <a href="event/package-summary.html">es.us.isa.bpms.esper.event</a>.
 * <p>
 * Los modulos con las declaraciones EPL de los procesos tienen que estar en el camino de las clases de la aplicacion, en 
 * la subcarpeta configurada para el paquete (bpms-modules, por defecto). El nombre de un modulo debe seguir el 
 * convenio: "module-" + id-del-diagrama-del-proceso + ".epl".
 * <p>
 * Las declaraciones EPL en los modulos indican el listener que se activa en el caso que se satisfagan. Las clases de los 
 * listeners estan en el paquete <a href="listener/package-summary.html">es.us.isa.bpms.esper.listener</a>.
 * <p>
 * La clase BpmsCenter crea una instancia de esta clase que utiliza internamente. El usuario final que implementa un 
 * proceso no necesita acceder a esta clase.
 * 
 * @author Edelia Garcia Gonzalez
 * @version 1.0
 * 
 */
public final class PpinotController {

    // log de la aplicacion
    private static final Log log = LogFactory.getLog(PpinotController.class);
    
    private static String KEYSEPARATOR = "-";
    
	// Thread pool utilizada para enviar los flujos de eventos a Esper
	private ExecutorService threadPool = null;
	// Factory para adicionar hilos a threadPool
	private SimpleThreadFactory threadFactory = null;
	// Mapa de los hilos asociados a cada una de las instancias de proceso
	private Map<String, SendRunnable> runnables = null;
	// procesos que han sido adicionados al controlador y los hilos de las instancias de este
	private Map<String, List<SendRunnable>> addedProcess = null;
	// nombre de los modulos de cada proceso
	private Map<String, String> moduleNames = null;
	// proveedor de servicios de Esper en la aplicacion
	private EPServiceProvider esperServiceProvider = null;
	// id del proveedor de servicios Esper
    private String engineURI;
    
	/**
	 * Constructor de la clase
	 */
	public PpinotController(String engineURI) {
		
		log.info(
				"Creating the controller" );  		

		this.engineURI = engineURI;
    }

	/**
	 * Inicializa el controlador Esper.
	 * <p>
	 * Configura los nemotecnicos DataobjectEvent, ProcessStartEvent, ProcessEndEvent, ActivityStartEvent y ActivityEndEvent,
	 * para que los eventos de las medidas puedan ser referenciados facilmente en las declaraciones EPL.
	 */
	private void open() {
		
		// si threadPool es diferente de null es que el controlador se esta ejecutado y no puede ser inicializado
 		if (this.threadPool==null) {

 	      	log.info(
					"Opening");
			
			// inicializa propiedades
			this.threadFactory = new SimpleThreadFactory();
			this.threadPool = null;
			this.runnables = new HashMap<String, SendRunnable>();
			this.addedProcess = new HashMap<String, List<SendRunnable>>();
			this.moduleNames = new HashMap<String, String>();
			
			// crea nemotecnicos para los eventos que los eventos de las medidas puedan ser referenciados facilmente en las declaraciones EPL
			Configuration configuration = new Configuration();
	        configuration.addEventType("DataobjectEvent", DataobjectEvent.class.getName());
	        configuration.addEventType("ProcessStartEvent", ProcessStartEvent.class.getName());
	        configuration.addEventType("ProcessEndEvent", ProcessEndEvent.class.getName());
	        configuration.addEventType("ActivityStartEvent", ActivityStartEvent.class.getName());
	        configuration.addEventType("ActivityEndEvent", ActivityEndEvent.class.getName());
	        configuration.addEventType("UserTaskAsignedUserEvent", UserTaskAsignedUserEvent.class.getName());
	        
	        // obtiene el proveedor de servicios de Esper que sera utilizado
			this.esperServiceProvider = EPServiceProviderManager.getProvider(this.engineURI, configuration);
 		}
 		else
 	      	log.info(
					"The controller is running");
	}
	
	/**
	 * Detiene el controlador Esper.
	 * <p>
	 * El controlador no termina de detenerse hasta que los hilos en ejecucion envien todos los eventos que tienen en cola.
	 * @throws BpmsException 
	 * 
	 */
	private void shutDown(String processDefId) throws BpmsException {
		
		log.info(
        		"Shutting down");
        
        // detiene cada uno de los hilos en ejecucion
		Iterator<Entry<String, SendRunnable>> it = this.runnables.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String, SendRunnable> pairs = (Map.Entry<String, SendRunnable>)it.next();
	        SendRunnable sendRunnable = (SendRunnable) pairs.getValue();
			
        	sendRunnable.setShutdown();
        	while (!sendRunnable.isShutdown()) {
        		// espera hasta que el hilo efectivamente se haya detenido. El hilo no se detiene hasta que envia todos los eventos que tiene en cola
        	}
	    }
	    
	    // detiene threadPool 
        this.threadPool.shutdown();
        try {
        	
        	this.threadPool.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException ex) {
        	
            // no action
        }
        
		this.removeProcessModule(processDefId);

		// resetea propiedades
        this.threadPool = null;
        this.runnables = null;
        this.addedProcess = null;
        this.moduleNames = null;
        this.threadFactory = null;
        this.esperServiceProvider = null;
	}

	/**
	 * Genera la clave con la que se identifica un hilo asociado a una instancia de proceso en ejecucion.
	 * 
	 * @param processDefId id del proceso para la plataforma BPM
	 * @param instanceId id de la instancia de proceso
	 * @return clave del hilo
	 */
	private String generateKey(String processDefId, String instanceId) {
		
		return processDefId+KEYSEPARATOR+instanceId;
	}

	/**
	 * Genera el nombre del modulo correspondiente a un proceso, a partir del id en su diagrama.
	 * 
	 * @param processDefId Id 
	 * del proceso para la plataforma BPM
	 * @return
	 */
	private String generateModuleName(String processDefId) {

//		return BpmsCenter.CLASSPATH + "/" + BpmsCenter.MODULESPATH + "/module-" + BpmsCenter.getPlatformController().getProcessId(processDefId) + ".epl";
		return BpmsConfig.MODULESPATH + "/module-" + BpmsConfig.getPlatformController().getProcessId(processDefId) + ".epl";
	}
	
	/**
	 * Crea las declaraciones EPL de un proceso a partir de su modulo EPL y les adiciona los listeners especificados en el modulo.
	 * <p>
	 * Las declaraciones EPL de las medidas siguen patrones, a partir de los cuales pueden ser creadas teniendo en cuenta
	 * lo especificado para estas en el XML del proceso.
	 * 
	 * @param processDefId Id del proceso para la plataforma BPM
	 * @throws BpmsException 
	 */
	private void deployProcessModule(String processDefId) throws BpmsException {

		if (!this.addedProcess.containsKey(processDefId)) {
			
	      	log.info(
					"Adding process: " + processDefId);  		
			
			// despliega el modulo EPL del proceso
	        List<EPStatement> statements;
			try {
				
		        EPDeploymentAdmin deployAdmin = this.esperServiceProvider.getEPAdministrator().getDeploymentAdmin();
				Module module = deployAdmin.read(this.generateModuleName(processDefId));
		        DeploymentResult deployResult = deployAdmin.deploy(module, new DeploymentOptions());
		        statements = deployResult.getStatements();   
		        this.moduleNames.put(processDefId, module.getName());
			} catch (Exception e) {
				
				throw new BpmsException("Error desplegando modulo EPL. Verifica que el modulo EPL del proceso esta en la carpeta de las clases, en la subcarpeta indicada en bpms.properties y que se nombra module-<id del proceso>.epl." +
						" Proceso: "+processDefId);
			}
	        
	        // adiciona a las declaraciones EPL del modulo desplegado, los listeners que se especifican para cada una de ellas en el modulo
	        for (EPStatement statement : statements) {
	        	
	            Annotation[] annotations = statement.getAnnotations();                  
	            for (Annotation annotation : annotations) {                          
	            	if (annotation instanceof Tag) {                                  
	            		Tag tag = (Tag) annotation;                                  
	            		if (tag.name().equals("listeners")) {                                          
	            			String[] listeners = tag.value().split(",");                                          
	            			for (String listener : listeners) {                                                  
	            				Class<?> cl;
								try {
									cl = Class.forName(listener);
		            				Object obj = cl.newInstance();                                                 
		            				if (obj instanceof DataMeasureListener) {                                                          
		            					statement.addListener((DataMeasureListener) obj);                                                  
		            				} else 
		            				if (obj instanceof DataConditionMeasureListener) {                                                          
		            					statement.addListener((DataConditionMeasureListener) obj);                                                  
		            				} else 
			            			if (obj instanceof ActivityStartListener) {                                                          
			            				statement.addListener((ActivityStartListener) obj);                                                  
			            			} else 
				            		if (obj instanceof ActivityEndListener) {                                                          
				            			statement.addListener((ActivityEndListener) obj);                                                  
				            		} else 
				            		if (obj instanceof UserTaskAsignedUserListener) {                                                          
				            			statement.addListener((UserTaskAsignedUserListener) obj);                                                  
				            		} else 
					            	if (obj instanceof ProcessStartListener) {                                                          
					            		statement.addListener((ProcessStartListener) obj);                                                  
					            	} else 
						            if (obj instanceof ProcessEndListener) {                                                          
						            	statement.addListener((ProcessEndListener) obj);                                                  
						            }                                    
								} catch (Exception e) {

									throw new BpmsException("Error creando instancia de listener Esper. Verifica que la clase especificada existe." +
											" Proceso: "+processDefId +" Listener: "+listener);
								}                                                  
	            			}                                  
	            		}                          
	            	}                  
	            }          	
	        	
	        }          
		} else
	      	log.info(
					"Process is added already: " + processDefId);  		
	}
	
	/**
	 * Elimina las declaraciones EPL de un proceso.
	 * 
	 * @param processDefId Id del proceso para la plataforma BPM
	 * @throws BpmsException 
	 */
	private void removeProcessModule(String processDefId) throws BpmsException {
		
		log.info(
				"Removing process " + processDefId);  		
		if (this.addedProcess.containsKey(processDefId)) {
			
			// elimina el modulo EPL del proceso
			String moduleName = this.moduleNames.get(processDefId);
			this.moduleNames.remove(processDefId);
			EPDeploymentAdmin deployAdmin = this.esperServiceProvider.getEPAdministrator().getDeploymentAdmin();                  
			for (DeploymentInformation deploymentInformation : deployAdmin.getDeploymentInformation()) {                          
				if (deploymentInformation!=null && deploymentInformation.getModule()!=null &&
						deploymentInformation.getModule().getName()!=null &&
						deploymentInformation.getModule().getName().equals(moduleName)) {        
					
					try {
						deployAdmin.undeploy(deploymentInformation.getDeploymentId());
					} catch (DeploymentException e) {

						throw new BpmsException("Error eliminando modulo desplegado." +
								" Proceso: "+processDefId);
					}     
					break;
				}  
			}
		}                    
	}
	
	/**
	 * Asocia un hilo a una instancia de proceso.
	 * 
	 * @param processDefId Id del proceso para la plataforma BPM
	 * @param instanceId Id de la instancia de proceso
	 * @throws BpmsException 
	 */
	private void addInstance(String processDefId, String instanceId ) throws BpmsException {
		
		String key = this.generateKey(processDefId, instanceId);
		
		if (!this.runnables.containsKey(key)) {
			
	      	log.info(
					"Adding instance: " + key); 
				
			// crea el Runnable que se ejecutara en el hilo correspondiente a la instancia de proceso
	        SendRunnable sendRunnable = new SendRunnable(this.esperServiceProvider);
			
	        // si threadPool no ha sido creado, se crea con un hilo
	        if (this.threadPool==null)
	        	this.threadPool = Executors.newFixedThreadPool(10, this.threadFactory);
			
	        // adiciona un nuevo hilo
	        this.threadFactory.newThread(sendRunnable);
	        // envia el Runnable
	        this.threadPool.submit(sendRunnable);
	        
	        // conserva el Runnable de la instancia de proceso en el mapa runnables
	        this.runnables.put( key, sendRunnable);
	        
	        // despliega el modulo EPL del proceso, si aun no ha sido desplegado
	        if (this.addedProcess.containsKey(processDefId)) {
	        	
	        	this.addedProcess.get(processDefId).add(sendRunnable);
	        } else {

	        	this.deployProcessModule(processDefId);
	        	
	        	List<SendRunnable> list = new ArrayList<SendRunnable>();
	        	list.add(sendRunnable);
	        	this.addedProcess.put(processDefId, list);
	        }
	        	
		} else
	      	log.info(
					"Instance is added already: " + key);  		
		
    }
	
	/**
	 * Elimina el hilo asociado a una instancia de proceso.
	 * 
	 * @param processDefId Id del proceso para la plataforma BPM
	 * @param instanceId Id de la instancia de proceso
	 * @throws BpmsException 
	 */
	private void removeInstance(String processDefId, String instanceId ) throws BpmsException {
		
		String key = this.generateKey(processDefId, instanceId);
		
		log.info(
				"Removing instance " + key);  		
		
		if (this.runnables.containsKey(key)) {
			
			// detiene el controlador, si no hay mas instancias de procesos en ejecucion
			if (this.runnables.size()==1) {
				
				this.shutDown(processDefId);
			}
			else {
				
				// detiene el hilo de la instancia de proceso
				SendRunnable sendRunnable = this.runnables.get(key);
				sendRunnable.setShutdown();        	
				while (!sendRunnable.isShutdown()) {
	        		// espera hasta que el hilo efectivamente se haya detenido. El hilo no se detiene hasta que envia todos los eventos que tiene en cola
	        	}

				// elimina el Runnable del mapa de las instancias de proceso
				this.runnables.remove(key);
				// elimina el Runnable del mapa de los procesos
				this.addedProcess.get(processDefId).remove(sendRunnable);
				// elimina los modulos EPL del proceso, si ya no existen hilos asociados a sus instancias de proceso
				if (this.addedProcess.get(processDefId).size()==0) {
					
					this.removeProcessModule(processDefId);
					this.addedProcess.remove(processDefId);
				}
			}
		}
	}
	
	/**
	 * Recibe un evento que es enviado al hilo asociado con la instancia de proceso que lo genero. 
     * <p>
	 * Al llegar un evento desde una instancia de proceso, se inicializa el controlador si no habia ninguna instancia de 
	 * proceso en ejecucion; se adiciona su proceso si no habia instancias de este en ejecucion; si no existia ya, se crea 
	 * el hilo que recibira sus eventos; y finalmente se envia el evento al hilo de la instancia de proceso.
	 * <p>
	 * Posteriormente, si se trata de un evento de fin de proceso, se elimina el hilo de esa instancia de proceso, 
	 * se elimina el proceso si no tiene mas instancias en ejecucion y se detiene el controlador si no hay instancias en 
	 * ejecucion de otros procesos. 
	 * 
	 * @param event Evento recibido
	 * @throws BpmsException 
	 */
	public void sendEvent(Event event) throws BpmsException {

		log.info(
				"*---Sending an event from " + event.getProcessDefId() + "-" + event.getInstanceId() + "-" + event);  		

		String key = this.generateKey(event.getProcessDefId(), event.getInstanceId());
		
		// abre el controlador, si es la primera instancia de proceso que se adiciona
		if (this.runnables==null || this.runnables.size()==0) {

			this.open(); 	
		}
		
		// se adiciona la instancia de proceso, si es la primera vez que se recibe una accion desde esta
		if (!this.runnables.containsKey(key)) {
			
			this.addInstance(event.getProcessDefId(), event.getInstanceId());
		}
			
		// genera un evento a partir de la accion enviada y lo envia al hilo correspondiente a la instancia de proceso 
		// desde la cual se envio. No se envia el evento en el caso que se le haya indicado al hilo que se detenga
		if (!this.runnables.get( key ).isToBeShutdown()) {
			
			this.runnables.get( key ).pushEvent( event );
		}
		
		// si el evento que se envio es de fin de proceso, se elimina esa instancia de proceso
		if (event instanceof ProcessEndEvent) {
			
			this.removeInstance(event.getProcessDefId(), event.getInstanceId());
		}
	}
}
