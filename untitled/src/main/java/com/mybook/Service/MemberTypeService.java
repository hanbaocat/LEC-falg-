package com.mybook.Service;

import com.mybook.Dao.MemberTypeDao;
import com.mybook.pojo.MemberType;

import java.sql.SQLException;
import java.util.List;

public class MemberTypeService {
    MemberTypeDao dao = new MemberTypeDao();

    public List<MemberType> getAll(){
        try {
            return dao.getAlL();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }
    public MemberType getById(long id){
        MemberType memberType = null;

        try {
            memberType = dao.getById(id);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return memberType;

    }
}
