package com.mybook.Service;

import com.mybook.Dao.BookDao;
import com.mybook.Dao.MemberDao;
import com.mybook.Dao.RecordDao;
import com.mybook.pojo.Book;
import com.mybook.pojo.Member;
import com.mybook.pojo.Record;
import com.mybook.util.DBHelper;
import com.mybook.util.DateHelper;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class RecordService {
    RecordDao recordDao = new RecordDao();
    BookDao bookDao = new BookDao();
    MemberDao memberDao = new MemberDao();
    MemberService memberService = new MemberService();
    public List<Record> getRecordsById(long id){
        List<Record> list = null;
        try {
            list = recordDao.getRecordsById(id);
            //外键信息
            //获取会员对象
            Member member = memberService.getById(id);
            for(Record record : list){
                Book book = bookDao.getBookById(record.getBookId());
                record.setBook(book);
                record.setMember(member);
                //应还时间
                long days = member.getType().getKeepDay();
                //时间计算
                Date rentDate = record.getRentDate();
                //转成毫秒数
                long mills = rentDate.getTime();
                mills += days*24*60*60*1000;
                Date backDate = new Date(mills);
                record.setBackDate(backDate);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public List<Record> getRecordsByIdNum(String idNUm){
        List<Record> list = null;
        try {
            list = recordDao.getRecordsByIdNum(idNUm);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     *借阅，record表添加信息
     * 此书数量-1
     * 会员表中更改余额
     * 事务处理
     * @param memberId
     * @param bookIdList
     * @param userId
     * @return
     */
    public int add(long memberId,List<Long> bookIdList,long userId){
        try {
            //1.启动事务
            DBHelper.beginTransaction();
            double total = 0;
            //2.拿到借阅书籍编号
            for(long bookId:bookIdList){
                //获取书籍对象
                Book book = bookDao.getBookById(bookId);
                //得到价格
                double price = book.getPrice();
                //算押金
                double resPrice = price*0.3;
                total += resPrice;
                //record表添加数据
                recordDao.add(memberId, bookId,resPrice, userId);
                //book加减数量
                bookDao.modify(bookId,-1);
            }
            //修改余额
            memberDao.modifyBalance(memberId,0-total);
            DBHelper.commitTransaction();
        }catch (Exception e){
            try {
                DBHelper.rollbackTransaction();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            return 0;
        }
        return 1;
    }

    public int back(long memberId, List<Long> recordIds, long userId) {
        try {
            //1.启动事务
            DBHelper.beginTransaction();
            Member member = memberService.getById(memberId);
            double total = 0;
            //2.拿到借阅书籍编号
            for(long recordId:recordIds){
                //获取record对象
                Record record = recordDao.getById(recordId);
                //计算押金，超时一天扣一块
                Date newDate = DateHelper.getNewDate(record.getRentDate(), member.getType().getKeepDay());
                java.util.Date currentDate = new java.util.Date();
                int day = 0;
                if (currentDate.after(newDate)){
                    //算天数
                    day = DateHelper.getSapn(newDate,currentDate);
                }
                total -= day;
                total += record.getDeposit();
                recordDao.back(userId,recordId, day);
                //book加减数量
                bookDao.modify(record.getBookId(),1);
            }
            //修改余额
            memberDao.modifyBalance(memberId,total);
            DBHelper.commitTransaction();
        }catch (Exception e){
            try {
                DBHelper.rollbackTransaction();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            return 0;
        }
        return 1;
    }

    public int modify(long id) {
        int cnt = 0;
        try {
            cnt = recordDao.modify(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return cnt;
    }
    public List<Map<String,Object>> query(int typeId,String keyWork){
        List<Map<String, Object>> list;
        try {
           list = recordDao.query(typeId, keyWork);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
}

