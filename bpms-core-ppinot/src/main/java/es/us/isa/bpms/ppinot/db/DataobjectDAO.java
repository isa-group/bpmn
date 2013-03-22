package es.us.isa.bpms.ppinot.db;

import java.util.Date;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.session.SqlSession;

/**
 * DAO para manipular la informacion en la BD de una propiedad de un dataobject en una instancia de proceso. 
 * Crea y utiliza la tabla bpms_dataobject.
 *  
 * @author Edelia Garcia Gonzalez
 * @version 1.0
 * @see DataobjectEntity
 */
public class DataobjectDAO {
	
	/**
	 * Mapper para manipular la informacion en la BD de una propiedad de un dataobject en una instancia de proceso
	 * 
	 * @author Edelia Garcia Gonzalez
	 */
	public interface DataobjectMapper {
		
		final String CREATETABLE = "CREATE TABLE IF NOT EXISTS bpms_dataobject (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, process_def_id VARCHAR(100) NOT NULL, instance_id VARCHAR(100) NOT NULL, execution_id VARCHAR(100) NOT NULL, activity_id VARCHAR(100) NOT NULL, dataobject_id VARCHAR(100) NOT NULL, property VARCHAR(100) NOT NULL, value VARCHAR(100) NOT NULL, type VARCHAR(100) NOT NULL, time TIMESTAMP NOT NULL )";
		@Update(CREATETABLE)
		public int createTable() throws Exception;
		
		final String SELECTPROPERTY = "SELECT id, process_def_id as processDefId, instance_id as instanceId, execution_id as executionId, activity_id as activityId, dataobject_id as dataobjectId, property, value, type, time FROM bpms_dataobject WHERE process_def_id = #{processDefId} AND  instance_id = #{instanceId} AND  dataobject_id = #{dataobjectId} AND  property = #{property}";
		@Select(SELECTPROPERTY)
		public DataobjectEntity selectByProperty(@Param("processDefId") final String processDefId, @Param("instanceId") final String instanceId, @Param("dataobjectId") final String dataobjectId, @Param("property") final String property) throws Exception;
		
		final String INSERTSTART = "INSERT INTO bpms_dataobject (process_def_id, instance_id, execution_id, activity_id, dataobject_id, property, value, type, time) VALUES (#{processDefId}, #{instanceId}, #{executionId}, #{activityId}, #{dataobjectId}, #{property}, #{value}, #{type}, #{time})";
		@Insert(INSERTSTART)
		@SelectKey(statement="call identity()", keyProperty="id", before=false, resultType=int.class)
		public int insert(final DataobjectEntity entity) throws Exception;
		
		final String UPDATEVALUE = "UPDATE bpms_dataobject SET execution_id = #{executionId}, activity_id = #{activityId}, value = #{value}, time = #{time} WHERE process_def_id = #{processDefId} AND  instance_id = #{instanceId} AND  dataobject_id = #{dataobjectId} AND  property = #{property}";
		@Update(UPDATEVALUE)
		public int updateValue(@Param("processDefId") final String processDefId, @Param("instanceId") final String instanceId, @Param("executionId") final String executionId, @Param("activityId") final String activityId, @Param("dataobjectId") final String dataobjectId, @Param("property") final String property, @Param("value") final String value, @Param("time") final Date time) throws Exception;
	}
	
	static {
		
		// si no existe, crea la tabla en la BD 
		try {
			
			createTable();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}

	/**
	 * Si no existe, crea la tabla bpms_dataobject 
	 * 
	 * @throws Exception
	 */
	public static void createTable() throws Exception {
	
		SqlSession session = ConnectionFactory.getSqlSessionFactory().openSession();
		
		try {
				
			DataobjectMapper mapper = session.getMapper(DataobjectMapper.class);
			mapper.createTable();
			
            session.commit();
		} finally {
			
			session.close();
		}
	}

	/**
	 * Conserva en la BD el ultimo cambio realizado en una propiedad de un dataobject en una instancia de proceso
	 * 
	 * @param processDefId Id del proceso en la plataforma BPM
	 * @param instanceId Id de la intancia de proceso
	 * @param executionId Id de la ejecucion
	 * @param activityId Id de la actividad
	 * @param dataobjectId Id del dataobject
	 * @param property Id de la propiedad
	 * @param value Valor de la propiedad
	 * @param type Tipo de la propiedad
	 * @param time Momento en que se realizo el cambio
	 * @return Cantidad de filas afectadas
	 * @throws Exception
	 */
	public static int saveChange(String processDefId, String instanceId, String executionId, String activityId, String dataobjectId, String property, String value, String type, Date time) throws Exception {
		
		SqlSession session = ConnectionFactory.getSqlSessionFactory().openSession();
		
		try {
		
			DataobjectMapper mapper = session.getMapper(DataobjectMapper.class);
			
			DataobjectEntity entity = mapper.selectByProperty(processDefId, instanceId, dataobjectId, property);
			int answer;
			if (entity==null)
				answer = mapper.insert(new DataobjectEntity(processDefId, instanceId, executionId, activityId, dataobjectId, property, value, type, time));
			else
				answer = mapper.updateValue(processDefId, instanceId, executionId, activityId, dataobjectId, property, value, time);
				
			session.commit();
			
			return answer;
		} finally {
			
			session.close();
		}
	}
}
