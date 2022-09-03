package com.mybook.Service;

import com.mybook.Dao.UserDao;
import com.mybook.pojo.User;

import java.sql.SQLException;

public class UserService {
    UserDao userDao = new UserDao();

    public User getUser(String name, String pwd){
        User user = null;
        try {
            user = userDao.getUser(name,pwd);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }
    public int modifyPwd(long id,String pwd){
        int cnt = 0;
        try {
            cnt = userDao.modifyPwd(id, pwd);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cnt;
    }
}
