package com.mybook.Dao;

import com.mybook.pojo.Member;
import com.mybook.pojo.Record;
import com.mybook.util.DBHelper;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class RecordDao {
    QueryRunner runner = new QueryRunner();

    //根据身份证号查询借阅数量
    public List<Record> getRecordsByIdNum(String idNumber) throws SQLException {
        Connection conn = DBHelper.getConnection();
        String sql = "select * from record where memberId = (select id from member where idNumber = ?)";
        List<Record> list = runner.query(conn, sql, new BeanListHandler<>(Record.class), idNumber);
        DBHelper.close(conn);
        return list;
    }
    //根据会员编号查询借阅数量
    public List<Record> getRecordsById(long id) throws SQLException {
        Connection conn = DBHelper.getConnection();
        String sql = "select * from record where memberId = ? and backDate is null";
        List<Record> list = runner.query(conn, sql, new BeanListHandler<>(Record.class), id);
        DBHelper.close(conn);
        return list;
    }

    //添加一个
    public int add(long memberId, long bookId, double deposit, long userId) throws SQLException {
        String sql="insert into record values(null,?,?,CURRENT_DATE,null,?,?,'978-7-302-12260-9')";
        Connection conn = DBHelper.getConnection();
        int cnt = runner.update(conn, sql, memberId, bookId, deposit, userId);
        DBHelper.close(conn);
        return cnt;
    }
    //归还一个
    public int back(long userId, long id,double deposit) throws SQLException {
        String sql = "update record set backDate = CURRENT_DATE , deposit = ?,userId =  ? where id = ?";
        Connection conn = DBHelper.getConnection();
        int cnt = runner.update(conn, sql,deposit,userId,id);
        DBHelper.close(conn);
        return cnt;
    }

    public Record getById(long recordId) throws SQLException {
        Connection conn = DBHelper.getConnection();
        String sql="select * from  record where id=?";
        Record record = runner.query(conn,sql,new BeanHandler<>(Record.class),recordId);
        DBHelper.close(conn);
        return  record;
    }

    public int modify(long id) throws SQLException {
        String sql = "update record set rentDate = CURRENT_DATE where id = ?";
        Connection conn = DBHelper.getConnection();
        int cnt = runner.update(conn, sql,id);
        DBHelper.close(conn);
        return cnt;
    }
    public  List<Map<String,Object>>  query(int typeId,String keyWork) throws SQLException {
        Connection conn = DBHelper.getConnection();
        StringBuilder sb = new StringBuilder("select * from recordView where 1=1 ");
        switch (typeId){
            case 0:
                break;
            case 1:
                sb.append("and  backDate is not null ");
                break;
            case 2:
                sb.append("and  backDate is  null ");
                break;
            case 3:
                sb.append("and  backDate is null and  returnDate < date_add(CURRENT_DATE,interval 7 DAY) ");
                break;
        }
        if(keyWork!=null){
            sb.append(" and bookName like '%"+keyWork+"%' or memberName like '%"+keyWork+"%' or   concat(rentDate,'') like '%"+keyWork+"%'");
        }
        List<Map<String,Object>> data =runner.query(conn,sb.toString(),new MapListHandler());
        DBHelper.close(conn);
        return data;
    }
}
