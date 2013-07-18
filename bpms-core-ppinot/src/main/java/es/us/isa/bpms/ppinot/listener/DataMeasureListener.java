package es.us.isa.bpms.ppinot.listener;

import java.util.Date;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

import es.us.isa.bpms.ppinot.db.DataobjectDAO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Listener para procesar modificaciones en una propiedad de un dataobject.
 * <p>
 * Este listener se asocia con una declaracion EPL que captura los eventos de la clase 
 * <a href="../event/DataobjectEvent.html">DataobjectEvent</a> en los cuales se haya 
 * modificado la propiedad indicada en la declaracion.
 * 
 * @author Edelia Garcia Gonzalez
 * @version 1.0
 * @see es.us.isa.bpms.ppinot.event.DataobjectEvent
 * @see es.us.isa.bpms.db
 *
 */
public class DataMeasureListener implements UpdateListener {

    private static final Log log = LogFactory.getLog(DataMeasureListener.class);
	
    /**
     * Conserva en la BD que la ultima vez que se modifico una propiedad de un dataobject
     */
    public void update(EventBean[] newEvents, EventBean[] oldEvents) {
    	
    	for (int i=0; i<newEvents.length; i++) {
    		
	        EventBean event = newEvents[i];
	        log.info(
	        		"====>> Catching DataMeasureEvent: "  
	        		+ "processDefId=" + event.get("processDefId") 
	        		+ ", instanceId=" + event.get("instanceId")
	        		+ ", executionId=" + event.get("executionId")  
	        		+ ", activityId=" + event.get("activityId")  
	        		+ ", time=" + event.get("time")
	        		+ ", dataobjectId=" + event.get("dataobjectId") 
	        		+ ", property=" + event.get("property") 
	        		+ ", value=" + event.get("value")  
	        		+ ", type=" + event.get("type")
	        		);

	        try {
	        	
				DataobjectDAO.saveChange((String) event.get("processDefId"), (String) event.get("instanceId"), (String) event.get("executionId"), (String) event.get("activityId"), (String) event.get("dataobjectId"), (String) event.get("property"), event.get("value").toString(), (String) event.get("type"), (Date) event.get("time"));
			} catch (Exception e) {

				e.printStackTrace();
			}
    	}
    }
}