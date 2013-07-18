package es.us.isa.bpms.ppinot;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import com.espertech.esper.client.EPServiceProvider;

import es.us.isa.bpms.ppinot.event.Event;

/**
 * Clase del hilo asociado a una instancia de proceso en ejecucion, para recibir eventos de esta que son enviados a Esper
 * 
 * @author Edelia Garcia Gonzalez
 * @version 1.0
 *
 */
public final class SendRunnable implements Runnable {

    // log de la aplicacion
	private static final Log log = LogFactory.getLog(SendRunnable.class);
	// proveedor de servicios de Esper en la aplicacion
	private final EPServiceProvider esperServiceProvider;
    // indica que el hilo tiene que ser cerrado. Lo sera cuando envie todos los eventos que tiene en cola
	private volatile boolean toBeShutdown;
    // indica que el hilo ya fue cerrado
	private volatile boolean isShutdown;
	// cola de eventos que seran enviados a Esper
	private List<Event> eventList;

	/**
	 * Constructor de la clase
	 */
	SendRunnable(EPServiceProvider esperServiceProvider) {
		
        this.esperServiceProvider = esperServiceProvider;
        
        this.toBeShutdown = false;
        this.isShutdown = false;
        this.eventList = new ArrayList<Event>();
    }
	
	/**
	 * Envia a Esper los eventos en cola
	 */
	@Override
	public void run() {

		log.info(
        		"Call thread " + Thread.currentThread() + " starting");

        try
        {
        	// el hilo continua enviando todos los eventos en cola antes de detenerse
            while(!this.toBeShutdown || this.eventList.size()!=0)
            {
            	
            	// mientras se obtenga algun evento de la cola
            	Event event = this.popEvent();
            	if (event!=null) {
                    // envia el evento a Esper
            		this.esperServiceProvider.getEPRuntime().sendEvent( event );
            	}
            }
            this.isShutdown = true;
        }
        catch (Exception ex) {
        	
        	log.info(
            		"Error in send loop" + ex.getMessage());
        } 

        log.info(
        		"Call thread " + Thread.currentThread() + " done");
		
	}

	/**
	 * Para indicar al hilo que se detenga
	 */
	void setShutdown()
    {
        this.toBeShutdown = true;
    }
	
	/**
	 * Devuelve el atributo toBeShutdown:
	 * indica que sera cerrado y no debe recibir mas eventos
	 * 
	 * @return Valor del atributo
	 */
	boolean isToBeShutdown() {
		return this.toBeShutdown;
	}
	
	/**
	 * Devuelve el atributo isShutdown:
	 * indica que el hilo ya fue cerrado. Antes de detenerse envia todos los eventos que tenia en cola
	 * 
	 * @return Valor del atributo
	 */
	boolean isShutdown() {
		return this.isShutdown;
	}
	
	/**
	 * Obtiene el siguiente evento a ejecutar en la cola y lo elimina de esta
	 *
	 * @return Evento
	 */
	private Event popEvent() {
		
		if (this.eventList.size()==0)
			
			return null;
		else {
			
			return this.eventList.remove(0);
		}
	}
	
	/**
	 * Adiciona un evento a la cola de los eventos que seran enviados a Esper
	 * 
	 * @param event Evento 
	 */
	void pushEvent(Event event) {

		this.eventList.add(event);
	}
}
