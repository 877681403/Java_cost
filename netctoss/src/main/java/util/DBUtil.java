package util;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.dbcp.BasicDataSource;

public class DBUtil {

	private static BasicDataSource ds;

	static {
		try {
			Properties prop = new Properties();
			prop.load(DBUtil.class.getClassLoader().getResourceAsStream("db.properties"));
			String driverName = prop.getProperty("jdbc.driver");
			String url = prop.getProperty("jdbc.url");
			String username = prop.getProperty("jdbc.username");
			String password = prop.getProperty("jdbc.password");
			int maxActive = Integer.parseInt(prop.getProperty("ds.maxActive"));
			int maxWait = Integer.parseInt(prop.getProperty("ds.maxWait"));
			// 实例化连接池
			ds = new BasicDataSource();
			// 设置连接池参数
			ds.setDriverClassName(driverName);
			ds.setUrl(url);
			ds.setUsername(username);
			ds.setPassword(password);
			// 设置最大连接数
			ds.setMaxActive(maxActive);
			// 设置最大等待时间
			ds.setMaxWait(maxWait);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Connection getConnection() throws Exception {
		Connection conn = null;
		try {
			// 从连接池中获取连接
			conn = ds.getConnection();
		} catch (Exception e) {
			// 日志
			e.printStackTrace();
			throw e;
		}
		return conn;
	}

	/**
	 * 关闭数据库的连接
	 */
	public static void close(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) throws Exception {
		System.out.println(getConnection());
	}
}
