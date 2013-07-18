package es.us.isa.bpms.ppinot.listener;

import java.util.Date;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

import es.us.isa.bpms.ppinot.db.ActivityDAO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Listener para procesar eventos de fin de actividad.
 * <p>
 * Este listener se asocia con una declaracion EPL que captura todos los eventos de la clase 
 * <a href="../event/ActivityEndEvent.html">ActivityEndEvent</a>.
 * 
 * @author Edelia Garcia Gonzalez
 * @version 1.0
 * @see es.us.isa.bpms.ppinot.event.ActivityEndEvent
 * @see es.us.isa.bpms.db
 *
 */
public class ActivityEndListener implements UpdateListener {

    private static final Log log = LogFactory.getLog(ActivityEndListener.class);
	
    /**
     * Conserva en la BD que se produjo un evento de fin de actividad
     */
    public void update(EventBean[] newEvents, EventBean[] oldEvents) {
    	
    	for (int i=0; i<newEvents.length; i++) {
    		
	        EventBean event = newEvents[i];
	        log.info(
	        		"====>> Catching ActivityEndEvent: " + 
	        		"processDefId=" + event.get("processDefId") + ", instanceId=" + event.get("instanceId") + ", executionId=" + event.get("executionId") + ", time=" + event.get("time") + 
	        		", activityId=" + event.get("activityId") );
	        
	        try {

		        ActivityDAO.saveEnd((String) event.get("processDefId"), (String) event.get("instanceId"), (String) event.get("activityId"), (Date) event.get("time"));
			} catch (Exception e) {
				
				e.printStackTrace();
			}
    	}
    }
}