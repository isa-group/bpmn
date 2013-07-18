package es.us.isa.bpms.ppinot;

import java.util.concurrent.ThreadFactory;

/**
 * Factory de los hilos asociados en las instancias de procesos
 * 
 * @author Edelia Garcia Gonzalez
 * @version 1.0
 *
 */
class SimpleThreadFactory implements ThreadFactory {
	
	   public Thread newThread(Runnable r) {
		   
	     return new Thread(r);
	   }
}
