package es.us.isa.bpms.platform.activiti.sendevent;


import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import es.us.isa.bpms.BpmsConfig;
import es.us.isa.bpms.ral.RalCenter;



/**
 * Ejecuta un evento de asignacion a una tarea del usuario, o los usuarios o grupos candidatos.
 * <p>
 * Indica a la clase <a href="../../../../../javadocs/es/us/isa/bpms/BpmsCenter.html">BpmsCenter</a> que se ejecute un evento de asignacion de usuario a una tarea 
 * (<a href="../../../../../javadocs/es/us/isa/bpms/BpmsCenter.html#executeUserTaskAssignmentEvent(java.lang.Object)">executeUserTaskAssignmentEvent</a>) 
 * pasando como parametro el objeto que permite interactuar con la plataforma BPM. Para usar el paquete 
 * <a href="../../../../../javadocs/es/us/isa/bpms/package-summary.html">es.us.isa.bpms</a>
 * con otra plataforma hay que invocar este metodo en todas las ocasiones que se asigne el usuario a una tarea. 
 * 
 * @author Edelia
 * @see es.us.isa.bpms.BpmsCenter
 */
public class UserTaskAssignmentEvent implements TaskListener {

	/**
	 * Notifica a BpmsCenter de un evento de asignacion de usuario a una tarea.
	 * <p>
	 * Envia el mensaje BpmsCenter.<a href="../../../../../javadocs/es/us/isa/bpms/BpmsCenter.html#executeUserTaskAssignmentEvent(java.lang.Object)">executeUserTaskAssignmentEvent</a>(execution)
	 * 
	 * @param delegate Objeto que permite interactuar con Activiti en tiempo de ejecucion
	 */
	@Override
	public void notify(DelegateTask delegate) {

		// Ejecuta el evento
    	if (BpmsConfig.RALACTIVE) 
    		RalCenter.executeUserTaskAssignmentEvent(delegate);
	}
}

