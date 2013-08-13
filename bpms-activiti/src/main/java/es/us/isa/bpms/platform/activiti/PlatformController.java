package es.us.isa.bpms.platform.activiti;

import java.util.List;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.VariableScope;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;

import es.us.isa.bpms.platform.PlatformControllerInterface;

/**
 * Clase que implementa la interfaz PlatformControllerInterface para interactuar con Activiti. Cada uno de sus metodos 
 * recibe como parametro el objeto que permite interactuar con Activiti en tiempo de ejecucion, y utilizandolo realiza 
 * las acciones que le corresponden.
 * <p>
 * La clase <a href="../../../../javadocs/es/us/isa/bpms/BpmsCenter.html">BpmsCenter</a> crea una instancia de esta clase que es utilizada 
 * internamente en el paquete es.us.isa.bpms. El usuario final que implementa un proceso no necesita acceder a esta clase.
 *
 * @author Edelia Garcia Gonzalez
 * @version 1.0
 */
public final class PlatformController implements PlatformControllerInterface {

	/**
	 * Obtiene el valor de una variable de Activiti
	 * 
	 * @param object Objeto de la plataforma BPM que permite interactuar con ella en tiempo de ejecucion
	 * @param variableName Nombre de la variable de plataforma
	 * @return Valor
	 */
	@Override
	public Object getVariableValue(Object object, String variableName) {
		// obtiene el valor de la variable Activiti llamada variableName
		VariableScope variableScope = (VariableScope) object;
		return variableScope.getVariable(variableName);
	}
	
	/**
	 * Da valor a una variable de Activiti
	 * 
	 * @param object Objeto de la plataforma BPM que permite interactuar con ella en tiempo de ejecucion
	 * @param variableName Nombre de la variable de la plataforma
	 * @param value Valor
	 */
	@Override
	public void setVariableValue(Object object, String variableName, Object value) {
		
		// Da valor a la variable Activiti llamada variableName
		VariableScope variableScope = (VariableScope) object;
		variableScope.setVariable( variableName, value);
	}

	/**
	 * Devuelve el id en el diagrama del proceso que se esta ejecutando
	 * 
	 * @param processDefId Id del proceso en Activiti
	 * @return Id del proceso en el diagrama
	 */
	@Override
	public String getProcessId(String processDefId) {

		String[] list = processDefId.split(":");
		return list[0];
	}

	/**
	 * Devuelve el id en el diagrama del proceso que se esta ejecutando 
	 * 
	 * @param object Objeto de Activiti que permite interactuar con este en tiempo de ejecucion
	 * @return Id del proceso en el diagrama
	 */
	@Override
	public String getProcessId(Object object) {
		
		String[] list = this.getProcessDefId(object).split(":");
		return list[0];
	}

	/**
	 * Devuelve el id en Activiti del proceso que se esta ejecutando
	 * 
	 * @param object Objeto de Activiti que permite interactuar con este en tiempo de ejecucion
	 * @return Id del proceso en Activiti
	 */
	@Override
	public String getProcessDefId(Object object) {

		if (object instanceof DelegateTask) {

			return ((DelegateTask) object).getProcessDefinitionId();
		} else
		if (object instanceof DelegateExecution) {

			ExecutionEntity executionEntity = Context.getCommandContext().getExecutionManager().findExecutionById(((DelegateExecution) object).getId());
			return executionEntity.getProcessDefinitionId();
		} else 
		if (object instanceof ExecutionEntity) {
			
			return ((ExecutionEntity) object).getProcessDefinitionId();
		} else
			return "";
	}

	/**
	 * Devuelve el id de la instancia de proceso que se esta ejecutando
	 * 
	 * @param object Objeto de Activiti que permite interactuar con este en tiempo de ejecucion
	 * @return Id de instancia de proceso
	 */
	@Override
	public String getInstanceId(Object object) {

		if (object instanceof DelegateTask) {
			
			DelegateTask delegateTask = (DelegateTask) object;
			return delegateTask.getProcessInstanceId();
		} else
		if (object instanceof DelegateExecution) {
			
			DelegateExecution delegateExecution = (DelegateExecution) object;
			return delegateExecution.getProcessInstanceId();
		} else 
		if (object instanceof ExecutionEntity) {
			
			return ((ExecutionEntity) object).getProcessInstanceId();
		} else
			return "";
	}

	/**
	 * Devuelve el id de la ejecucion
	 * 
	 * @param object Objeto de Activiti que permite interactuar con este en tiempo de ejecucion
	 * @return Id de ejecucion
	 */
	@Override
	public String getExecutionId(Object object) {

		if (object instanceof DelegateTask) {
			
			return ((DelegateTask) object).getExecutionId();
		} else
		if (object instanceof DelegateExecution) {
			
			ExecutionEntity executionEntity = Context.getCommandContext().getExecutionManager().findExecutionById(((DelegateExecution) object).getId());
			return executionEntity.getId();
		} else 
		if (object instanceof ExecutionEntity) {
			
			return ((ExecutionEntity) object).getId();
		} else
			return "";
	}

