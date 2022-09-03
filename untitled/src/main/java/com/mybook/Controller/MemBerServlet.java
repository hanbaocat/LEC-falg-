package com.mybook.Controller;

import com.alibaba.fastjson.JSON;
import com.mybook.Service.MemberService;
import com.mybook.Service.MemberTypeService;
import com.mybook.Service.RecordService;
import com.mybook.pojo.Member;
import com.mybook.pojo.MemberType;
import com.mybook.pojo.Record;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jdk.nashorn.internal.runtime.JSONFunctions;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/member.let")
public class MemBerServlet extends HttpServlet {
    MemberTypeService memberTypeService = new MemberTypeService();
    MemberService memberService = new MemberService();
    RecordService recordService = new RecordService();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       doPost(req,resp);
    }
    /**
     * /member.let?type=addpre 添加准备(MemberTypes)
     * /member.let?type=add
     * /member.let?type=modifypre&id=xx 修改准备(MemberTypes ,Member)
     * /member.let?type=modify 修改
     * /member.let?type=remove&id=xx 删除
     * /member.let?type=query
     * /member.let?type=modifyrecharge 充值
     * /member.let?type=doajax&idn=xx 通过ajax请求会员信息
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //设置编码
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession();
        if(session.getAttribute("user")==null){
            out.println("<script>alert('请登录');parent.window.location.href='login.html';</script>");
            return;
        }

        //匹配方法
        String method = request.getParameter("type");
        switch (method){
            case "addpre":
                addpre(request,response,out);
                break;
            case "add":
                add(request,response,out);
                break;
            case "modifypre":
                long id =  Long.parseLong(request.getParameter("id"));
                Member member = memberService.getById(id);
                request.setAttribute("member",member);
                List<MemberType> memberTypes = memberTypeService.getAll();
                request.setAttribute("memberTypes",memberTypes);
                request.getRequestDispatcher("mem_modify.jsp").forward(request,response);
                break;
            case "modify":
                modify(request,response,out);
                break;
            case "remove":
                remove(request,response,out);
                break;
            case "query":
                List<Member> memberList = memberService.getAll();
                request.setAttribute("memberList",memberList);
                request.getRequestDispatcher("mem_list.jsp").forward(request,response);
                break;
            case "modifyrecharge":
                modifyrecharge(request,response,out);
                break;
            case "doajax":
                //获取身份证
                String idNum = request.getParameter("idn");
                //获取member对象
                Member member1 = memberService.getByIdNumber(idNum);
                //修改member借书数量
                List<Record> list = recordService.getRecordsById(member1.getId());
                if(list.size()>0){
                    int size = member1.getType().getAmount() - list.size();
                    member1.getType().setAmount(size);
                }
                //member-->json字符串
                String memJson = JSON.toJSONString(member1);
                //响应客户端
                out.print(memJson);
                //
                break;
        }
    }

    private void modifyrecharge(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
        String idNumber = request.getParameter("idNumber");
        double amount = Double.parseDouble(request.getParameter("amount"));
        Member member = memberService.getByIdNumber(idNumber);
        member.setBalance(member.getBalance()+amount);
        int cnt = memberService.modify(member.getId(), member.getName(), member.getPwd(), member.getTypeId(), member.getBalance(), member.getTel(), member.getIdNumber());
        if(cnt>0){
            out.println("<script>alert('充值成功'); location.href='member.let?type=query';</script>");
        }else{
            out.println("<script>alert('充值失败'); location.href='member.let?type=query';</script>");
        }
    }

    private void addpre(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws ServletException, IOException {
        //获取所有会员类型
        List<MemberType> memberTypes = memberTypeService.getAll();
        //存到request
        request.setAttribute("memberTypes",memberTypes);
        //转发
        request.getRequestDispatcher("mem_add.jsp").forward(request,response);
    }

    private void modify(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
        long id =  Long.parseLong(request.getParameter("id"));
        String name =  request.getParameter("name");
        String pwd =  request.getParameter("pwd");
        long memberTypeId =  Long.parseLong(request.getParameter("memberType"));
        double  balance = Double.parseDouble(request.getParameter("balance"));
        String tel =  request.getParameter("tel");
        String idNumber =  request.getParameter("idNumber");
        int count = memberService.modify(id,name,pwd,memberTypeId,balance,tel,idNumber);
        if(count>0){
            out.println("<script>alert('修改成功'); location.href='member.let?type=query';</script>");
        }else{
            out.println("<script>alert('修改失败'); location.href='member.let?type=query';</script>");
        }
    }

    private void add(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
        String name =  request.getParameter("name");
        String pwd =  request.getParameter("pwd");
        long memberTypeId =  Long.parseLong(request.getParameter("memberType"));
        double  balance = Double.parseDouble(request.getParameter("balance"));
        String tel =  request.getParameter("tel");
        String idNumber =  request.getParameter("idNumber");
        int count = memberService.add(name,pwd,memberTypeId,balance,tel,idNumber);
        if(count>0){
            out.println("<script>alert('会员开卡成功'); location.href='member.let?type=query';</script>");
        }else{
            out.println("<script>alert('会员开卡失败'); location.href='member.let?type=query';</script>");
        }
    }
    private void remove(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
        long id =  Long.parseLong(request.getParameter("id"));
        try {
            int cnt = memberService.remove(id);
            out.println("<script>alert('删除成功'); location.href='member.let?type=query';</script>");
        } catch (Exception e) {
            out.println("<script>alert('"+e.getLocalizedMessage()+"'); location.href='member.let?type=query';</script>");
        }
    }
}
