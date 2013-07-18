package es.us.isa.bpms.ppinot.db;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.session.SqlSession;

/**
 * DAO para manipular la informacion en la BD de una actividad. Crea y utiliza las tablas bpms_activity y
 * bpms_activity_event.
 *  
 * @author Edelia Garcia Gonzalez
 * @version 1.0
 * @see ActivityEntity
 */
public class ActivityDAO {
	
	/**
	 * Mapper para manipular la informacion en la BD de una actividad
	 * 
	 * @author Edelia Garcia Gonzalez
	 */
	public interface ActivityMapper {
		
		final String CREATETABLEACTIVITY = "CREATE TABLE IF NOT EXISTS bpms_activity (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, process_def_id VARCHAR(100) NOT NULL, activity_id VARCHAR(100) NOT NULL, activity_name VARCHAR(100) NOT NULL, activity_type VARCHAR(100) NOT NULL )";
		@Update(CREATETABLEACTIVITY)
		public int createTableActivity() throws Exception;
		
		final String CREATETABLEEVENT = "CREATE TABLE IF NOT EXISTS bpms_activity_event (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, process_def_id VARCHAR(100) NOT NULL, instance_id VARCHAR(100) NOT NULL, execution_id VARCHAR(100) NOT NULL, activity_id VARCHAR(100) NOT NULL, username VARCHAR(255) NOT NULL, start_time TIMESTAMP NOT NULL, end_time TIMESTAMP )";
		@Update(CREATETABLEEVENT)
		public int createTableActivityEvent() throws Exception;
		
		final String SELECTALLBYACTIVITY = "SELECT a.id as ida, e.id as ide, e.process_def_id as processDefId, instance_id as instanceId, execution_id as executionId, e.activity_id as activityId, activity_name as activityName, activity_type as activityType, start_time as startTime, end_time as endTime FROM bpms_activity AS a LEFT JOIN bpms_activity_event AS e ON a.process_def_id=e.process_def_id AND a.activity_id=e.activity_id WHERE a.process_def_id = #{processDefId} AND  a.activity_id = #{activityId}";
		@Select(SELECTALLBYACTIVITY)
		public List<ActivityEntity> selectAllByActivity(@Param("processDefId") final String processDefId, @Param("activityId") final String activityId) throws Exception;
		
		final String SELECTUSERACTIVITY = "SELECT e.id as ide, e.process_def_id as processDefId, instance_id as instanceId, execution_id as executionId, e.activity_id as activityId, username FROM bpms_activity_event AS e WHERE e.process_def_id = #{processDefId} AND e.instance_id = #{instanceId} AND  e.activity_id = #{activityId} ORDER BY end_time ASC";
		@Select(SELECTUSERACTIVITY)
		public List<ActivityEntity> selectUserActivity(@Param("processDefId") final String processDefId, @Param("instanceId") final String instanceId, @Param("activityId") final String activityId) throws Exception;
		
		final String INSERTACTIVITY = "INSERT INTO bpms_activity (process_def_id, activity_id, activity_name, activity_type) VALUES (#{processDefId}, #{activityId}, #{activityName}, #{activityType})";
		@Insert(INSERTACTIVITY)
		@SelectKey(statement="call identity()", keyProperty="ida", before=false, resultType=int.class)
		public int insertActivity(final ActivityEntity entity) throws Exception;
		
		final String INSERTSTART = "INSERT INTO bpms_activity_event (process_def_id, instance_id, execution_id, activity_id, start_time, username) VALUES (#{processDefId}, #{instanceId}, #{executionId}, #{activityId}, #{startTime}, '')";
		@Insert(INSERTSTART)
		@SelectKey(statement="call identity()", keyProperty="ide", before=false, resultType=int.class)
		public int insertStart(final ActivityEntity entity) throws Exception;
		
		final String UPDATEEND = "UPDATE bpms_activity_event SET end_time = #{endTime} WHERE process_def_id = #{processDefId} AND  instance_id = #{instanceId} AND  activity_id = #{activityId} AND end_time IS NULL";
		@Update(UPDATEEND)
		public int updateEnd(@Param("processDefId") final String processDefId, @Param("instanceId") final String instanceId, @Param("activityId") final String activityId, @Param("endTime") final Date endTime) throws Exception;
		
		final String UPDATEUSERNAME = "UPDATE bpms_activity_event SET username = #{username} WHERE process_def_id = #{processDefId} AND  instance_id = #{instanceId} AND  activity_id = #{activityId}";
		@Update(UPDATEUSERNAME)
		public int updateUsername(@Param("processDefId") final String processDefId, @Param("instanceId") final String instanceId, @Param("activityId") final String activityId, @Param("username") final String username) throws Exception;
	}
	
