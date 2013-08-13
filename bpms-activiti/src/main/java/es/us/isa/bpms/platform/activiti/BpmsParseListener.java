package es.us.isa.bpms.platform.activiti;


import java.util.List;

import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.bpmn.parser.BpmnParseListener;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ScopeImpl;
import org.activiti.engine.impl.pvm.process.TransitionImpl;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.impl.util.xml.Element;
import org.activiti.engine.impl.variable.VariableDeclaration;

import es.us.isa.bpms.BpmsConfig;
import es.us.isa.bpms.platform.activiti.sendevent.ActivityEndSendEvent;
import es.us.isa.bpms.platform.activiti.sendevent.ActivityStartSendEvent;
import es.us.isa.bpms.platform.activiti.sendevent.ProcessEndSendEvent;
import es.us.isa.bpms.platform.activiti.sendevent.ProcessStartSendEvent;
import es.us.isa.bpms.platform.activiti.sendevent.UserTaskAsignedUserEvent;
import es.us.isa.bpms.platform.activiti.sendevent.UserTaskAssignmentEvent;
import es.us.isa.bpms.ral.RalCenter;


/**
 * Esta clase permite notificar a la clase 
 * <a href="../../../../javadocs/es/us/isa/bpms/BpmsCenter.html">BpmsCenter</a> del inicio y fin de proceso, asi como 
 * del inicio y fin de cada una de las actividades. De esta forma se obtiene toda la informacion necesaria para calcular 
 * las medidas del tipo CountMeasure, TimeMeasure y ElementConditionMeasure, y se puede manipular adecuadamente los
 * dataobjects de los procesos.  
 * <p>
 * Con esta clase, durante el parse de los procesos se adicionan eventos que se invocan al inicio y fin del 
 * proceso, y al inicio y fin de cada una de las actividades. Las clases de los eventos adicionados se encuentran en el 
 * paquete <a href="sendevent/package-summary.html">es.us.isa.bpms.platform.activiti.sendevent</a>. Estos con los 
 * que envian los mensajes de notificacion a la clase BpmsCenter.
 * <p>
 * Para utilizar el paquete <a href="../../../../javadocs/es/us/isa/bpms/package-summary.html">es.us.isa.bpms</a>
 * con otras plataformas BPM es necesario lanzar eventos semejantes a estos implementados para Activiti.
 * 
 * @author Edelia Garcia Gonzalez
 */
public class BpmsParseListener implements BpmnParseListener {

  /**
   * Adiciona los listeners para notificar a la clase BpmsCenter los eventos de inicio y fin de proceso.
   */
  @Override
  public void parseProcess(Element processElement, ProcessDefinitionEntity processDefinition) {

System.out.println("parseProcess 1");
	  processDefinition.addExecutionListener(org.activiti.engine.impl.pvm.PvmEvent.EVENTNAME_START, new ProcessStartSendEvent());
System.out.println("parseProcess 2");
	  processDefinition.addExecutionListener(org.activiti.engine.impl.pvm.PvmEvent.EVENTNAME_END, new ProcessEndSendEvent());
System.out.println("parseProcess 3");
  }

  /**
   * Adiciona los listeners para notificar a la clase BpmsCenter los eventos de inicio y fin de actividad.
   */
  @Override
  public void parseExclusiveGateway(Element exclusiveGwElement, ScopeImpl scope, ActivityImpl activity) {
	  
System.out.println("parseExclusiveGateway 1");
	  addActivitySendEvents(activity);
System.out.println("parseExclusiveGateway 2");
  }

  /**
   * Adiciona los listeners para notificar a la clase BpmsCenter los eventos de inicio y fin de actividad.
   */
  @Override
  public void parseInclusiveGateway(Element inclusiveGwElement, ScopeImpl scope, ActivityImpl activity) {
	  
System.out.println("parseInclusiveGateway 1");
	  addActivitySendEvents(activity);
System.out.println("parseInclusiveGateway 2");
  }

  /**
   * Adiciona los listeners para notificar a la clase BpmsCenter los eventos de inicio y fin de actividad.
   */
  @Override
  public void parseCallActivity(Element callActivityElement, ScopeImpl scope, ActivityImpl activity) {
	  
System.out.println("parseCallActivity 1");
	  addActivitySendEvents(activity);
System.out.println("parseCallActivity 2");
  }

