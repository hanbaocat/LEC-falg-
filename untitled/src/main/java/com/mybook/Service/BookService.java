package com.mybook.Service;

import com.mybook.Dao.BookDao;
import com.mybook.Dao.TypeDao;
import com.mybook.pojo.Book;
import com.mybook.pojo.Type;

import java.sql.SQLException;
import java.util.List;

public class BookService {
    BookDao bookDao = new BookDao();

    public List<Book> getBooksByTypeId(long id){
        List<Book> list = null;
        try {
            list = bookDao.getBooksByTypeId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public int add (long typeId,String name,double price,String desc,String pic,
                    String publish,String author,long stock,String address){
        int cnt = 0;
        try {
            cnt = bookDao.add(typeId,name,price,desc,pic,publish,author,stock,address);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cnt;
    }
    public int add(Book book){
        int cnt = 0;

        try {
            cnt = bookDao.add(book.getTypeId(),book.getName(),book.getPrice(),book.getDesc(),book.getPic(),book.getPublish(),book.getAuthor(),book.getStock(),book.getAddress());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cnt;
    }
    public int remove (long id){
        int cnt = 0;
        try {
            cnt = bookDao.remove(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cnt;
    }
    public int modify(long id,long typeId,String name,double price,String desc,String pic,
                      String publish,String author,long stock,String address){
        int cnt = 0;
        try {
            cnt = bookDao.modify(id,typeId,name,price,desc,pic,publish,author,stock,address);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cnt;
    }
    public int getCount() {
        int cnt = 0;
        try {
            cnt = bookDao.getCount();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cnt;
    }
    public Book getBookById(long id) {
        Book book = null;
        try {
            book = bookDao.getBookById(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return book;
    }
    public Book getBookByName(String name) {
        Book book = null;
        try {
            book = bookDao.getBookByName(name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return book;
    }
    public List<Book> getByPage(int pageIndex,int pageSize){
        List<Book> list = null;
        try {
            list = bookDao.getByPage(pageIndex,pageSize);
            TypeDao typeDao = new TypeDao();
            //给book对象设置外键
            for(Book book:list){
                long typeId = book.getTypeId();
                Type type = typeDao.getTypeById(typeId);
                book.setType(type);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public int getPageCount(int pageSize){
        int pageCount = 0;
        int row = getCount();
        pageCount = (row-1)/pageSize+1;
        return pageCount;
    }
}
