package es.us.isa.bpms;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Formatter;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;


/**
 * Utilitarios
 * 
 * @author Edelia Garcia Gonzalez
 * @version 1.0
 *
 */
public class BpmsUtils {

	/**
	 * Genera un objeto Date a partir de una cadena que contenga una fecha en el formato configurado para el paquete
	 * 
	 * @param dateString Cadena
	 * @return Objeto Date
	 */
	public static Date parseDate(String dateString) {
		
		SimpleDateFormat dateFormat = new SimpleDateFormat( BpmsConfig.FORMATDATE );
		Date date = new Date();
		try {

			date = dateFormat.parse(dateString);
		} catch (Exception e) {
			
			date = null;
		}
		return date;
	}
	
	/**
	 * Genera un objeto Date a partir de una cadena que contenga una fecha con hora en el formato configurado para el paquete
	 * 
	 * @param dateString Cadena
	 * @return Objeto Date
	 */
	public static Date parseDateHour(String dateString) {
		
		SimpleDateFormat dateFormat = new SimpleDateFormat( BpmsConfig.FORMATDATEHOUR );
		Date date;
		try {

			date = dateFormat.parse(dateString);
		} catch (Exception e) {
			
			date = null;
		}
		return date;
	}

	/**
	 * Devuelve una cadena con la fecha en el formato configurado para el paquete
	 * 
	 * @param date Objeto Date
	 * @return Cadena con la fecha
	 */
	public static String formatString(Date date) {
		
		SimpleDateFormat dateFormat = new SimpleDateFormat( BpmsConfig.FORMATDATE );
		String string = "";
		if (date!=null) 
			string = dateFormat.format(date);
		return string;
	}

	/**
	 * Devuelve una cadena con la fecha y la hora en el formato configurado para el paquete
	 * 
	 * @param date Objeto Date
	 * @return Cadena con la fecha
	 */
	public static String formatStringHour(Date date) {
		
		SimpleDateFormat dateFormat = new SimpleDateFormat( BpmsConfig.FORMATDATEHOUR );
		String string = "";
		if (date!=null) 
			string = dateFormat.format(date);
		return string;
	}
	
	/**
	 * Convierte un Double a cadena. Devuelve la cadena vacia en el caso que se pase null.
	 * 
	 * @param doub Objeto Double
	 * @return Cadena
	 */
	public static String doubleToString(Double doub) {
		
		if (doub==null)
			return "";
		else
			return String.valueOf(doub);
	}
	
	/**
	 * Convierte una cadena a Double. Devuelve null en el caso que se pase la cadena vacia o null.
	 * 
	 * @param string Cadena
	 * @return Objeto Double
	 */
	public static Double stringToDouble(String string) {
		
		if (string==null || string=="")
			return null;
		else
			return Double.valueOf(string);
	}
	
	/**
	 * A partir de un Boolean devuelve una cadena con valor "1" o "0".
	 * 
	 * @param bool Objeto Boolean
	 * @return Cadena
	 */
	public static String booleanToStringInteger(Boolean bool) {
		
		String str = "0"; 
		if (bool) 
			str = "1";
		return str;
	}
	
	/**
	 * Formatea una cadena que contenga un valor numerico; queda con solo dos decimales. Si la cadena no contiene un valor 
	 * numerico se devuelve la cadena vacia. 
	 * 
	 * @param str Cadena con un valor numerico
	 * @return Cadena formateada
	 */
	public static String numberFormat( String str) {

		try {
			
			Double doub = Double.valueOf(str);
			
			StringBuilder sb = new StringBuilder();
			Formatter formatter = new Formatter(sb, Locale.US);
	
			formatter.format("%10.2f", doub);
			
			return sb.substring(0);
			
		} catch (Exception e) {
			
			return "";
		}
	}
	
	/**
	 * Devuelve el año actual
	 * 
	 * @return Año actual
	 */
	public static Integer currentYear() {
		
		GregorianCalendar gFechaActual = new GregorianCalendar(); 
		return gFechaActual.get(GregorianCalendar.YEAR);
	}
	
	/**
	 * Devuelve la fecha actual
	 * 
	 * @return Objeto Date
	 */
	public static Date getCurrentTime() {
		
		GregorianCalendar gFechaActual = new GregorianCalendar(); 
		return gFechaActual.getTime();
	}
	
	/**
	 * Lee un archivo texto y devuelve su contenido en una lista de cadenas. Imprime la traza de la excepcion en el caso que
	 * no se encuentre el archivo o no pueda ser leido.
	 * 
	 * @param fileName Nombre del archivo incluyendo en camino
	 * @return Lista de cadenas
	 */
	public static List<String> leerArchivo(String fileName) {
		
		File archivo = null;
	    FileReader fr = null;
	    BufferedReader br = null;

	    List<String> list = new ArrayList<String>();
	    
	    try {
	       // Apertura del fichero y creacion de BufferedReader para poder hacer una lectura comoda
	       archivo = new File( fileName );
	       fr = new FileReader( archivo );
	       br = new BufferedReader( fr );

	       // Lectura del fichero
	       String linea;
	       while((linea=br.readLine())!=null)
	          list.add( linea );
	    }
	    catch(Exception e){
	       e.printStackTrace();
	    }finally{
	       // En el finally cerramos el fichero, para asegurarnos que se cierra tanto si todo va bien como si hay excepcion
	       try{                    
	          if( null != fr ){   
	          fr.close();     
	       }                  
	       }catch (Exception e2){ 
	          e2.printStackTrace();
	       }
	    }
	    
	    return list;
	}
}