  /**
   * Adiciona los listeners para notificar a la clase BpmsCenter los eventos de inicio y fin de actividad.
   */
  @Override
  public void parseManualTask(Element manualTaskElement, ScopeImpl scope, ActivityImpl activity) {
	  
System.out.println("parseManualTask 1");
	  addActivitySendEvents(activity);
System.out.println("parseManualTask 2");
  }

  /**
   * Adiciona los listeners para notificar a la clase BpmsCenter los eventos de inicio y fin de actividad.
   */
  @Override
  public void parseScriptTask(Element scriptTaskElement, ScopeImpl scope, ActivityImpl activity) {
	  
System.out.println("parseScriptTask 1");
	  addActivitySendEvents(activity);
System.out.println("parseScriptTask 2");
  }

  /**
   * Adiciona los listeners para notificar a la clase BpmsCenter los eventos de inicio y fin de actividad.
   */
  @Override
  public void parseTask(Element taskElement, ScopeImpl scope, ActivityImpl activity) {
	  
System.out.println("parseTask 1");
	  addActivitySendEvents(activity);
System.out.println("parseTask 2");
  }

  /**
   * Adiciona los listeners para notificar a la clase BpmsCenter los eventos de inicio y fin de actividad.
   */
  @Override
  public void parseUserTask(Element userTaskElement, ScopeImpl scope, ActivityImpl activity) {
	  
System.out.println("parseUserTask 1");
	addActivitySendEvents(activity);
		
System.out.println("parseUserTask 2");
	// el evento de asignacion a la tarea del usuario, o los usuarios o grupos candidatos
	TaskDefinition taskDefinition = ((UserTaskActivityBehavior) activity.getActivityBehavior()).getTaskDefinition();
	taskDefinition.addTaskListener(TaskListener.EVENTNAME_CREATE, new UserTaskAssignmentEvent());
	taskDefinition.addTaskListener(TaskListener.EVENTNAME_ASSIGNMENT, new UserTaskAsignedUserEvent());
	
System.out.println("parseUserTask 3");
	// conserva las expresiones RAL de las tareas del proceso
	if (BpmsConfig.RALACTIVE && taskDefinition.getAssigneeExpression()!=null) 
		RalCenter.getRalController().putRalExpression(
				activity.getProcessDefinition().getDeploymentId(), 
				activity.getId(), 
				taskDefinition.getAssigneeExpression().getExpressionText());
System.out.println("parseUserTask 4");
  }

  /**
   * Adiciona los listeners para notificar a la clase BpmsCenter los eventos de inicio y fin de actividad.
   * Indica a la service task cual es el metodo que tiene que ejecutar a continuacion de los listeners de inicio.
   */
  @Override
  public void parseServiceTask(Element serviceTaskElement, ScopeImpl scope, ActivityImpl activity) {
	  
System.out.println("parseServiceTask 1");
	  addActivitySendEvents(activity);
System.out.println("parseServiceTask 2");
  }

  /**
   * Adiciona los listeners para notificar a la clase BpmsCenter los eventos de inicio y fin de actividad.
   */
  @Override
  public void parseBusinessRuleTask(Element businessRuleTaskElement, ScopeImpl scope, ActivityImpl activity) {
	  
System.out.println("parseBusinessRuleTask 1");
	  addActivitySendEvents(activity);
System.out.println("parseBusinessRuleTask 2");
  }

  /**
   * Adiciona los listeners para notificar a la clase BpmsCenter los eventos de inicio y fin de actividad.
   */
  @Override
  public void parseSubProcess(Element subProcessElement, ScopeImpl scope, ActivityImpl activity) {
	  
System.out.println("parseSubProcess 1");
	  addActivitySendEvents(activity);
System.out.println("parseSubProcess 2");
  }

  /**
   * Adiciona los listeners para notificar a la clase BpmsCenter los eventos de inicio y fin de actividad.
   */
  @Override
  public void parseStartEvent(Element startEventElement, ScopeImpl scope, ActivityImpl activity) {
	  
System.out.println("parseStartEvent 1");
	  addActivitySendEvents(activity);
System.out.println("parseStartEvent 2");
  }

