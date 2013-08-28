package es.us.isa.bpmn.handler;


import com.sun.xml.bind.IDResolver;
import org.xml.sax.SAXException;

import javax.xml.bind.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Clase abstracta a partir de la cual heredan las clases que permiten exportar e importar a XML. 
 * 
 * Cuando se importa de xml se obtiene la informacion que contiene en instancias de clases generadas con Jaxb (clases Jaxb) y a partir de 
 * esas instancias se puede obtener instancias de clases que modelen esa informacion (clases del modelo), es decir:
 * 
 * codigo xml -> instancias de clases Jabx -> clases del modelo
 * 
 * Esto permite que si cambia la sintaxis del xml (con lo cual cambian las clases Jaxb), los proyectos que utilicen las clases del modelo 
 * no sufren cambios. 
 * 
 * Esta clase tiene las siguientes funciones:
 * - Carga un xml y obtiene las clases del modelo.
 * - A partir de las clases del modelo genera un xml.
 *
 * @author resinas
 * @author Edelia
 *
 */
@SuppressWarnings("rawtypes")
public abstract class AbstractModelHandler implements ModelHandler {
	
	// Objeto JAXBElement utilizado para manejar el xml
	private JAXBContext jc;
	
	// Objeto JAXBElement que se obtiene del unmarshall de un xml (a partir de un archivo xml obtener clases Jaxb)
	private JAXBElement element;

    private Map<String, Object> elementIds;

	/**
	 * Constructor de la clase.
	 * Realiza las inicializaciones implementadas en las subclases mediante iniLoader.
	 * 
	 * @throws JAXBException
	 */
	public AbstractModelHandler() {
		super();
        elementIds = new HashMap<String, Object>();
        xmlConfig(this.iniLoader());
	}

    public AbstractModelHandler(JAXBElement element) {
        this();
        this.element = element;
    }

    /**
	 * Configuracion para importar y exportar XML.
	 * Este metodo debe ser invocado en el iniLoader de la subclase, para especificar las clases Jaxb y la clase factory utilizadas. 
	 * 
	 * @param classList Arreglo con las clases para leer y guardar como xml 
	 */
	private void xmlConfig(Class[] classList) {
		
		try {
			// crea el JAXBContext para manejar los XML
			this.jc = JAXBContext.newInstance( classList );
		} catch (JAXBException e) {
            throw new RuntimeException("Unable to load xml context: " + e);
        }
    }

	/**
	 * Devuelve el valor del atributo element
	 * Objeto JAXBElement que se obtiene del unmarshall de un xml
	 * 
	 * @return Valor del atributo
	 */
	protected JAXBElement getElement() {
		return this.element;
	}

    protected Map<String, Object> getElementIds() {
        return elementIds;
    }

    @Override
    public Object getElement(String id) {
        return elementIds.get(id);
    }

	/**
	 * Realiza las inicializaciones propias de la clase que implementa a ModelHandler. 
	 * En esta inicializacion hay que incluir la llamada a xmlConfig para configurar las clases Jaxb y la clase factory.
	 * 
	 * @throws JAXBException
	 */
	protected abstract Class[] iniLoader();
	
	/**
	 * Genera las instancias de clases Jaxb a partir de instancias de clases del modelo. 
	 * Tiene que ser implementado en las subclases.
	 * Genera el JAXBElement para exportar, por lo que debe finalizar invocando a this.setExportElement
	 * 
	 * @param procId Id del proceso en el xml. Es utilizado para formar el nombre del archivo xml generado
	 */
	protected abstract void generateXml(String procId);
	
	/**
	 * Genera las instancias de clases del modelo a partir de instancias de clases Jabx. 
	 * Tiene que ser implementado en las subclases.
	 * Utiliza el elemento JAXBElement para importar, invocando a this.getExportElement
	 */
	protected abstract void generateModel();
	
	/**
	 * Genera las instancias de clases del modelo a partir del xml en un archivo
	 * 
	 * @param path Camino del archivo
	 * @param file Nombre del archivo
	 * @throws JAXBException
	 */
	public void load(String path, String file) throws JAXBException {
        try {
            load(new FileInputStream(new File(path, file)));
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found: " + path + " - " + file, e);
        }
    }

    /**
	 * Genera las instancias de clases del modelo a partir del xml en un stream
     * 
     * @param stream 
     * @throws JAXBException
     */
	public void load(InputStream stream) throws JAXBException {
    	
		// hace el unmarshall a las clases generadas por Jaxb
        Unmarshaller unmarshaller = jc.createUnmarshaller();

        unmarshaller.setProperty(com.sun.xml.bind.IDResolver.class.getName(), new MyIDResolver());

        this.element =  (JAXBElement<?>) unmarshaller.unmarshal(stream);

        // obtiene los objetos del modelo, a partir de los objetos obtenidos del unmarshall
        this.generateModel();
    }

	/**
	 * Salva el xml a un archivo, a partir de las instancias de clases del modelo
	 * 
	 * @param path Camino
	 * @param file Nombre del fichero
	 * @param procId Id del proceso
	 * @throws JAXBException
	 */
	public void save(String path, String file, String procId) throws JAXBException {
        try {
            save(new FileOutputStream(new File(path, file)), procId);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found: " + path + " - " + file, e);
        }
    }

    /**
	 * Salva el xml a un stream, a partir de las instancias de clases del modelo
	 * 
	 * @param stream
	 * @param procId Id del proceso
	 * @throws JAXBException
	 */
	public void save(OutputStream stream, String procId) throws JAXBException {

		// genera un objeto del tipo JAXBElement con los objetos que pueden ser exportados a un fichero xml
    	this.generateXml(procId);
    	
    	// exporta el xml a partir del JAXBElement generado
        this.jc.createMarshaller().marshal( element, stream );
	}

    public class MyIDResolver extends IDResolver {

        @Override
        public void startDocument(ValidationEventHandler eventHandler) throws SAXException {
            if(elementIds != null)
                elementIds.clear();
        }

        @Override
        public void bind(String s, Object o) throws SAXException {
            elementIds.put(s, o);
        }

        @Override
        public Callable<?> resolve(final String s, Class aClass) throws SAXException {
            return new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    if (elementIds == null) return null;
                    return elementIds.get(s);
                }
            };
        }

    }

}
