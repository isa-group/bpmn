package es.us.isa.bpms.platform;

import java.util.List;

/**
 * Interfaz para interactuar con una plataforma BPM. 
 * <p>
 * Para utilizar el paquete bpms con una plataforma BPM hay que crear la clase 
 * <a href="../../../javadocs-activiti/es/us/isa/bpms/platform/activiti/PlatformController.html">PlatformController</a>
 * que implementa los metodos de esta interfaz para esa plataforma. Cada uno de los cuales recibe como parametro el objeto 
 * que permite interactuar con la plataforma en tiempo de ejecucion, y utilizandolo tiene que realiza las acciones que le 
 * corresponden.
 * 
 * @author Edelia Garcia Gonzalez
 * @version 1.0
 */
public interface PlatformControllerInterface {
	
	/**
	 * Obtiene el valor de una variable de la plataforma BPM
	 * 
	 * @param object Objeto de la plataforma BPM que permite interactuar con ella en tiempo de ejecucion
	 * @param variableName Nombre de la variable de plataforma
	 * @return Valor
	 */
	public abstract Object getVariableValue(Object object, String variableName);
	
	/**
	 * Da valor a una variable de la plataforma BPM
	 * 
	 * @param object Objeto de la plataforma BPM que permite interactuar con ella en tiempo de ejecucion
	 * @param variableName Nombre de la variable de la plataforma
	 * @param value Valor
	 */
	public abstract void setVariableValue(Object object, String variableName, Object value);
	
	/**
	 * Devuelve el id del diagrama del proceso que se esta ejecutando
	 * 
	 * @param processDefId Id del proceso en la plataforma BPM
	 * @return Id del proceso en el diagrama
	 */
	public abstract String getProcessId(String processDefId);
	
	/**
	 * Devuelve el id del diagrama del proceso que se esta ejecutando
	 * 
	 * @param object Objeto de la plataforma BPM que permite interactuar con ella en tiempo de ejecucion
	 * @return Id del proceso en el diagrama
	 */
	public abstract String getProcessId(Object object);
	
	/**
	 * Devuelve el id en la plataforma BPM del proceso que se esta ejecutando
	 * 
	 * @param object Objeto de la plataforma BPM que permite interactuar con ella en tiempo de ejecucion
	 * @return Id del proceso en la plataforma BPM
	 */
	public abstract String getProcessDefId(Object object);
	
	/**
	 * Devuelve el id de la instancia de proceso que se esta ejecutando
	 * 
	 * @param object Objeto de la plataforma BPM que permite interactuar con ella en tiempo de ejecucion
	 * @return Id de instancia de proceso
	 */
	public abstract String getInstanceId(Object object);
	
	/**
	 * Devuelve el id de la ejecucion
	 * 
	 * @param object Objeto de la plataforma BPM que permite interactuar con ella en tiempo de ejecucion
	 * @return Id de ejecucion
	 */
	public abstract String getExecutionId(Object object);
	
	/**
	 * Devuelve el id del despliegue del proceso
	 * 
	 * @param object Objeto de la plataforma BPM que permite interactuar con ella en tiempo de ejecucion
	 * @return Id del despliegue
	 */
	public abstract String getDeploymentId(Object object);
	
	/**
	 * Devuelve el id de la actividad que se esta ejecutando
	 * 
	 * @param object Objeto de la plataforma BPM que permite interactuar con ella en tiempo de ejecucion
	 * @return Id de la actividad
	 */
	public abstract String getActivityId(Object object);
	
	/**
	 * Devuelve el nombre de la actividad que se esta ejecutando
	 * 
	 * @param object Objeto de la plataforma BPM que permite interactuar con ella en tiempo de ejecucion
	 * @return Nombre de la actividad
	 */
	public abstract String getActivityName(Object object);
	
	/**
	 * Devuelve el tipo de la actividad que se esta ejecutando
	 * 
	 * @param object Objeto de la plataforma BPM que permite interactuar con ella en tiempo de ejecucion
	 * @return Tipo de la actividad
	 */
	public abstract String getActivityType(Object object);
	
	/**
	 * Devuelve la expresion RAL de una tarea, mediante la cual se determinar el usuario al que se le asigna esa tarea
	 * 
	 * @param object Objeto de la plataforma BPM que permite interactuar con ella en tiempo de ejecucion
	 * @return Expresion RAL de la tarea
	 */
	public abstract String getRalExpression(Object object);
	
	/**
	 * Devuelve el usuario que ejecuto una tarea
	 * 
	 * @param object Objeto de la plataforma BPM que permite interactuar con ella en tiempo de ejecucion
	 * @return Nombre de usuario
	 */
	public abstract String getUser(Object object);
	
	/**
	 * Asigna una tarea a un usuario 
	 * 
	 * @param object Objeto de la plataforma BPM que permite interactuar con ella en tiempo de ejecucion
	 * @param assignment Cadena que indica a quien se le asigna la tarea o quienes son candidatos
	 */
	public abstract void setUser(Object object, String assignment);
	
	/**
	 * Asigna una lista de usuarios candidatos a una tarea 
	 * 
	 * @param object Objeto de la plataforma BPM que permite interactuar con ella en tiempo de ejecucion
	 * @param users Lista de usuarios candidatos
	 */
	public abstract void addCandidateUsers(Object object, List<String> users);
	
}
