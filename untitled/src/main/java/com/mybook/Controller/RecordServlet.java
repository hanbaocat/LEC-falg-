package com.mybook.Controller;

import com.alibaba.fastjson.JSON;
import com.mybook.Service.MemberService;
import com.mybook.Service.RecordService;
import com.mybook.pojo.Member;
import com.mybook.pojo.Record;
import com.mybook.pojo.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebServlet("/record.let")
public class RecordServlet extends HttpServlet {
    RecordService recordService = new RecordService();
    MemberService memberService = new MemberService();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession();
        if(session.getAttribute("user")==null){
            out.println("<script>alert('请登录');parent.window.location.href='login.html';</script>");
            return;
        }


        String method = request.getParameter("type");
        User user = (User)session.getAttribute("user");
        switch (method){
            case "add":
                //获取memberId
                long mid = Long.parseLong(request.getParameter("mid"));
                //获取booksId
                String ids = request.getParameter("ids");
                String[] strs = ids.split("_");
                List<Long> bookIdLit = new ArrayList<>();
                for(String s:strs){
                    bookIdLit.add(Long.parseLong(s));
                }
                //获取userId
                long userId = user.getId();
                //调用方法
                int cnt = recordService.add(mid, bookIdLit, userId);
                if(cnt>0){
                    out.println("<script>alert('图书借阅成功');location.href='main.jsp';</script>");
                }else{
                    out.println("<script>alert('图书借阅失败');location.href='main.jsp';</script>");
                }
                break;
            case "queryback"://查询所有未还书籍
                //获取会员身份证号
                String idn = request.getParameter("idn");
                //获取会员对象和所有的未还的记录
                Member member = memberService.getByIdNumber(idn);
                List<Record> list = recordService.getRecordsById(member.getId());
                //存req
                request.setAttribute("member",member);
                request.setAttribute("records",list);
                //转发
                request.getRequestDispatcher("return_list.jsp").forward(request,response);
                break;
            case "back":
                //获取memberId
                long mid2 = Long.parseLong(request.getParameter("mid"));
                //获取booksId
                String ids2 = request.getParameter("ids");
                String[] strs2 = ids2.split("_");
                List<Long> recordIdLit2 = new ArrayList<>();
                for(String s:strs2){
                    recordIdLit2.add(Long.parseLong(s));
                }
                //获取userId
                long userId2 = user.getId();
                //调用service
                int cnt2 = recordService.back(mid2,recordIdLit2, userId2);
                if(cnt2>0){
                    out.println("<script>alert('图书归还成功');location.href='main.jsp';</script>");
                }else{
                    out.println("<script>alert('图书归还失败');location.href='main.jsp';</script>");
                }
                break;
            case "keep":
                long id = Long.parseLong(request.getParameter("id"));
                int cnt3 = recordService.modify(id);
                if(cnt3>0){
                    out.println("<script>alert('图书续借成功');location.href='main.jsp';</script>");
                }else{
                    out.println("<script>alert('图书续借失败');location.href='main.jsp';</script>");
                }
                break;
            case "doajax":
                int typeId = Integer.parseInt(request.getParameter("typeId"));
                String keyword = request.getParameter("keyword");
                keyword = keyword.isEmpty()?null:keyword;
                List<Map<String, Object>> rows = recordService.query(typeId, keyword);
                out.print(JSON.toJSONString(rows));
                break;
        }
    }
}
