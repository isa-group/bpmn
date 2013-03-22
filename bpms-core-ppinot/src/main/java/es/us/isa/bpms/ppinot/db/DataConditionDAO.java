package es.us.isa.bpms.ppinot.db;

import java.util.Date;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.session.SqlSession;

/**
 * DAO para manipular la informacion en la BD del estado de un dataobject en una instancia de proceso. 
 * Crea y utiliza la tabla bpms_datacondition.
 *  
 * @author Edelia Garcia Gonzalez
 * @version 1.0
 * @see DataConditionEntity
 */
public class DataConditionDAO {

	/**
	 * Mapper para manipular la informacion en la BD de un estado de un dataobject en una instancia de proceso
	 * 
	 * @author Edelia Garcia Gonzalez
	 */
	public interface DataConditionMapper {
		
		final String CREATETABLE = "CREATE TABLE IF NOT EXISTS bpms_datacondition (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, process_def_id VARCHAR(100) NOT NULL, instance_id VARCHAR(100) NOT NULL, execution_id VARCHAR(100) NOT NULL, activity_id VARCHAR(100) NOT NULL, dataobject_id VARCHAR(100) NOT NULL, state VARCHAR(100) NOT NULL, truth INT(1) NOT NULL, time TIMESTAMP NOT NULL )";
		@Update(CREATETABLE)
		public int createTable() throws Exception;
		
		final String SELECTSTATE = "SELECT id, process_def_id as processDefId, instance_id as instanceId, execution_id as executionId, activity_id as activityId, dataobject_id as dataobjectId, state, truth, time FROM bpms_datacondition WHERE process_def_id = #{processDefId} AND  instance_id = #{instanceId} AND  dataobject_id = #{dataobjectId} AND  state = #{state}";
		@Select(SELECTSTATE)
		public DataConditionEntity selectByState(@Param("processDefId") final String processDefId, @Param("instanceId") final String instanceId, @Param("dataobjectId") final String dataobjectId, @Param("state") final String state) throws Exception;
		
		final String INSERTSTART = "INSERT INTO bpms_datacondition (process_def_id, instance_id, execution_id, activity_id, dataobject_id, state, truth, time) VALUES (#{processDefId}, #{instanceId}, #{executionId}, #{activityId}, #{dataobjectId}, #{state}, #{truth}, #{time})";
		@Insert(INSERTSTART)
		@SelectKey(statement="call identity()", keyProperty="id", before=false, resultType=int.class)
		public int insert(final DataConditionEntity entity) throws Exception;
		
		final String UPDATEVALUE = "UPDATE bpms_datacondition SET execution_id = #{executionId}, activity_id = #{activityId}, truth = #{truth}, time = #{time} WHERE process_def_id = #{processDefId} AND  instance_id = #{instanceId} AND  dataobject_id = #{dataobjectId} AND state = #{state}";
		@Update(UPDATEVALUE)
		public int updateValue(@Param("processDefId") final String processDefId, @Param("instanceId") final String instanceId, @Param("executionId") final String executionId, @Param("activityId") final String activityId, @Param("dataobjectId") final String dataobjectId, @Param("state") final String state, @Param("truth") final Boolean truth, @Param("time") final Date time) throws Exception;
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
	 * Si no existe, crea la tabla bpms_datacondition 
	 * 
	 * @throws Exception
	 */
	public static void createTable() throws Exception {
	
		SqlSession session = ConnectionFactory.getSqlSessionFactory().openSession();
		
		try {
				
			DataConditionMapper mapper = session.getMapper(DataConditionMapper.class);
			mapper.createTable();
			
            session.commit();
		} finally {
			
			session.close();
		}
	}

	/**
	 * Conserva en la BD el ultimo cambio realizado en un estado de un dataobject en una instancia de proceso
	 * 
	 * @param processDefId Id del proceso en la plataforma BPM
	 * @param instanceId Id de la intancia de proceso
	 * @param executionId Id de la ejecucion
	 * @param activityId Id de la actividad
	 * @param dataobjectId Id del dataobject
	 * @param state Id del estado
	 * @param truth Indica si el dataobject tiene el estado o no lo tiene
	 * @param time Momento en que se realizo el cambio
	 * @return Cantidad de filas afectadas
	 * @throws Exception
	 */
	public static int saveChange(String processDefId, String instanceId, String executionId, String activityId, String dataobjectId, String state, Boolean truth, Date time) throws Exception {
		
		SqlSession session = ConnectionFactory.getSqlSessionFactory().openSession();
		
		try {
		
			DataConditionMapper mapper = session.getMapper(DataConditionMapper.class);
			
			DataConditionEntity entity = mapper.selectByState(processDefId, instanceId, dataobjectId, state);
			int answer;
			if (entity==null)
				answer = mapper.insert(new DataConditionEntity(processDefId, instanceId, executionId, activityId, dataobjectId, state, truth, time));
			else
				answer = mapper.updateValue(processDefId, instanceId, executionId, activityId, dataobjectId, state, truth, time);
				
			session.commit();
			
			return answer;
		} finally {
			
			session.close();
		}
	}
}
