package es.us.isa.bpms;

import java.io.File;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.us.isa.bpms.platform.PlatformControllerInterface;


@SuppressWarnings("unchecked")
public class BpmsConfig {

    // log de la aplicacion
    private static final Log log = LogFactory.getLog(BpmsConfig.class);

    /**
     * Formato de las fechas
     */
	public static String FORMATDATE = null;
	/**
	 * Formato de fecha y hora
	 */
	public static String FORMATDATEHOUR;
	/**
	 * Año de inicio para los reportes
	 */
	public static Integer STARTYEAR;
	/**
	 * Si se envia eventos a Esper o no 
	 */
	public static Boolean PPINOTACTIVE;
	/**
	 * Si esta activa la asignacion de usuarios mediante RAL 
	 */
	public static Boolean RALACTIVE;
	/**
	 * URI de Esper
	 */
	public static String ENGINEURI;
	/**
	 * Nombre de la carpeta de los modulos EPL
	 */
	public static String MODULESPATH;
	/**
	 * Identificador de evento de inicio de una actividad, utilizado en las anotaciones 
	 * <a href="dataobject/annotations/ObjectUpdate.html">ObjectUpdate</a> y 
	 * <a href="dataobject/annotations/VariableUpdate.html">VariableUpdate</a>
	 */
	public static String STARTEVENT;
	/**
	 * Identificador de evento de asignacion de usuario a una actividad, utilizado en las anotaciones  
	 * <a href="dataobject/annotations/ObjectUpdate.html">ObjectUpdate</a> y 
	 * <a href="dataobject/annotations/VariableUpdate.html">VariableUpdate</a>
	 */
	public static String ASSIGNMENTEVENT;
	/**
	 * Identificador de evento de fin de actividad, utilizado en las anotaciones ObjectUpdate y VariableUpdate
	 */
	public static String ENDEVENT;
	/**
	 * Identificador de actualizacion de dataobject, utilizado en las anotaciones ObjectUpdate y VariableUpdate
	 */
	public static String DATAOBJECTUPDATE;
	/**
	 * Identificador de actualizacion de clase de dataobjects, utilizado en las anotaciones ObjectUpdate y VariableUpdate
	 */
	public static String CLASSUPDATE;
	/**
	 * Identificador de actualizacion de coleccion de dataobjects, utilizado en las anotaciones ObjectUpdate y VariableUpdate
	 */
	public static String COLLECTIONUPDATE;
	/**
	 * Identificador de la plataforma BPM con la que se enlaza 
	 */
	public static String BPMPLATFORM;
	/**
	 * La url donde se encuentran los .owl de los procesos
	 */
	public static String ORGANIZATIONURL;
	/**
	 * Nombre del archivo base .owl
	 */
	public static String ORGANIZATIONOWLNAME;
	/**
	 * La carpeta donde se conserva la informacion para recuperar el sistema en el caso que se apague el servidor 
	 */
	public static String BACKUPPATH;
	/**
	 * Carpeta de las clases
	 */
	public static String CLASSPATH;
	/**
	 * La carpeta donde se crean los archivos temporales para procesar las expresiones RAL
	 */
	public static String TMPPATH;
	/**
	 * Driver de la conexion a la BD 
	 */
	public static String DBDRIVER;
	/**
	 * Url de la conexion a la BD 
	 */
	public static String DBURL;
	/**
	 * Usuario de la conexion a la BD 
	 */
	public static String DBUSERNAME;
	/**
	 * Contraseña de la conexion a la BD 
	 */
	public static String DBPASSWORD;
	/**
	 * Archivo backup
	 */
	public static String BACKUPFILE = "bpms.obj";
	
	// controlador de la plataforma BPM
	private static Object platformController;
    
	// clase del controlador de la plataforma BPM
	private static Class<? extends PlatformControllerInterface> platformControllerClass;
	
    static {

    	if (FORMATDATE==null) {
    		
	    	try {
	
		   		// da valor a las constantes a partir del archivo de configuracion
	     		Configuration config = new XMLConfiguration("bpms.cfg.xml");
	
	     		FORMATDATE = config.getString("formatdate");
	    		FORMATDATEHOUR = config.getString("formatdatehour");
	    		STARTYEAR = config.getInt("startyear");
	    		PPINOTACTIVE = config.getBoolean("ppinotactive");
	    		RALACTIVE = config.getBoolean("ralactive");
	    		ENGINEURI = config.getString("engineuri");
	    		MODULESPATH = config.getString("modulespath");
	    		STARTEVENT = config.getString("startevent");
	    		ASSIGNMENTEVENT = config.getString("assignmentevent");
	    		ENDEVENT = config.getString("endevent");
	    		DATAOBJECTUPDATE = config.getString("dataobjectupdate");
	    		CLASSUPDATE = config.getString("classupdate");
	    		COLLECTIONUPDATE = config.getString("collectionupdate");
	    		BPMPLATFORM = config.getString("bpmplatform");
	    		ORGANIZATIONURL = config.getString("organizationurl");
	    		ORGANIZATIONOWLNAME = config.getString("organizationowlname");
	    		BACKUPPATH = config.getString("backuppath");
	    		CLASSPATH = config.getString("basedir")+"/WEB-INF/classes";
	    		TMPPATH = config.getString("tmppath");
	
	    		DBDRIVER = config.getString("database[@driver]");
	    		DBURL = config.getString("database[@url]");
	    		DBUSERNAME = config.getString("database[@username]");
	    		DBPASSWORD = config.getString("database[@password]");

				// si no existe, crea la carpeta backup 
	    		File backuppath = new File(BACKUPPATH);
	    		if (!backuppath.exists())
	    			backuppath.mkdirs();

				// si no existe, crea la carpeta temporal 
				File tmpFile = new File(TMPPATH);
				if (!tmpFile.exists())
					tmpFile.mkdirs();

	    		// crea el controlador de la plataforma BPM
	    		String pack = "es.us.isa.bpms.platform." + BPMPLATFORM;
	    		Class<?> cl;
				try {
					cl = Class.forName(pack + ".PlatformController");
		    		platformControllerClass = (Class<? extends PlatformControllerInterface>) cl;
		    		platformController = platformControllerClass.newInstance();
				} catch (Exception e) {
					
			      	log.error(
							"Error enlazando con la plataforma BPM. Verifica que exista un subpaquete nombrado como la plataforma indicada en la configuracion y que tenga la clase PlatformController. Paquete:"+ pack);
				}
				
	    	} catch (ConfigurationException e) {             

	          	log.info(
						"Error cargando la configuracion. Verifica que el archivo bpms.properties esta en la carpeta de las clases.");  		
	    	} catch (Exception e) {
				
				e.printStackTrace();
			}     
    	}
    }  	
	
    /**
     * Devuelve el controlador de la plataforma BPM. Es utilizado internamente en el paquete.
     * 
     * @return Controlador
     */
	public static PlatformControllerInterface getPlatformController() {
    	
    	return platformControllerClass.cast(platformController);
    }
	
	public static String cleanProcessDefId(String processDefId) {
		
		return processDefId.replace(":", "");
	}

}
