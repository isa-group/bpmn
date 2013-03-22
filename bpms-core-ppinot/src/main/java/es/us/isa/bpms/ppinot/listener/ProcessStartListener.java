package es.us.isa.bpms.ppinot.listener;

import java.util.Date;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

import es.us.isa.bpms.ppinot.db.ProcessDAO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Listener para procesar eventos de inicio de la ejecucion de una instancia de proceso.
 * <p>
 * Este listener se asocia con una declaracion EPL que captura todos los eventos de la clase 
 * <a href="../event/ProcessStartEvent.html">ProcessStartEvent</a>.
 * 
 * @author Edelia Garcia Gonzalez
 * @version 1.0
 * @see es.us.isa.bpms.ppinot.event.ProcessStartEvent
 * @see es.us.isa.bpms.db
 *
 */
public class ProcessStartListener implements UpdateListener {

    private static final Log log = LogFactory.getLog(ProcessStartListener.class);

    /**
     * Conserva en la BD que se produjo un evento de inicio de proceso
     */
    public void update(EventBean[] newEvents, EventBean[] oldEvents) {
    	
    	for (int i=0; i<newEvents.length; i++) {
    		
	        EventBean event = newEvents[i];
	        log.info(
	        		"====>> Catching ProcessStartEvent: " 
	        		+ "processDefId=" + event.get("processDefId") + ", instanceId=" + event.get("instanceId") + ", time=" + event.get("time") 
	        		+ "processDefId=" + event.get("processDefId") );
	        
	        try {

		        ProcessDAO.saveStart((String) event.get("processDefId"), (String) event.get("instanceId"), (Date) event.get("time"), (String) event.get("processId"));
			} catch (Exception e) {
				
				e.printStackTrace();
			}
    	}
    }
}