package es.us.isa.bpms.ppinot.db;

import java.util.Date;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.session.SqlSession;

/**
 * DAO para manipular la informacion en la BD de un proceso. Crea y utiliza las tablas bpms_process y bpms_instance.
 *  
 * @author Edelia Garcia Gonzalez
 * @version 1.0
 * @see ProcessEntity
 */
public class ProcessDAO {
	
	/**
	 * Mapper para manipular la informacion en la BD de un proceso
	 * 
	 * @author Edelia Garcia Gonzalez
	 */
	public interface ProcessMapper {
		
		final String CREATETABLEPROCESS = "CREATE TABLE IF NOT EXISTS bpms_process (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, process_id VARCHAR(100) NOT NULL, process_def_id VARCHAR(100) NOT NULL )";
		@Update(CREATETABLEPROCESS)
		public int createTableProcess() throws Exception;
		
		final String CREATETABLEINSTANCE = "CREATE TABLE IF NOT EXISTS bpms_instance (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, process_def_id VARCHAR(100) NOT NULL, instance_id VARCHAR(100) NOT NULL, start_time TIMESTAMP NOT NULL, end_time TIMESTAMP )";
		@Update(CREATETABLEINSTANCE)
		public int createTableInstance() throws Exception;
		
		final String SELECTPROCESSDEF = "SELECT process_id as processId FROM bpms_process WHERE process_def_id = #{processDefId}";
		@Select(SELECTPROCESSDEF)
		public ProcessEntity selectByProcessDef(@Param("processDefId") final String processDefId) throws Exception;
		
		final String INSERTPROCESS = "INSERT INTO bpms_process (process_id, process_def_id) VALUES (#{processId}, #{processDefId})";
		@Insert(INSERTPROCESS)
		@SelectKey(statement="call identity()", keyProperty="idp", before=false, resultType=int.class)
		public int insertProcess(final ProcessEntity entity) throws Exception;
		
		final String INSERTSTART = "INSERT INTO bpms_instance (process_def_id, instance_id, start_time) VALUES (#{processDefId}, #{instanceId}, #{startTime})";
		@Insert(INSERTSTART)
		@SelectKey(statement="call identity()", keyProperty="idi", before=false, resultType=int.class)
		public int insertStart(final ProcessEntity entity) throws Exception;
		
		final String UPDATEEND = "UPDATE bpms_instance SET end_time = #{endTime} WHERE process_def_id = #{processDefId} AND  instance_id = #{instanceId}";
		@Update(UPDATEEND)
		public int updateEnd(@Param("processDefId") final String processDefId, @Param("instanceId") final String instanceId, @Param("endTime") final Date endTime) throws Exception;
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
	 * Si no existe, crea las tablas bpms_process y bpms_instance 
	 * 
	 * @throws Exception
	 */
	private static void createTable() throws Exception {
	
		SqlSession session = ConnectionFactory.getSqlSessionFactory().openSession();
		
		try {
				
			ProcessMapper mapper = session.getMapper(ProcessMapper.class);
			mapper.createTableProcess();
			mapper.createTableInstance();
			
            session.commit();
		} finally {
			
			session.close();
		}
	}

	/**
	 * Conserva en la BD que se inicio una instancia de proceso
	 * 
	 * @param processDefId Id del proceso en la plataforma BPM
	 * @param instanceId Id de la intancia de proceso
	 * @param startTime Momento de inicio
	 * @return Cantidad de filas afectadas
	 * @throws Exception
	 */
	public static int saveStart(String processDefId, String instanceId, Date startTime, String processId) throws Exception {
		
		SqlSession session = ConnectionFactory.getSqlSessionFactory().openSession();
		
		try {
		
			ProcessMapper mapper = session.getMapper(ProcessMapper.class);
			
			ProcessEntity entity = new ProcessEntity(processId, processDefId, instanceId, startTime, null);
			
			int answer = 1; 
			ProcessEntity tmp = mapper.selectByProcessDef(processDefId);
			if (tmp==null)
				answer = mapper.insertProcess(entity);
			if (answer==1)
				answer = mapper.insertStart(entity);

			session.commit();
			
			return answer;
		} finally {
			
			session.close();
		}
	}
	
	/**
	 * Conserva en la BD que finalizo una instancia de proceso
	 * 
	 * @param processDefId Id del proceso en la plataforma BPM
	 * @param instanceId Id de la intancia de proceso
	 * @param endTime Momento en que finalizo
	 * @return Cantidad de filas afectadas
	 * @throws Exception
	 */
	public static int saveEnd(String processDefId, String instanceId, Date endTime) throws Exception {
	
		SqlSession session = ConnectionFactory.getSqlSessionFactory().openSession();
		
		try {
		
			ProcessMapper mapper = session.getMapper(ProcessMapper.class);
			int answer = mapper.updateEnd(processDefId, instanceId, endTime);
			
            session.commit();
			
			return answer;
		
		} finally {
			
			session.close();
		}
	}
}

