package com.mybook.Dao;

import com.mybook.pojo.Book;
import com.mybook.util.DBHelper;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class BookDao {
    QueryRunner runner = new QueryRunner();
    public List<Book> getBooksByTypeId(long id) throws SQLException {
        Connection conn = DBHelper.getConnection();
        String sql = "select * from book where typeid = ?";
        List<Book> books = runner.query(conn, sql, new BeanListHandler<>(Book.class), id);
        DBHelper.close(conn);
        return books;
    }

    public int add (long typeId,String name,double price,String desc,String pic,
                    String publish,String author,long stock,String address) throws SQLException {
        Connection conn = DBHelper.getConnection();
        String sql="insert into book(typeId,`name`,price,`desc`,pic,publish,author,stock,address) " +
                "values(?,?,?,?,?,?,?,?,?)";
        int cnt = runner.update(conn, sql, typeId, name, price, desc, pic, publish, author, stock, address);
        DBHelper.close(conn);
        return cnt;
    }
    public int remove (long id) throws SQLException {
        Connection conn = DBHelper.getConnection();
        String sql="delete from book where id = ?";
        int cnt = runner.update(conn, sql, id);
        DBHelper.close(conn);
        return cnt;
    }
    public int modify(long id,long typeId,String name,double price,String desc,String pic,
                      String publish,String author,long stock,String address) throws SQLException {
        Connection conn = DBHelper.getConnection();
        String sql="update book set typeId= ?,`name` = ?,price =?,`desc`=?,pic = ?,publish = ?,author =?,stock=?,address = ? where id= ? ";
        int cnt = runner.update(conn, sql, typeId, name, price, desc, pic, publish, author, stock, address,id);
        DBHelper.close(conn);
        return cnt;
    }
    public int getCount() throws SQLException {
        Connection conn = DBHelper.getConnection();
        String sql="select count(*) from book";
        int cnt = (int)((long)runner.query(conn,sql,new ScalarHandler<>()));
        DBHelper.close(conn);
        return cnt;
    }
    public List<Book> getByPage(int pageIndex,int pageSize) throws SQLException {
        Connection conn = DBHelper.getConnection();
        String sql = "select * from book limit ?,?";
        int offset = (pageIndex-1)*pageSize;
        List<Book> list = runner.query(conn, sql, new BeanListHandler<>(Book.class), offset, pageSize);
        DBHelper.close(conn);
        return list;
    }
    public Book getBookById(long id) throws SQLException {
        Connection conn = DBHelper.getConnection();
        String sql="select * from book where id =?";
        Book book = runner.query(conn, sql, new BeanHandler<>(Book.class), id);
        DBHelper.close(conn);
        return book;
    }
    //根据名字查询书籍
    public Book getBookByName(String name) throws SQLException {
        Connection conn = DBHelper.getConnection();
        String sql="select * from book where name =?";
        Book book = runner.query(conn, sql, new BeanHandler<>(Book.class), name);
        DBHelper.close(conn);
        return book;
    }

    public int modify(long bookId, int amount) throws SQLException {
        String sql = "update book set stock = stock + ? where id = ?";
        Connection conn = DBHelper.getConnection();
        int cnt = runner.update(conn, sql, amount, bookId);
        DBHelper.close(conn);
        return cnt;
    }
}
