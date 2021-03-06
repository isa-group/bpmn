package es.us.isa.bpmn.owl.converter;

import es.us.isa.bpmn.handler.ModelHandler;
import org.semanticweb.owlapi.model.*;

import java.io.File;
import java.io.IOException;

/**
 * Clase abstracta de la que heredan las clases que convierten a owl, a partir de los objetos del modelo en un ModelHandler
 * 
 * @author Edelia
 *
 */
public abstract class ToOWLConverter {

	private OWLOntologyManager manager;	// OWLOntologyManager utilizado
	private String baseIRI;				// IRI de la ontologia creada

	private OWLOntology ontology;		// Ontologia creada

	private String ontologyURI;			// URI de la ontologia creada
	
	/**
	 * Constructor de la clase
	 * 
	 * @param baseIRI IRI de la ontologia creada
	 * @param manager OWLOntologyManager utilizado
	 */
	public ToOWLConverter(String baseIRI, OWLOntologyManager manager){
		
		this.setBaseIRI(baseIRI);
		this.setManager(manager);
	}

	/**
	 * Genera una ontologia OWL a partir de los objetos del modelo en un ModelHandler
	 * 
	 * @param modelHandler Manejador de los objetos del modelo
	 * @return Ontologia OWL
	 * @throws OWLOntologyCreationException
	 */
	public OWLOntology convertToOwlOntology(ModelHandler modelHandler) throws OWLOntologyCreationException {

    	setOntologyURI(getBaseIRI() + modelHandler.getProcId() + ".owl");
    	
		setOntology(getManager().createOntology(IRI.create(getOntologyURI())));

		this.generateOntology(modelHandler);
		
		return this.getOntology();
	}
	
	/**
	 * Adiciona las declaraciones imports a la ontologia generada. De esta manera se especifican otros ontologias que se importan.
	 * 
	 * @param uris Arreglo con las URIs de las ontologias importadas
	 */
	protected void addOntologyImports(String[] uris) {
		
		for (int i=0; i<uris.length; i++)
			getManager().applyChange(new AddImport(getOntology(), getManager().getOWLDataFactory().getOWLImportsDeclaration( IRI.create( uris[i] ) )));
	}
	
	/**
	 * Ejecuta las operaciones propias de cada subclase para generar la ontologia a partir de un ModelHandler
	 * 
	 * @param modelHandler Objeto ModelHandler a partir del cual se genera la ontologia
	 * @throws OWLOntologyCreationException
	 */
	protected abstract void generateOntology(ModelHandler modelHandler) throws OWLOntologyCreationException;
	
	/**
	 * Salva la ontologia generada
	 * 
	 * @param caminoDestino Camino
	 * @param bpmnFilename Nombre del archivo
	 */
	public void saveOntology(String caminoDestino, String bpmnFilename) {
		
		try {
			File bpmnFile = new File(caminoDestino + bpmnFilename);
			bpmnFile.createNewFile();
			
			getManager().saveOntology(getOntology(), IRI.create(bpmnFile.toURI()));
		} catch (OWLOntologyStorageException e) {

			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}

	/**
     * Devuelve el atributo manager:
     * OWLOntologyManager utilizado
     * 
     * @return Valor del atributo
     */
	public OWLOntologyManager getManager() {
		return manager;
	}

    /**
     * Da valor al atributo manager:
     * OWLOntologyManager utilizado
     * 
     * @param manager Valor del atributo
     */
	public void setManager(OWLOntologyManager manager) {
		this.manager = manager;
	}

	/**
     * Devuelve el atributo baseIRI:
     * IRI de la ontologia creada
     * 
     * @return Valor del atributo
     */
	public String getBaseIRI() {
		return baseIRI;
	}

    /**
     * Da valor al atributo baseIRI:
     * IRI de la ontologia creada
     * 
     * @param baseIRI Valor del atributo
     */
	public void setBaseIRI(String baseIRI) {
		this.baseIRI = baseIRI;
	}

	/**
     * Devuelve el atributo ontology:
     * Ontologia creada
     * 
     * @return Valor del atributo
     */
	public OWLOntology getOntology() {
		return ontology;
	}

    /**
     * Da valor al atributo ontology:
     * Ontologia creada
     * 
     * @param ontology Valor del atributo
     */
	public void setOntology(OWLOntology ontology) {
		this.ontology = ontology;
	}

	/**
     * Devuelve el atributo ontologyURI:
     * URI de la ontologia creada
     * 
     * @return Valor del atributo
     */
	public String getOntologyURI() {
		return ontologyURI;
	}

    /**
     * Da valor al atributo ontologyURI:
     * URI de la ontologia creada
     * 
     * @param ontologyURI Valor del atributo
     */
	public void setOntologyURI(String ontologyURI) {
		this.ontologyURI = ontologyURI;
	}

}