	static {
		
		// si no existe, crea las tablas en la BD 
		try {
			
			createTable();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}

	/**
	 * Si no existen, crea las tablas bpms_activity y bpms_activity_event
	 * 
	 * @throws Exception
	 */
	private static void createTable() throws Exception {
	
		SqlSession session = ConnectionFactory.getSqlSessionFactory().openSession();
		
		try {
				
			ActivityMapper mapper = session.getMapper(ActivityMapper.class);
			mapper.createTableActivity();
			mapper.createTableActivityEvent();
			
            session.commit();
		} finally {
			
			session.close();
		}
	}

	/**
	 * Conserva en la BD que se inicio una actividad
	 * 
	 * @param processDefId Id del proceso en la plataforma BPM
	 * @param instanceId Id de la intancia de proceso
	 * @param executionId Id de la ejecucion
	 * @param activityId Id de la actividad
	 * @param activityName Nombre de la actividad
	 * @param activityType Tipo de la actividad
	 * @param startTime Momento de inicio
	 * @return Cantidad de filas afectadas
	 * @throws Exception
	 */
	public static int saveStart(String processDefId, String instanceId, String executionId, String activityId, String activityName, String activityType, Date startTime) throws Exception {
		
		SqlSession session = ConnectionFactory.getSqlSessionFactory().openSession();
		
		try {
		
			ActivityMapper mapper = session.getMapper(ActivityMapper.class);
			List<ActivityEntity> list = mapper.selectAllByActivity(processDefId, activityId);
			int answer = 1;
			if (list==null || list.size()==0)
				answer = mapper.insertActivity(new ActivityEntity(processDefId, instanceId, executionId, activityId, activityName, activityType, "", startTime, null));
			if (answer!=0)
				answer = mapper.insertStart(new ActivityEntity(processDefId, instanceId, executionId, activityId, activityName, activityType, "", startTime, null));

			session.commit();
			
			return answer;
		} finally {
			
			session.close();
		}
	}
	
	/**
	 * Conserva en la BD el usuario que ejecuto una tarea
	 * 
	 * @param processDefId Id del proceso en la plataforma BPM
	 * @param instanceId Id de la intancia de proceso
	 * @param activityId Id de la actividad
	 * @param username Nombre de usuario
	 * @return Cantidad de filas afectadas
	 * @throws Exception
	 */
	public static int saveUsername(String processDefId, String instanceId, String activityId, String username) throws Exception {
	
		SqlSession session = ConnectionFactory.getSqlSessionFactory().openSession();
		
		try {
		
			ActivityMapper mapper = session.getMapper(ActivityMapper.class);
			int answer = mapper.updateUsername(processDefId, instanceId, activityId, username);
			
            session.commit();
			
			return answer;
		
		} finally {
			
			session.close();
		}
	}
	
	/**
	 * Conserva en la BD que finalizo una actividad
	 * 
	 * @param processDefId Id del proceso en la plataforma BPM
	 * @param instanceId Id de la intancia de proceso
	 * @param activityId Id de la actividad
	 * @param endTime Momento en que finalizo
	 * @return Cantidad de filas afectadas
	 * @throws Exception
	 */
	public static int saveEnd(String processDefId, String instanceId, String activityId, Date endTime) throws Exception {
	
		SqlSession session = ConnectionFactory.getSqlSessionFactory().openSession();
		
		try {
		
			ActivityMapper mapper = session.getMapper(ActivityMapper.class);
			int answer = mapper.updateEnd(processDefId, instanceId, activityId, endTime);
			
            session.commit();
			
			return answer;
		
		} finally {
			
			session.close();
		}
	}
	
	/**
	 * Devuelve el usuario que ejecuto la ultima ocurrencia de una actividad en una instancia de proceso
	 * 
	 * @param processDefId
	 * @param instanceId
	 * @param activityId
	 * @return nombre de usuario
	 * @throws Exception 
	 */
	public static String getUSer(String processDefId, String instanceId, String activityId) throws Exception {
		
		SqlSession session = ConnectionFactory.getSqlSessionFactory().openSession();
		
		try {
		
			ActivityMapper mapper = session.getMapper(ActivityMapper.class);
			List<ActivityEntity> list = mapper.selectUserActivity(processDefId, instanceId, activityId);
			
			return list.get(list.size()-1).getUsername();
		} finally {
			
			session.close();
		}
	}
}

