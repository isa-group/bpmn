package es.us.isa.bpms.projects.simple.dataobject;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import es.us.isa.bpms.dataobject.Dataobject;
import es.us.isa.bpms.dataobject.annotations.BPMVariables;
import es.us.isa.bpms.dataobject.annotations.SVPoint;
import es.us.isa.bpms.dataobject.annotations.StateViewPoints;


/**
 * Dataobject con la informacion de la solicitud que se esta procesando.
 * 
 * @author Edelia Garcia Gonzalez
 * @version 1.0
 */
@SuppressWarnings("serial")
@StateViewPoints({
	@SVPoint(id = "aprobacion", fromBeginning = "true", states = {"aprobado", "noAprobado"})
})
public class SolicitudObject extends Dataobject {

	/**
	 * El nombre de usuario del solicitante
	 * @serial
	 */
	private String nombre;
	
	/**
	 * La edad del solicitante
	 * @serial
	 */
	private Byte edad;
	
	/**
	 * La fecha de solicitud
	 * @serial
	 */
	private Date fecha;
	
	/**
	 * La descripcion
	 * @serial
	 */
	private String descripcion;
	
	/**
	 * Si la solicitud fue aprobada
	 * @serial
	 */
	private Boolean aprobacion;
	
	/**
	 * El motivo de la aprobacion o no aprobacion
	 * @serial
	 */
	private String motivo;

	/**
	 * Constructor de la clase 
	 * 
	 */
	public SolicitudObject() {

		super();
	}
	
	/**
	 * Inicializa el dataobject
	 * 
	 */
	@Override
	protected void init() {
		
		this.setNombre("");
		this.setEdad((byte) 0);
		this.setFecha(new Date());
		this.setDescripcion("");
		this.setAprobacion(false);
		this.setMotivo("");
	}
	
	/**
	 * Valida si los atributos de la solicitud cumplen con las condiciones requeridas
	 * 
	 * @return El mensaje de error en caso que se detecte alguno, en caso contrario se devuelve la cadena vacia
	 */
	@Override
	public String validate() {
		String error = "";
		if ( (Byte) this.getEdad()<24 )
			error = "La edad tiene que ser mayor o igual que 24";
		return error;
	}
	
	/**
	 * Actualiza propiedades a partir de variables de la plataforma: 
	 * nombre
	 * 
	 */
	@BPMVariables ({"nombre"})
	public void updateInAsignar(Map<String, Object> variables) {
		
		this.setNombre( (String) variables.get("nombre") );
	}
	
	
	/**
	 * Devuelve el valor de variables de la plataforma BPM a partir de propiedades: 
	 * nombre
	 * 
	 * @return Mapa con el nombres de las variables y su valor
	 */
	public Map<String, Object> updateOutAsignar() {
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("nombre", (String) this.getNombre());
		
		return map;
	}
	
	/**
	 * Actualiza propiedades a partir de variables de la plataforma: 
	 * descripcion, fecha y edad
	 * 
	 */
	@BPMVariables ({"descripcion", "fecha", "edad"})
	public void updateInEnviar(Map<String, Object> variables) {

		this.setDescripcion((String) variables.get("descripcion") );
		this.setFecha((Date) variables.get("fecha") );
		this.setEdad(((Long) variables.get("edad")).byteValue() );

	}
	
	/**
	 * Devuelve el valor de variables de la plataforma BPM a partir de propiedades: 
	 * descripcion, fecha y edad
	 * 
	 * @return Mapa con el nombres de las variables y su valor
	 */
	public Map<String, Object> updateOutEnviar() {
		
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("descripcion", this.getDescripcion());
		map.put("fecha", this.getFecha());
		map.put("edad", ((Byte) this.getEdad()).longValue());
		
		return map;
	}

	/**
	 * Actualiza propiedades a partir de variables de la plataforma: 
	 * motivo y aprobacion
	 * 
	 */
	@BPMVariables ({"motivo", "aprobacion"})
	public void updateInAprobar(Map<String, Object> variables) {

		this.setMotivo((String) variables.get("motivo") );
		this.setAprobacion(((String) variables.get("aprobacion")).contentEquals("true") );
	}
	
