package es.us.isa.bpms.ppinot.listener;

import java.util.Date;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

import es.us.isa.bpms.ppinot.db.ActivityDAO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Listener para procesar eventos de inicio de actividad.
 * <p>
 * Este listener se asocia con una declaracion EPL que captura todos los eventos de la clase 
 * <a href="../event/ActivityStartEvent.html">ActivityStartEvent</a>.
 * 
 * @author Edelia Garcia Gonzalez
 * @version 1.0
 * @see es.us.isa.bpms.ppinot.event.ActivityStartEvent
 * @see es.us.isa.bpms.db
 *
 */
public class ActivityStartListener implements UpdateListener {

    private static final Log log = LogFactory.getLog(ActivityStartListener.class);
	
    /**
     * Conserva en la BD que se produjo un evento de inicio de actividad
     */
    public void update(EventBean[] newEvents, EventBean[] oldEvents) {
    	
    	for (int i=0; i<newEvents.length; i++) {
    		
	        EventBean event = newEvents[i];
	        
	        log.info(
	        		"====>> Catching ActivityStartEvent: " + 
	        		"processDefId=" + event.get("processDefId") + ", instanceId=" + event.get("instanceId") + ", executionId=" + event.get("executionId") + ", time=" + event.get("time") + 
	        		", activityId=" + event.get("activityId") + ", activityName=" + event.get("activityName") + ", activityType=" + event.get("activityType") );
	        
	        try {

		        ActivityDAO.saveStart((String) event.get("processDefId"), (String) event.get("instanceId"), (String) event.get("executionId"), (String) event.get("activityId"), (String) event.get("activityName"), (String) event.get("activityType"), (Date) event.get("time"));
			} catch (Exception e) {
				
				e.printStackTrace();
			}
    	}
    }
}