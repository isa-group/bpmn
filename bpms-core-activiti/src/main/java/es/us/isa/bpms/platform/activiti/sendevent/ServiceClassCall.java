package es.us.isa.bpms.platform.activiti.sendevent;


import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

import es.us.isa.bpms.dataobject.DataobjectCenter;


/**
 * Ejecuta un metodo en una service task.
 * <p>
 * Indica a la clase <a href="../../../../../javadocs/es/us/isa/bpms/BpmsCenter.html">BpmsCenter</a> que se ejecute un metodo en una
 * service task (<a href="../../../../../javadocs/es/us/isa/bpms/BpmsCenter.html#executeServiceClassCall(java.lang.Object)">executeServiceClassCall</a>) 
 * pasando como parametro el objeto que permite interactuar con la plataforma BPM. En el caso de Activiti, esta clase se utiliza en 
 * el XML del proceso para configurar la propiedad Service Class en todas las service tasks. En otra plataforma BPM habria que 
 * utilizarlo en una forma semejante. 
 * 
 * @author Edelia
 * @see es.us.isa.bpms.BpmsCenter
 */
public class ServiceClassCall implements JavaDelegate { 

	/**
	 * Indica a BpmsCenter que ejecute un metodo. Es utilizado en las tareas del tipo service task.
	 * <p>
	 * Envia el mensaje BpmsCenter.<a href="../../../../../javadocs/es/us/isa/bpms/BpmsCenter.html#executeServiceClassCall(java.lang.Object)">ServiceClassCall</a>(execution)
	 * 
	 * @param execution Objeto que permite interactuar con Activiti en tiempo de ejecucion
	 */
	@Override
	public void execute(DelegateExecution execution) throws Exception {

		// Ejecuta el metodo 
		DataobjectCenter.executeServiceClassCall(execution);
	} 
}
