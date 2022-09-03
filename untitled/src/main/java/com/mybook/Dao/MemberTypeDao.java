package com.mybook.Dao;

import com.mybook.pojo.MemberType;
import com.mybook.util.DBHelper;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import javax.management.Query;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MemberTypeDao {
    QueryRunner runner = new QueryRunner();

    public List<MemberType> getAlL() throws SQLException {
        Connection conn = DBHelper.getConnection();
        List<MemberType> list = new ArrayList<>();
        String sql = "select * from membertype";
        list = runner.query(conn,sql,new BeanListHandler<>(MemberType.class));
        DBHelper.close(conn);
        return list;
    }
    public MemberType getById(long id) throws SQLException {
        Connection conn = DBHelper.getConnection();
        String sql = "select * from membertype where id = ?";
        MemberType memberType = runner.query(conn, sql, new BeanHandler<>(MemberType.class),id);
        DBHelper.close(conn);
        return memberType;
    }
}
