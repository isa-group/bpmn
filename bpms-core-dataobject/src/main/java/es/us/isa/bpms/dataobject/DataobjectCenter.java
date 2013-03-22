package es.us.isa.bpms.dataobject;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.us.isa.bpms.BpmsConfig;
import es.us.isa.bpms.BpmsException;

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
public class DataobjectCenter {

    // log de la aplicacion
    private static final Log log = LogFactory.getLog(DataobjectCenter.class);
	
	// controlador de los dataobjects
	private static DataobjectController dataobjectController = null;
	
    static {
    	
    	if (dataobjectController==null) {
    		
    		dataobjectController = new DataobjectController();
    		
    		restore();
    	}
    }  
    
    private static void restore() {
    	
    	try {
    		
			// restaura el sistema, en el caso que exista un backup. Utilizado para el caso de fallas en el servidor
			File file = new File( BpmsConfig.BACKUPPATH+"dataobject-"+BpmsConfig.BACKUPFILE);
			if (file.exists()) {
				
				ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(BpmsConfig.BACKUPPATH + BpmsConfig.BACKUPFILE));
				
				dataobjectController = (DataobjectController) entrada.readObject();

				entrada.close();
			}
			
    	} catch (Exception e) {
			
			e.printStackTrace();
		}     
    } 
    
    /**
     * Guarda la informacion para recuperar el sistema en el caso de fallas en el servidor
     */
    private static void backup() {
    	
        try {
        	
			File file = new File( new File(BpmsConfig.BACKUPPATH), "dataobject-"+BpmsConfig.BACKUPFILE);
			if (file.exists())
				file.delete();
			
            ObjectOutputStream salida=new ObjectOutputStream(new FileOutputStream(BpmsConfig.BACKUPPATH + BpmsConfig.BACKUPFILE));
            
            salida.writeObject(dataobjectController);

            salida.close();
		} catch (IOException e) {
			
			e.printStackTrace();
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

	    	// inicializa el espacio de dataobjects del proceso
	    	dataobjectController.addProcess(object);
	    	
	    	// hace el resguardo para el caso de fallas
	    	backup();
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
    	
		// elimina el espacio de dataobjects del proceso
		dataobjectController.removeProcess(object);
		
		// hace el resguardo para el caso de fallas
		backup(); 
    	
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
			
	    	// ejecuta el evento de inicio de la actividad
			dataobjectController.executeStartEvent(object);

			// hace el resguardo para el caso de fallas
	    	backup();
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
			
	    	// ejecuta el evento de fin de la actividad
			dataobjectController.executeEndEvent(object);
	    	
	    	// hace el resguardo para el caso de fallas
	    	backup();
		} catch (BpmsException e) {
			
	      	log.error(
					e.getMessage());  		
		} 
    	
    }

    /**
     * En una tarea de servicio, ejecuta un metodo del espacio de dataobjects del proceso.
     * 
     * @param object Objeto de la plataforma BPM que permite interactuar con ella en tiempo de ejecucion
     */
    public static void executeServiceClassCall(Object object) {
    	
		try {
			
			dataobjectController.executeServiceClassCall(object);
		} catch (BpmsException e) {
			
	      	log.error(
					e.getMessage());  		
		}
    }

}
