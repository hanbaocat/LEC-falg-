package com.mybook.Dao;

import com.mybook.pojo.User;
import com.mybook.util.DBHelper;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import java.sql.Connection;
import java.sql.SQLException;

public class UserDao {
    //创建QueryRunner对象（DBUtil）
    QueryRunner DbUtil = new QueryRunner();

    public User getUser(String name, String pwd) throws SQLException {
        //获取连接对象
        Connection conn = DBHelper.getConnection();
        //准备sql
        String sql = "select * from user where name = ? and pwd = ? and state = 1";
        //调用QueryRunner.query方法，将数据封装为User对象
        User user = DbUtil.query(conn, sql, new BeanHandler<User>(User.class), name, pwd);
        //关闭连接对象
        DBHelper.close(conn);
        //返回user
        return user;
    }
    //修改密码
    public int modifyPwd(long id,String pwd) throws SQLException {
        //获取连接对象
        Connection conn = DBHelper.getConnection();
        //准备sql
        String sql = "update user set pwd = ? where id = ?";
        int cnt = DbUtil.update(conn,sql, pwd, id);
        DBHelper.close(conn);
        return cnt;
    }
}
