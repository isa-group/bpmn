package es.us.isa.bpms.ppinot.listener;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

import es.us.isa.bpms.ppinot.db.ActivityDAO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Listener para procesar eventos de asignacion de usuario a una actividad.
 * <p>
 * Este listener se asocia con una declaracion EPL que captura todos los eventos de la clase 
 * <a href="../event/ActivityUserAssignmentEvent.html">ActivityUserAssignmentEvent</a>.
 * 
 * @author Edelia Garcia Gonzalez
 * @version 1.0
 * @see es.us.isa.bpms.ppinot.event.ActivityStartEvent
 * @see es.us.isa.bpms.db
 *
 */
public class UserTaskAsignedUserListener implements UpdateListener {

    private static final Log log = LogFactory.getLog(UserTaskAsignedUserListener.class);
	
    /**
     * Conserva en la BD que se produjo un evento de inicio de actividad
     */
    public void update(EventBean[] newEvents, EventBean[] oldEvents) {
    	
    	for (int i=0; i<newEvents.length; i++) {
    		
	        EventBean event = newEvents[i];
	        
	        log.info(
	        		"====>> Catching UserTaskAssignedUserEvent: " + 
	        		"processDefId=" + event.get("processDefId") + ", instanceId=" + event.get("instanceId") + 
	        		", username=" + event.get("username") );
	        
	        try {

		        ActivityDAO.saveUsername((String) event.get("processDefId"), (String) event.get("instanceId"), (String) event.get("activityId"), (String) event.get("username"));
			} catch (Exception e) {
				
				e.printStackTrace();
			}
    	}
    }
}
