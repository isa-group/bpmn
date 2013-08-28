package es.us.isa.bpmn.handler;

import es.us.isa.bpmn.xmlClasses.bpmn20.*;

import javax.xml.bind.JAXBElement;
import java.util.*;

/**
 * Class that loads XML BPMN 2.0 models
 *
 * @author resinas
 * @author Edelia
 *
 */
public class Bpmn20ModelHandlerImpl extends AbstractModelHandler implements Bpmn20ModelHandler {

    private Map<String, TProcess> processes;
    private Map<String, TProcess> processOfElement;
    private Map<String, TTask> taskList;
	private Map<String, TStartEvent> startEventList;
	private Map<String, TEndEvent> endEventList;
	private Map<String, TDataObject> dataObjectList;
	private Map<String, TSequenceFlow> sequenceFlowList;
	private Map<String, TExclusiveGateway> exclusiveGtwList;
	private Map<String, TGateway> gatewayList;
	private Map<String, TSubProcess> subProcessList;

	/**
	 * Constructor de la clase
	 */
	public Bpmn20ModelHandlerImpl() {
		super();
        processes = new HashMap<String, TProcess>();
        processOfElement = new HashMap<String, TProcess>();
        taskList = new HashMap<String, TTask>();
        startEventList = new HashMap<String, TStartEvent>();
        endEventList = new HashMap<String, TEndEvent>();
        dataObjectList = new HashMap<String, TDataObject>();
        sequenceFlowList = new HashMap<String, TSequenceFlow>();
        exclusiveGtwList = new HashMap<String, TExclusiveGateway>();
        gatewayList = new HashMap<String, TGateway>();
        subProcessList = new HashMap<String, TSubProcess>();
    }
	
	/** 
	 * Devuelve el mapa de TTask en el proceso. El id es la key en el mapa.
	 */
	public Map<String, TTask> getTaskMap(){
		return taskList;        	
	}
	
	/** 
	 * Devuelve el mapa de StartEvent en el proceso. El id es la key en el mapa.
	 */
	public Map<String, TStartEvent> getStartEventMap(){
		return startEventList;
	}
	
	/**
	 * Devuelve el mapa de EndEvent en el proceso. El id es la key en el mapa.
	 */
	public Map<String, TEndEvent> getEndEventMap(){
		return endEventList;
	}
	
	/**
	 * Devuelve el mapa de DataObject en el proceso. El id es la key en el mapa.
	 */
	public Map<String, TDataObject> getDataObjectMap(){
		return dataObjectList;
	}
	
	/**
	 * Devuelve el mapa de SequenceFlow en el proceso. El id es la key en el mapa.
	 */
	public Map<String, TSequenceFlow> getSequenceFlowMap(){
		return sequenceFlowList;
	}
	
	/**
	 * Devuelve el mapa de Gateway en el proceso. El id es la key en el mapa.
	 */
	public Map<String, TGateway> getGatewayMap(){
		return gatewayList;
	}
	
	/**
	 * Devuelve el mapa de SequenceFlow en el proceso. El id es la key en el mapa.
	 */
	public Map<String, TExclusiveGateway> getExclusiveGatewayMap(){
		return exclusiveGtwList;
	}
	
	/**
	 * Devuelve el mapa de SubProcess en el proceso. El id es la key en el mapa.
	 */
	public Map<String, TSubProcess> getSubProcessMap(){
		return subProcessList;
	}
	
	/**
	 * Returns factory classes that are used while loading XML files.
	 */
	@Override
	protected Class[] iniLoader() {

        return new Class[] {es.us.isa.bpmn.xmlClasses.bpmn20.ObjectFactory.class,
                es.us.isa.bpmn.xmlClasses.bpmndi.ObjectFactory.class};
    }

	/**
	 * No esta implementado
	 * 
	 * @param procId Id del proceso en el xml. Es utilizado para formar el nombre del archivo xml generado
	 */
	@Override
	protected void generateXml(String procId) {
        throw new UnsupportedOperationException();
    }

