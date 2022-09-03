package com.mybook.Controller;

import com.mybook.Service.UserService;
import com.mybook.pojo.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/user.let")
public class UserController extends HttpServlet {
    UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    /**
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession();
        //判断请求类型
        String method = request.getParameter("type");
        switch (method){
            case "login":
                //获取用户名和密码
                String name = request.getParameter("name");
                String pwd = request.getParameter("pwd");
                String userCode = request.getParameter("valCode").toLowerCase();
                String realCode = session.getAttribute("code").toString();
                //查询用户
                User user = userService.getUser(name,pwd);

                if(!userCode.equals(realCode)){
                    out.println("<script>alert('验证码错误');location.href='login.html';</script/>");
                    return;
                }
                //判断是否成功
                //成功进入主页面
                //失败退回登录界面
                if(user != null){
                    session.setAttribute("user",user);
                    out.println("<script>alert('登陆成功');location.href='index.jsp';</script>");
                }else{
                    out.println("<script>alert('用户名或者密码错误');location.href='login.html';</script/>");
                }
                break;
            case "exit":
                //清除session
                session.invalidate();
                //跳转
                out.println("<script>parent.window.location.href='login.html';</script/>");
                break;
            case "modifyPwd":
                //验证原密码
                User nowUser = (User)session.getAttribute("user");
                if(!nowUser.getPwd().equals(request.getParameter("pwd"))){
                    out.println("<script>alert('原密码错误');</script>");
                    return ;
                }
                //获取新密码
                String newpwd1 = (String)request.getParameter("newpwd");
                String newpwd2 = (String)request.getParameter("newpwd2");
                if (!newpwd1.equals(newpwd2)){
                    out.println("<script>alert('两次密码不同');</script>");
                }
                //获取用户id
                Long id = nowUser.getId();
                //调用方法修改密码
                int cnt = userService.modifyPwd(id, newpwd1);
                //跳转
                if(cnt!=0){
                    out.println("<script>alert('修改成功');parent.window.location.href='login.html';</script>");
                }else {
                    out.println("<script>alert('修改失败');</script>");
                }
                break;
        }
    }
}
