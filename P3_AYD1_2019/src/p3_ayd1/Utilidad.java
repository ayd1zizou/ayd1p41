package p3_ayd1;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.zkoss.zk.ui.util.GenericForwardComposer;




@SuppressWarnings("rawtypes")
public class Utilidad extends GenericForwardComposer{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static QueryRunner qRunner;
	private static ScalarHandler scalarHandler;
	
	public static Connection getConection(){
		Connection conexion=null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
         try {
			conexion = DriverManager.getConnection("jdbc:mysql://localhost/mydb?useSSL=true","root","1593");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return conexion;
	}
	
	


	private static QueryRunner getQueryRunner() {
		if (qRunner == null) {
			qRunner = new QueryRunner();
		}
		return qRunner;
	}
	
	private static ScalarHandler getScalarHandler() {
		if (scalarHandler == null) {
			scalarHandler = new ScalarHandler();
		}
		return scalarHandler;
	}
	

	
	@SuppressWarnings("deprecation")
	public static Object ejecutaConsulta(String query)  {
		System.out.println("Consulta:" + query);
		Object[] params=null;
		Object temp = null;
		try {
			temp = getQueryRunner().query(getConection(), query, getScalarHandler(),
					params);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return temp;
	}

	
	public static List<Map<String, Object>> ejecutaConsultaList(
		 String sql ) throws SQLException {
		Connection con =getConection();
		Object[] params=null;
		MapListHandler mapListHandler = new MapListHandler();
		return getQueryRunner().query(con, sql, mapListHandler, params);
	}
	
	@SuppressWarnings("unchecked")
	public static List ejecutaConsultaList( String sql, BeanListHandler bean)  {
		 Object[] params=null;
		 Connection enlace=getConection();
		try {
			try {
				return getQueryRunner().query(enlace, sql, bean, params);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			return null;
		}		
	}
	
	public static int ejecutaUpdate( String sql) {
		Object[] params=null;
		Connection enlace=getConection();
		int temp = 0;
		try {
			temp = getQueryRunner().update(enlace, sql, params);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return temp;

	}
	
	
}