    public TDefinitions getDefinitions() {
        return  (TDefinitions) this.getElement().getValue();

    }

    @Override
    public TProcess getProcessOfElement(String id) {
        return processOfElement.get(id);
    }

    protected List<TProcess> readProcesses() {
        List<TProcess> processes = new ArrayList<TProcess>();
        TDefinitions tDefinitions = (TDefinitions) this.getElement().getValue();

        for (JAXBElement<?> element : tDefinitions.getRootElement()) {
            Object participant = element.getValue();
            if (participant instanceof TProcess ) {
                processes.add((TProcess) participant);
            }
        }

        return processes;
    }


    /**
	 * Genera las instancias de clases del modelo a partir de instancias de clases Jabx. 
	 */
	@Override
	protected void generateModel() {
        for (TProcess p : readProcesses()) {
            this.generateProcessModelLists(p);
            this.processes.put(p.getId(), p);
        }
    }

    /**
	 * Genera las instancias de clases del modelo, del proceso indicado
	 * 
	 * @param process Clase Jaxb del proceso
	 */
	private void generateProcessModelLists(TProcess process){
		
		List<JAXBElement<? extends TFlowElement>> flowElements= process.getFlowElement();

        // itera por todos los elementos en el proceso, y de acuerdo a su tipo lo situa en el mapa correspondiente
        for (JAXBElement<? extends TFlowElement> flowElement : flowElements) {

            TFlowElement contentElement = flowElement.getValue();

            processOfElement.put(contentElement.getId(), process);

            if (contentElement instanceof TStartEvent) {

                TStartEvent startEvent = (TStartEvent) contentElement;
                startEvent.getName();

                startEventList.put(startEvent.getId(), startEvent);

            } else if (contentElement instanceof TEndEvent) {

                TEndEvent endEvent = (TEndEvent) contentElement;
                endEvent.getName();
                endEventList.put(endEvent.getId(), endEvent);
            } else if (contentElement instanceof TTask) {

                TTask task = (TTask) contentElement;
                task.getName();
                taskList.put(task.getId(), task);
            } else if (contentElement instanceof TSubProcess) {

                TSubProcess subprocess = (TSubProcess) contentElement;
                subprocess.getName();
                subProcessList.put(subprocess.getId(), subprocess);
            } else if (contentElement instanceof TDataObject) {
                TDataObject dataObject = (TDataObject) contentElement;
                dataObject.getName();
                dataObjectList.put(dataObject.getId(), dataObject);

            } else if (contentElement instanceof TSequenceFlow) {
                TSequenceFlow sequenceFlow = (TSequenceFlow) contentElement;
                sequenceFlow.getName();
                sequenceFlowList.put(sequenceFlow.getId(), sequenceFlow);
            } else if (contentElement instanceof TExclusiveGateway) {

                TExclusiveGateway exclusivegtw = (TExclusiveGateway) contentElement;
                exclusivegtw.getName();
                exclusiveGtwList.put(exclusivegtw.getId(), exclusivegtw);
            } else if (contentElement instanceof TGateway) {

                TGateway gtw = (TGateway) contentElement;
                gtw.getName();
                gatewayList.put(gtw.getId(), gtw);
            }
        }
	}

	/**
	 * Devuelve el objeto Jaxb del proceso
	 * 
	 * @return Objeto TProcess
	 */
    @Override
	public TProcess getProcess() {
        TProcess p = null;
        if (!processes.isEmpty()) {
            p = processes.values().iterator().next();
        }
        return p;
	}

	/**
	 * Devuelve el id del proceso involucrado en el xml
	 * 
	 * @return Id del proceso
	 */
	@Override
	public String getProcId() {
		return this.getProcess().getId();
	}

    @Override
    public Collection<TProcess> getProcesses() {
        return processes.values();
    }

    protected TProcess getProcess(String id) {
        return processes.get(id);
    }

    @Override
    public Collection<String> getProcessId() {
        return processes.keySet();
    }
}
