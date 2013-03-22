package es.us.isa.bpms.projects.simple;

import es.us.isa.bpms.dataobject.DataobjectSpace;
import es.us.isa.bpms.dataobject.annotations.*;
import es.us.isa.bpms.projects.simple.dataobject.MensajeObject;
import es.us.isa.bpms.projects.simple.dataobject.SolicitudObject;

/**
 * En este ejemplo se ilustra la forma de utilizar el paquete <a href="../../javadocs/es/us/isa/bpms/package-summary.html">es.us.isa.bpms</a>. 
 * <p>
 * En el proceso de ejemplo se realiza lo siguiente:
 * <ol>
 * <li>En el evento de inicio se muestra un formulario mediante el cual se obtiene el nombre de usuario al que se le asigna
 * la tarea "Enviar solicitud".
 * <li>En la tarea "Enviar Solicitud" se le muestra un formulario al usuario indicado, para que introduzca edad, fecha y 
 * descripcion de la solicitud.
 * <li>Se valida que la edad de la solicitud sea mayor que 24, en caso que no lo sea se retorna a "Enviar Solicitud" y se 
 * muestra el mensaje de error. Si no hay error se pasa a la tarea "Cambiar descripcion a mayusculas".
 * <li>En la tarea "Cambiar descripcion a mayusculas" se toma la descripcion de la solicitud y se cambia a mayusculas.
 * <li>La tarea "Aprobar solicitud" esta asignada al grupo management. En esta se muestra un formulario mediante el cual
 * se puede aprobar o no la solicitud, y opcionalmente indicar un motivo.
 * <li>Si la solicitud es aprobada se termina el proceso, si no es aprobada se pasa a la tarea "Informar no aprobado".
 * <li>La tarea "Informar no aprobado" esta asignada al mismo usuario indicado en el evento de inicio. Muestra un formulario
 * mediante el cual se informa que la solicitud no fue aprobada y los motivos, ademas se permite modificar la edad, la 
 * fecha y la descripcion de la solicitud, e indicar si se desea reenviar la solicitud o no.
 * <li>Si la solicitud no es reenviada se termina el proceso, en caso contrario se va al paso 3.
 * </ol>
 * En este proceso de ejemplo se estructuran los datos en dos objetos, uno con la informacion de la solicitud 
 * que esta siendo procesada y otro con el mensaje de error que se puede generar al validar la solicitud. Este ultimo
 * no es imprescindible pero se incluye para ilustrar. 
 * <p>
 * Tiene las anotaciones de clase:
 * <p>
 * &#064<a href="../../javadocs/es/us/isa/bpms/dataobject/annotations/DataobjectClasses.html">DataobjectClasses</a>({<br>
 * 	&nbsp&nbsp&#064DataobjectClass(id = "SolicitudObject", classref = "es.us.isa.bpms.projects.simple.dataobject.SolicitudObject"),<br>
 * 	&nbsp&nbsp&#064DataobjectClass(id = "MensajeObject", classref = "es.us.isa.bpms.projects.simple.dataobject.MensajeObject")<br>
 * })<br>
 * &#064<a href="../../javadocs/es/us/isa/bpms/dataobject/annotations/Dataobjects.html">Dataobjects</a>({<br>
 * 	&nbsp&nbsp&#064Dataobject(id = "solicitud", dataobjectclassid = "SolicitudObject"),<br>
 * 	&nbsp&nbsp&#064Dataobject(id = "mensaje", dataobjectclassid = "MensajeObject")<br>
 * })<br>
 * &#064<a href="../../javadocs/es/us/isa/bpms/dataobject/annotations/ObjectUpdates.html">ObjectUpdates</a>({<br>
 * 	&nbsp&nbsp&#064ObjectUpdate(activityid = "startevent1", event = "complete", updatetype = "dataobject", id = "solicitud", updates = { "asignar" }),<br>
 * 	&nbsp&nbsp&#064ObjectUpdate(activityid = "usertask2", event = "complete", updatetype = "class", id = "SolicitudObject", updates = { "enviar" }),<br>
 * 	&nbsp&nbsp&#064ObjectUpdate(activityid = "usertask3", event = "complete", updatetype = "collection", id = "solicitudCollection", updates = { "aprobar" }),<br>
 * 	&nbsp&nbsp&#064ObjectUpdate(activityid = "usertask4", event = "complete", updatetype = "dataobject", id = "solicitud", updates = { "enviar" })<br>
 * })<br>
 * &#064<a href="../../javadocs/es/us/isa/bpms/dataobject/annotations/VariableUpdates.html">VariableUpdates</a>({<br>
 * 	&nbsp&nbsp&#064VariableUpdate(activityid = "usertask2", event = "complete", updatetype = "dataobject", id = "mensaje"),<br>
 * 	&nbsp&nbsp&#064VariableUpdate(activityid = "usertask4", event = "complete", updatetype = "class", id = "MensajeObject"),<br>
 * 	&nbsp&nbsp&#064VariableUpdate(activityid = "servicetask1", event = "complete", updatetype = "collection", id = "solicitudCollection")<br>
 * })<br>
 * 
 * @author Edelia Garcia Gonzalez
 * @version 1.0
 */
