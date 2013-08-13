package es.us.isa.bpms.platform.activiti.sendevent;


import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

import es.us.isa.bpms.BpmsConfig;
import es.us.isa.bpms.dataobject.DataobjectCenter;
import es.us.isa.bpms.ppinot.PpinotCenter;



/**
 * Ejecuta un evento de fin de proceso.
 * <p>
 * Indica a la clase <a href="../../../../../javadocs/es/us/isa/bpms/BpmsCenter.html">BpmsCenter</a> que se ejecute un evento de fin de proceso 
 * (<a href="../../../../../javadocs/es/us/isa/bpms/BpmsCenter.html#executeProcessEndSendEvent(java.lang.Object)">executeProcessEndSendEvent</a>) 
 * pasando como parametro el objeto que permite interactuar con la plataforma BPM. Para usar el paquete 
 * <a href="../../../../../javadocs/es/us/isa/bpms/package-summary.html">es.us.isa.bpms</a>
 * con otra plataforma hay que invocar este metodo en el momento que se finaliza una instancia de proceso. 
 * 
 * @author Edelia
 * @see es.us.isa.bpms.BpmsCenter
 */
public class ProcessEndSendEvent implements ExecutionListener {

	/**
	 * Notifica a BpmsCenter de un evento de fin de proceso.
	 * <p>
	 * Envia el mensaje BpmsCenter.<a href="../../../../../javadocs/es/us/isa/bpms/BpmsCenter.html#executeProcessEndSendEvent(java.lang.Object)">executeProcessEndSendEvent</a>(execution)
	 * 
	 * @param execution Objeto que permite interactuar con Activiti en tiempo de ejecucion
	 */
	@Override
	public void notify(DelegateExecution execution) {

		// Ejecuta el evento 
	  	DataobjectCenter.executeProcessEndSendEvent(execution);
    	if (BpmsConfig.PPINOTACTIVE)
    		PpinotCenter.executeProcessEndSendEvent(execution);
	}
}

