package es.us.isa.bpms.ppinot.listener;

import java.util.Date;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

import es.us.isa.bpms.ppinot.db.DataConditionDAO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Listener para procesar modificaciones en un estado de un dataobject.
 * <p>
 * Este listener se asocia con dos declaraciones EPL complementarias; una que captura los eventos de la clase 
 * <a href="../event/DataobjectEvent.html">DataobjectEvent</a> en los cuales el dataobject haya pasado al estado indicado 
 * en la declaracion y la otra que captura los eventos de la clase DataobjectEvent en los cuales el dataobject haya 
 * abandonado al estado indicado en la declaracion.
 * 
 * @author Edelia Garcia Gonzalez
 * @version 1.0
 * @see es.us.isa.bpms.ppinot.event.DataobjectEvent
 * @see es.us.isa.bpms.db
 *
 */
public class DataConditionMeasureListener implements UpdateListener {

    private static final Log log = LogFactory.getLog(DataConditionMeasureListener.class);
	
    /**
     * Conserva en la BD que la ultima vez que se modifico un estado de un dataobject
     */
    public void update(EventBean[] newEvents, EventBean[] oldEvents) {
    	
    	for (int i=0; i<newEvents.length; i++) {
    		
	        EventBean event = newEvents[i];
	        log.info(
	        		"====>> Catching DataConditionMeasureEvent: " 
	        		+ "processDefId=" + event.get("processDefId") 
	        		+ ", instanceId=" + event.get("instanceId") 
	        		+ ", executionId=" + event.get("executionId") 
	        		+ ", activityId=" + event.get("activityId")  
	        		+ ", time=" + event.get("time") 
	        		+ ", dataobjectId=" + event.get("dataobjectId") 
	        		+ ", state=" + event.get("state") 
	        		+ ", truth=" + event.get("truth"));

	        try {
	        	
				DataConditionDAO.saveChange((String) event.get("processDefId"), (String) event.get("instanceId"), (String) event.get("executionId"), (String) event.get("activityId"), (String) event.get("dataobjectId"), (String) event.get("state"), (Boolean) event.get("truth"), (Date) event.get("time"));
			} catch (Exception e) {

				e.printStackTrace();
			}
    	}
    }
}