  /**
   * Adiciona los listeners para notificar a la clase BpmsCenter los eventos de inicio y fin de actividad.
   */
  @Override
  public void parseSendTask(Element sendTaskElement, ScopeImpl scope, ActivityImpl activity) {
	  
System.out.println("parseSendTask 1");
	  addActivitySendEvents(activity);
System.out.println("parseSendTask 2");
  }

  /**
   * Adiciona los listeners para notificar a la clase BpmsCenter los eventos de inicio y fin de actividad.
   */
  @Override
  public void parseEndEvent(Element endEventElement, ScopeImpl scope, ActivityImpl activity) {
	  
System.out.println("parseEndEvent 1");
	  addActivitySendEvents(activity);
System.out.println("parseEndEvent 2");
  }

  /**
   * Adiciona los listeners para notificar a la clase BpmsCenter los eventos de inicio y fin de actividad.
   */
  protected void addActivitySendEvents(ActivityImpl activity) {
	  
System.out.println("addActivitySendEvents 1");
	  activity.addExecutionListener(org.activiti.engine.impl.pvm.PvmEvent.EVENTNAME_START, new ActivityStartSendEvent(), 0);
      activity.addExecutionListener(org.activiti.engine.impl.pvm.PvmEvent.EVENTNAME_END, new ActivityEndSendEvent());
System.out.println("addActivitySendEvents 2");
  }

  /**
   * Metodos que hay que definir pero que no se implementan
   */
  @Override
  public void parseParallelGateway(Element parallelGwElement, ScopeImpl scope, ActivityImpl activity) {
  }

  @Override
  public void parseBoundaryTimerEventDefinition(Element timerEventDefinition, boolean interrupting, ActivityImpl timerActivity) {
  }

  @Override
  public void parseBoundaryErrorEventDefinition(Element errorEventDefinition, boolean interrupting, ActivityImpl activity, ActivityImpl nestedErrorEventActivity) {
  }

  @Override
  public void parseIntermediateTimerEventDefinition(Element timerEventDefinition, ActivityImpl timerActivity) {
  }

  @Override
  public void parseProperty(Element propertyElement, VariableDeclaration variableDeclaration, ActivityImpl activity) {
  }

  @Override
  public void parseSequenceFlow(Element sequenceFlowElement, ScopeImpl scopeElement, TransitionImpl transition) {
  }

  @Override
  public void parseRootElement(Element rootElement, List<ProcessDefinitionEntity> processDefinitions) {
  }
  
  @Override
  public void parseMultiInstanceLoopCharacteristics(Element activityElement, 
          Element multiInstanceLoopCharacteristicsElement, ActivityImpl activity) {
	  // Remove any history parse listeners already attached: the Multi instance behavior will
	  // call them for every instance that will be created
  }

  @Override
  public void parseReceiveTask(Element receiveTaskElement, ScopeImpl scope, ActivityImpl activity) {
  }

  @Override
  public void parseIntermediateSignalCatchEventDefinition(Element signalEventDefinition, ActivityImpl signalActivity) {
  }

  @Override
  public void parseIntermediateMessageCatchEventDefinition(Element messageEventDefinition, ActivityImpl nestedActivity) {
  }

  @Override
  public void parseBoundarySignalEventDefinition(Element signalEventDefinition, boolean interrupting, ActivityImpl signalActivity) {
  }

  @Override
  public void parseEventBasedGateway(Element eventBasedGwElement, ScopeImpl scope, ActivityImpl activity) {
  }

  @Override
  public void parseTransaction(Element transactionElement, ScopeImpl scope, ActivityImpl activity) {
  }

  @Override
  public void parseCompensateEventDefinition(Element compensateEventDefinition, ActivityImpl compensationActivity) {
  }

  @Override
  public void parseIntermediateThrowEvent(Element intermediateEventElement, ScopeImpl scope, ActivityImpl activity) {
  }

  @Override
  public void parseIntermediateCatchEvent(Element intermediateEventElement, ScopeImpl scope, ActivityImpl activity) {
  }

  @Override
  public void parseBoundaryEvent(Element boundaryEventElement, ScopeImpl scopeElement, ActivityImpl nestedActivity) {
  }

  @Override
  public void parseBoundaryMessageEventDefinition(Element element, boolean interrupting, ActivityImpl messageActivity) {
  }
}

