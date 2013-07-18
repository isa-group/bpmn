package es.us.isa.bpms.ppinot.db;

import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import es.us.isa.bpms.BpmsConfig;
import es.us.isa.bpms.ppinot.db.ActivityDAO.ActivityMapper;
import es.us.isa.bpms.ppinot.db.DataConditionDAO.DataConditionMapper;
import es.us.isa.bpms.ppinot.db.DataobjectDAO.DataobjectMapper;
import es.us.isa.bpms.ppinot.db.ProcessDAO.ProcessMapper;


/**
 * Factory de las sesiones para la conexion a la BD
 * 
 * @author Edelia Garcia Gonzalez
 * @version 1.0
 *
 */
public class ConnectionFactory {

	private static SqlSessionFactory sqlSessionFactory = null;
	
	static {
	
		try {
			
			if (sqlSessionFactory == null) {
				
				PooledDataSource ds = new PooledDataSource();      
				ds.setDriver(BpmsConfig.DBDRIVER);      
				ds.setUrl(BpmsConfig.DBURL);      
				ds.setUsername(BpmsConfig.DBUSERNAME);      
				ds.setPassword(BpmsConfig.DBPASSWORD);  
			    
				Environment env = new Environment("development", new JdbcTransactionFactory(), ds); 
				Configuration config = new Configuration(env); 
				  
				config.addMapper(ProcessMapper.class);
				config.addMapper(ActivityMapper.class);
				config.addMapper(DataobjectMapper.class);
				config.addMapper(DataConditionMapper.class);
				  
				SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder(); 
				
				sqlSessionFactory = builder.build(config); 
			}
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
	/**
	 * Devuelve una sesion para la conexion a la BD
	 * 
	 * @return Sesion
	 */
	public static SqlSessionFactory getSqlSessionFactory() {
	
		return sqlSessionFactory;
	}

}

