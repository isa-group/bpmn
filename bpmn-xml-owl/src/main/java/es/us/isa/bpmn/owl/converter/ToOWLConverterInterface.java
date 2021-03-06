package es.us.isa.bpmn.owl.converter;

import es.us.isa.bpmn.handler.ModelHandler;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

/**
 * Interfaz de las clases que convierten a owl, a partir de los objetos del modelo en un ModelHandler
 * 
 * @author Edelia
 *
 */
public interface ToOWLConverterInterface {

	/**
	 * Genera una ontologia OWL a partir de los objetos del modelo en un ModelHandler
	 * 
	 * @param modelHandler Manejador de los objetos del modelo
	 * @return Ontologia OWL
	 * @throws OWLOntologyCreationException
	 */
    public OWLOntology convertToOwlOntology(ModelHandler modelHandler) throws OWLOntologyCreationException;
	/**
     * Devuelve el atributo ontologyURI:
     * URI de la ontologia creada
     * 
     * @return Valor del atributo
     */
    public String getOntologyURI();
	/**
	 * Salva la ontologia generada
	 * 
	 * @param caminoDestino Camino
	 * @param bpmnFilename Nombre del archivo
	 */
    public void saveOntology(String caminoDestino, String bpmnFilename);
}
