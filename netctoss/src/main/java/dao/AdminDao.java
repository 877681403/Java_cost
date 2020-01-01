package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import entity.Admin;
import util.DBUtil;

public class AdminDao {
	
	public Admin findByCode(String adminCode) {
		if(adminCode == null) {
			return null;
		}
		Connection con = null;
		try {
			con = DBUtil.getConnection();
			String sql = 
				"select * from admin_info where admin_code=?";
			PreparedStatement ps = 
				con.prepareStatement(sql);
			ps.setString(1, adminCode);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				Admin a = new Admin();
				a.setAdminId(rs.getInt("admin_id"));
				a.setAdminCode(rs.getString("admin_code"));
				a.setPassword(rs.getString("password"));
				a.setName(rs.getString("name"));
				a.setTelephone(rs.getString("telephone"));
				a.setEmail(rs.getString("email"));
				a.setEnrolldate(rs.getTimestamp("enrolldate"));
				return a;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(
				"查询管理员失败");
		} finally {
			DBUtil.close(con);
		}
		return null;
	}

}








