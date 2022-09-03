package com.mybook.Service;

import com.mybook.Dao.BookDao;
import com.mybook.Dao.TypeDao;
import com.mybook.pojo.Book;
import com.mybook.pojo.Type;

import java.sql.SQLException;
import java.util.List;

public class TypeService {
    TypeDao typeDao = new TypeDao();
    //添加种类
    public int add(String name,long parentId){
        int cnt = 0;
        try {
            cnt = typeDao.add(name,parentId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cnt;
    }
    //删除种类
    public int remove(long id){
        //如果有子项，不能删除
        BookDao bookDao = new BookDao();
        try {
            List<Book> list = bookDao.getBooksByTypeId(id);
            if(list.size()!=0){
                //不能删除
                return -1;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        int cnt = 0;
        try {
            cnt = typeDao.remove(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cnt;
    }

    //修改种类
    public int modify(long id,String name,long parentId){
        int cnt = 0;
        try {
            cnt = typeDao.modify(id,name,parentId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cnt;
    }
    //查询种类
    public Type getTypeById(long id){
        Type type = null;
        try {
            type = typeDao.getTypeById(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return type;
    }
    public List<Type> getTypes(){
        List<Type> list = null;
        try {
            list = typeDao.getTypes();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