@SuppressWarnings("serial")
@DataobjectClasses({
	@DataobjectClass(id = "SolicitudObject", classref = "es.us.isa.bpms.projects.simple.dataobject.SolicitudObject"),
	@DataobjectClass(id = "MensajeObject", classref = "es.us.isa.bpms.projects.simple.dataobject.MensajeObject")
})
@Dataobjects({
	@DataobjectObject(id = "solicitud", dataobjectclassid = "SolicitudObject"),
	@DataobjectObject(id = "mensaje", dataobjectclassid = "MensajeObject")
})
@ObjectUpdates({
	@ObjectUpdate(activityid = "startevent1", event = "complete", updatetype = "dataobject", id = "solicitud", updates = { "asignar" }),
	@ObjectUpdate(activityid = "usertask2", event = "complete", updatetype = "class", id = "SolicitudObject", updates = { "enviar" }),
	@ObjectUpdate(activityid = "usertask3", event = "complete", updatetype = "collection", id = "solicitudCollection", updates = { "aprobar" }),
	@ObjectUpdate(activityid = "usertask4", event = "complete", updatetype = "dataobject", id = "solicitud", updates = { "enviar" })
})
@VariableUpdates({
	@VariableUpdate(activityid = "usertask2", event = "complete", updatetype = "dataobject", id = "mensaje"),
	@VariableUpdate(activityid = "usertask4", event = "complete", updatetype = "class", id = "MensajeObject"),
	@VariableUpdate(activityid = "servicetask1", event = "complete", updatetype = "collection", id = "solicitudCollection")
})
public class ProcessDataobjectSpace extends DataobjectSpace {
	
    /**
     * Constructor de la clase.
     */
	public ProcessDataobjectSpace() {
		
		super();
	}

     /**
     * Implementacion para el listener que se invoca cuando se completan las tareas "Enviar Solicitud" e "Informar no aprobado".
     * Se asocia a estas actividades en tiempo de ejecucion, durante el parse del proceso.
     * <p>
     * Tiene la anotacion:
     * <p>
     * &#064<a href="../../javadocs/es/us/isa/bpms/dataobject/annotations/Listeners.html">Listeners</a>({<br>
     * 	&nbsp&nbsp&#064Listener(activityid = "usertask2", event = "complete"),<br>
     * 	&nbsp&nbsp&#064Listener(activityid = "usertask4", event = "complete")<br>
     * })<br>
     * 
     * @param object Objeto para interactuar con la plataforma BPM
     */
	@Listeners({
		@Listener(activityid = "usertask2", event = "complete"),
		@Listener(activityid = "usertask4", event = "complete")
	})
    public void lstValidarSolicitud(Object object) {
		// para ilustrar se incluyen dos formas de obtener el resultado de la validacion de la solicitud 
		
		// valida la solicitud y, en caso que exista, conserva el mensaje de error en el dataobject Mensaje
		// en el diagrama del proceso es utilizado el dataobject Mensaje en el formulario de la tarea "Enviar Solicitud"
		// y en el conector que regresa a esta tarea si la validacion no fue correcta
		((MensajeObject) this.getDataobject(object, "mensaje")).setTexto( 
				((SolicitudObject) this.getDataobject(object, "solicitud")).validate() );
		
		// valida la solicitud y, en caso que exista, conserva el mensaje de error la variable error
		// en el diagrama del proceso se utiliza esta variable en el conector que continua a la tarea "Cambiar descripcion 
		// a mayusculas" si la validacion fue correcta
		this.setVariable(object, "error", ((SolicitudObject) this.getDataobject(object, "solicitud")).validate());
   }
    
    /**
     * Implementacion para la clase que se invoca en la tarea "Cambiar descripcion a mayusculas".
     * <p>
     * Tiene la anotacion:
     * <p>
     * &#064<a href="../../javadocs/es/us/isa/bpms/dataobject/annotations/ServiceTasks.html">ServiceTasks</a>({<br>
     * 	&nbsp&nbsp&#064ServiceTask("servicetask1")<br>
     * })<br>
     * 
     * @param object Objeto para interactuar con la plataforma BPM
     */
	@ServiceTasks({
		@ServiceTask("servicetask1")
	})
    public void accChangeToUppercase(Object object) {

		// cambia la descripcion de la solicitud a mayusculas
		SolicitudObject solicitud = (SolicitudObject) this.getDataobject(object, "solicitud" );
	    solicitud.setDescripcion( solicitud.getDescripcion().toUpperCase() ); 

	    // para ilustrar se adicionan los dataobjects a colecciones
	    // estas colecciones son utilizadas posteriormente para actualizar los dataobjects; lo cual se especifica en las
	    // anotaciones de la clase
	    this.addToCollection(object, "solicitud", "solicitudCollection");
	    this.addToCollection(object, "mensaje", "mensajeCollection");
  	
    }
}