	/**
	 * Devuelve el id en Activiti del despliegue del proceso que se esta ejecutando
	 * 
	 * @param object Objeto de Activiti que permite interactuar con este en tiempo de ejecucion
	 * @return Id del despliegue
	 */
	@Override
	public String getDeploymentId(Object object) {

		if (object instanceof DelegateTask) {

			ExecutionEntity executionEntity = Context.getCommandContext().getExecutionManager().findExecutionById(((DelegateTask) object).getExecution().getId());
			return Context.getCommandContext().getProcessDefinitionManager().findLatestProcessDefinitionById(executionEntity.getProcessDefinitionId()).getDeploymentId();
		} else
		if (object instanceof DelegateExecution) {

			ExecutionEntity executionEntity = Context.getCommandContext().getExecutionManager().findExecutionById(((DelegateExecution) object).getId());
			return Context.getCommandContext().getProcessDefinitionManager().findLatestProcessDefinitionById(executionEntity.getProcessDefinitionId()).getDeploymentId();
		} else 
		if (object instanceof ExecutionEntity) {
			
			ExecutionEntity executionEntity = (ExecutionEntity) object;
			return Context.getCommandContext().getProcessDefinitionManager().findLatestProcessDefinitionById(executionEntity.getProcessDefinitionId()).getDeploymentId();
		} else
			return "";
	}
	
	/**
	 * Devuelve el id de la actividad que se esta ejecutando
	 * 
	 * @param object Objeto de la plataforma BPM que permite interactuar con ella en tiempo de ejecucion
	 * @return Id de la actividad
	 */
	@Override
	public String getActivityId(Object object) {

		if (object instanceof DelegateTask) {
			
			ExecutionEntity executionEntity = Context.getCommandContext().getExecutionManager().findExecutionById(
					((DelegateTask) object).getExecution().getId() );

			return executionEntity.getActivityId();
		} else
		if (object instanceof DelegateExecution) {
			
			ExecutionEntity executionEntity = Context.getCommandContext().getExecutionManager().findExecutionById(
					((DelegateExecution) object).getId());
			return executionEntity.getActivityId();
		} else 
		if (object instanceof ExecutionEntity) {
			
			return ((ExecutionEntity) object).getActivityId();
		} else
			return "";
	}
	
	/**
	 * Devuelve el nombre de la actividad que se esta ejecutando 
	 * 
	 * @param object Objeto de la plataforma BPM que permite interactuar con ella en tiempo de ejecucion
	 * @return Nombre de la actividad
	 */
	@Override
	public String getActivityName(Object object) {

		if (object instanceof DelegateTask) {
			
			ExecutionEntity executionEntity = Context.getCommandContext().getExecutionManager().findExecutionById(
					((DelegateTask) object).getExecution().getId() );
			return (String) executionEntity.getActivity().getProperty("name");
		} else
		if (object instanceof DelegateExecution) {
			
			ExecutionEntity executionEntity = Context.getCommandContext().getExecutionManager().findExecutionById(
					((DelegateExecution) object).getId());
			return (String) executionEntity.getActivity().getProperty("name");
		} else 
		if (object instanceof ExecutionEntity) {
			
			return (String) ((ExecutionEntity) object).getActivity().getProperty("name");
		} else
			return "";
	}
	
	/**
	 * Devuelve el tipo de la actividad que se esta ejecutando
	 * 
	 * @param object Objeto de la plataforma BPM que permite interactuar con ella en tiempo de ejecucion
	 * @return Tipo de la actividad
	 */
	@Override
	public String getActivityType(Object object) {

		if (object instanceof DelegateTask) {
			
			ExecutionEntity executionEntity = Context.getCommandContext().getExecutionManager().findExecutionById(
					((DelegateTask) object).getExecution().getId() );
			return (String) executionEntity.getActivity().getProperty("type");
		} else
		if (object instanceof DelegateExecution) {
			
			ExecutionEntity executionEntity = Context.getCommandContext().getExecutionManager().findExecutionById(
					((DelegateExecution) object).getId());
			return (String) executionEntity.getActivity().getProperty("type");
		} else 
		if (object instanceof ExecutionEntity) {
			
			return (String) ((ExecutionEntity) object).getActivity().getProperty("type");
		} else
			return "";
	}

	/**
	 * Devuelve la expresion RAL de una tarea, mediante la cual se determina el usuario al que se le asigna esa tarea.
	 * En Activiti se pone directamente en el xml, en el campo para indicar el usuario al que se le asigna la tarea
	 * 
	 * @param object Objeto de la plataforma BPM que permite interactuar con ella en tiempo de ejecucion
	 * @return Expresion RAL de la tarea
	 */
	@Override
	public String getRalExpression(Object object) {

		if (object instanceof DelegateTask) {
			
			return ((DelegateTask) object).getAssignee();
		} else
			return "";
		
	}
	
	/**
	 * Devuelve el usuario que ejecuto una tarea
	 * 
	 * @param object Objeto de la plataforma BPM que permite interactuar con ella en tiempo de ejecucion
	 * @return Nombre de usuario
	 */
	@Override
	public String getUser(Object object) {
		
		if (object instanceof DelegateTask) {

//System.out.println("**************************** "+((DelegateTask) object).getOwner()+" - "+((DelegateTask) object).getAssignee());
			return ((DelegateTask) object).getAssignee();
		} else
			return "";
		
	}

	/**
	 * Asigna una tarea a un usuario, o a una lista de usuarios o a una lista de grupos candidatos 
	 * 
	 * @param object Objeto de la plataforma BPM que permite interactuar con ella en tiempo de ejecucion
	 * @param assignment Cadena que indica a quien se le asigna la tarea o quienes son candidatos
	 */
	@Override
	public void setUser(Object object, String assignment) {

		if (object instanceof DelegateTask) {
			
			((DelegateTask) object).setAssignee(assignment);
		} 
	}

	/**
	 * Asigna una lista de usuarios candidatos a una tarea 
	 * 
	 * @param object Objeto de la plataforma BPM que permite interactuar con ella en tiempo de ejecucion
	 * @param users Lista de usuarios candidatos
	 */
	@Override
	public void addCandidateUsers(Object object, List<String> users) {
		
		if (object instanceof DelegateTask) {
			
			for (String user : users)
				((DelegateTask) object).addCandidateUser(user);
		} 
	}	
}