	/**
	 * Devuelve el valor de variables de la plataforma BPM a partir de propiedades: 
	 * motivo y aprobacion
	 * 
	 * @return Mapa con el nombres de las variables y su valor
	 */
	public Map<String, Object> updateOutAprobar() {
		
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("motivo", this.getMotivo());
		map.put("aprobacion", this.getAprobacion()?"true":"false");

		return map;
	}
	
	/**
	 * Evalua si el dataobject se encuentra en estado aprobado
	 * 
	 * @return Indica si el dataobject esta en el estado indicado o no
	 */
	public Boolean isAprobado() {
		
		return (Boolean) this.getAprobacion();
	}
	
	/**
	 * Evalua si el dataobject se encuentra en estado noAprobado
	 * 
	 * @return Indica si el dataobject esta en el estado indicado o no
	 */
	public Boolean isNoAprobado() {
		
		return !(Boolean) this.getAprobacion();
	}
	
	/**
	 * Devuelve el valor del atributo nombre: 
	 * Nombre de usuario del solicitante.
	 * 
	 * @return Valor del atributo
	 */
	public String getNombre() {
		return this.nombre;
	}
	
	/**
	 * Da valor al atributo nombre: 
	 * Nombre de usuario del solicitante.
	 * 
	 * @param newValue Valor del atributo
	 */
	public void setNombre(String newValue) {
		
		this.recordChange("nombre", this.getNombre(), newValue);
		this.nombre = newValue;
	}
	
	/**
	 * Devuelve el valor del atributo edad: 
	 * Edad del solicitante.
	 * 
	 * @return Valor del atributo
	 */
	public Byte getEdad() {
		return this.edad;
	}
	
	/**
	 * Da valor al atributo edad: 
	 * Edad del solicitante.
	 * 
	 * @param newValue Valor del atributo: 
	 */
	public void setEdad(Byte newValue) {
		
		this.recordChange("edad", this.getEdad(), newValue);
		this.edad = newValue;
	}
	
	/**
	 * Devuelve el valor del atributo fecha: 
	 * Fecha indicada en la solicitud.
	 * 
	 * @return Valor del atributo
	 */
	public Date getFecha() {
		return this.fecha;
	}
	
	/**
	 * Da valor al atributo fecha: 
	 * Fecha indicada en la solicitud.
	 * 
	 * @param newValue Valor del atributo
	 */
	public void setFecha(Date newValue) {
		
		this.recordChange("fecha", this.getFecha(), newValue);
		this.fecha = newValue;
	}
	
	/**
	 * Devuelve el valor del atributo descripcion: 
	 * Descripcion de la solicitud.
	 * 
	 * @return Valor del atributo
	 */
	public String getDescripcion() {
		return this.descripcion;
	}
	
	/**
	 * Da valor al atributo descripcion: 
	 * Descripcion de la solicitud.
	 * 
	 * @param newValue Valor del atributo
	 */
	public void setDescripcion(String newValue) {
		
		this.recordChange("descripcion", this.getDescripcion(), newValue);
		this.descripcion = newValue;
	}
	
	/**
	 * Devuelve el valor del atributo aprobacion: 
	 * Aprobacion del jefe.
	 * 
	 * @return Valor del atributo
	 */
	public Boolean getAprobacion() {
		return this.aprobacion;
	}
	
	/**
	 * Da valor al atributo nombre: 
	 * Aprobacion del jefe.
	 * 
	 * @param newValue Valor del atributo
	 */
	public void setAprobacion(Boolean newValue) {
		
		this.recordChange("aprobacion", this.getAprobacion(), newValue);
		this.aprobacion = newValue;
	}
	
	/**
	 * Devuelve el valor del atributo motivo: 
	 * Motivo de la aprobacion o no del jefe.
	 * 
	 * @return Valor del atributo
	 */
	public String getMotivo() {
		return this.motivo;
	}
	
	/**
	 * Da valor al atributo nombre: 
	 * Motivo de la aprobacion o no del jefe.
	 * 
	 * @param newValue Valor del atributo
	 */
	public void setMotivo(String newValue) {
		
		this.recordChange("motivo", this.getMotivo(), newValue);
		this.motivo = newValue;
	}
}