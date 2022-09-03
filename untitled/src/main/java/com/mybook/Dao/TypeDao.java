package com.mybook.Dao;

import com.mybook.pojo.Type;
import com.mybook.util.DBHelper;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class TypeDao {
    //构建QueryRunner
    QueryRunner runner = new QueryRunner();

    //添加种类
    public int add(String name,long parentId) throws SQLException {
        Connection conn = DBHelper.getConnection();
        String sql = "insert into type values(null,?,?)";
        int cnt = runner.update(conn, sql, name, parentId);
        DBHelper.close(conn);
        return cnt;
    }
    //删除种类
    public int remove(long id) throws SQLException {
        Connection conn = DBHelper.getConnection();
        String sql = "delete from type where id = ?";
        int cnt = runner.update(conn, sql, id);
        DBHelper.close(conn);
        return cnt;
    }
    //修改种类
    public int modify(long id,String name,long parentId) throws SQLException {
        Connection conn = DBHelper.getConnection();
        String sql = "update type set name = ?,parentid = ? where id = ?";
        int cnt = runner.update(conn, sql, name, parentId, id);
        DBHelper.close(conn);
        return cnt;
    }
    //查询种类
    public Type getTypeById(long id) throws SQLException {
        Connection conn = DBHelper.getConnection();
        String sql = "select * from type where id = ?";
        Type type = runner.query(conn, sql, new BeanHandler<>(Type.class), id);
        DBHelper.close(conn);
        return type;
    }
    public List<Type> getTypes() throws SQLException {
        Connection conn = DBHelper.getConnection();
        String sql = "select * from type";
        List<Type> list = runner.query(conn, sql, new BeanListHandler<>(Type.class));
        DBHelper.close(conn);
        return list;
    }

}
