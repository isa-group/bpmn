package es.us.isa.bpms.platform.activiti.sendevent;


import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import es.us.isa.bpms.BpmsConfig;
import es.us.isa.bpms.ppinot.PpinotCenter;
import es.us.isa.bpms.ral.RalCenter;



/**
 * Ejecuta de asignacion de usuario a una actividad.
 * <p>
 * Indica a la clase <a href="../../../../../javadocs/es/us/isa/bpms/BpmsCenter.html">BpmsCenter</a> que se ejecute un evento de asignacion de usuario a una actividad 
 * (<a href="../../../../../javadocs/es/us/isa/bpms/BpmsCenter.html#executeActivityUserAssignmentEvent(java.lang.Object)">executeActivityUserAssignmentEvent</a>) 
 * pasando como parametro el objeto que permite interactuar con la plataforma BPM. Para usar el paquete 
 * <a href="../../../../../javadocs/es/us/isa/bpms/package-summary.html">es.us.isa.bpms</a>
 * con otra plataforma hay que invocar este metodo cuando se asigna usuario a una actividad. 
 * 
 * @author Edelia
 * @see es.us.isa.bpms.BpmsCenter
 */
public class UserTaskAsignedUserEvent implements TaskListener {

	/**
	 * Notifica a BpmsCenter de un evento de asignacion de usuario a una actividad.
	 * <p>
	 * Envia el mensaje BpmsCenter.<a href="../../../../../javadocs/es/us/isa/bpms/BpmsCenter.html#executeActivityUserAssignmentEvent(java.lang.Object)">executeActivityUserAssignmentEvent</a>(execution)
	 * 
	 * @param execution Objeto que permite interactuar con Activiti en tiempo de ejecucion
	 */
	@Override
	public void notify(DelegateTask delegate) {

		// Ejecuta el evento
    	if (BpmsConfig.RALACTIVE) 
		   	RalCenter.executeUserTaskAsignedUserEvent(delegate);
    	if (BpmsConfig.PPINOTACTIVE) 
		   	PpinotCenter.executeUserTaskAsignedUserEvent(delegate);
	}
}

