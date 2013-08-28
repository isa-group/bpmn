package es.us.isa.bpmn.handler;

import es.us.isa.bpmn.xmlClasses.bpmn20.*;

import java.util.Collection;
import java.util.Map;


/**
 * Interfaz de las clases que permiten exportar e importar a XMLs de BPMN 2.0
 * 
 * @author Edelia
 *
 */
public interface Bpmn20ModelHandler extends ModelHandler {

    public TDefinitions getDefinitions();
	
	/**
	 * Devuelve la instancia de clase Jaxb del proceso
	 * 
	 * @return Objeto TProcess
	 */
    @Deprecated
	public TProcess getProcess();

    public Collection<TProcess> getProcesses();

	/** 
	 * Devuelve el mapa de TTask en el proceso. El id es la key en el mapa.
	 */
	public Map<String, TTask> getTaskMap();
	/** 
	 * Devuelve el mapa de TStartEvent en el proceso. El id es la key en el mapa.
	 */
	public Map<String, TStartEvent> getStartEventMap();
	/** 
	 * Devuelve el mapa de TEndEvent en el proceso. El id es la key en el mapa.
	 */
	public Map<String, TEndEvent> getEndEventMap();
	/** 
	 * Devuelve el mapa de TDataObject en el proceso. El id es la key en el mapa.
	 */
	public Map<String, TDataObject> getDataObjectMap();
	/** 
	 * Devuelve el mapa de TSequenceFlow en el proceso. El id es la key en el mapa.
	 */
	public Map<String, TSequenceFlow> getSequenceFlowMap();
	/** 
	 * Devuelve el mapa de TGateway en el proceso. El id es la key en el mapa.
	 */
	public Map<String, TGateway> getGatewayMap();
	/** 
	 * Devuelve el mapa de TExclusiveGateway en el proceso. El id es la key en el mapa.
	 */
	public Map<String, TExclusiveGateway> getExclusiveGatewayMap();
	/** 
	 * Devuelve el mapa de TSubProcess en el proceso. El id es la key en el mapa.
	 */
	public Map<String, TSubProcess> getSubProcessMap();

    TProcess getProcessOfElement(String id);
}
