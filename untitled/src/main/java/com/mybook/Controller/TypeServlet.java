package com.mybook.Controller;

import com.mybook.Service.TypeService;
import com.mybook.pojo.Type;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/type.let")
public class TypeServlet extends HttpServlet {
    TypeService typeService = new TypeService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
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
        ServletContext application = request.getServletContext();

        switch (method){
            case "add":
                add(request,response,out,application);
                break;
            case "remove":
                remove(request,response,out,application);
                break;
            case "modify":
                modify(request,response,out,application);
                break;
            case "modifypre":
                modifyPre(request,response,out,application);
                break;
        }
    }

    private void remove(HttpServletRequest request, HttpServletResponse response, PrintWriter out, ServletContext application) {
        //获取id
        long id = Integer.parseInt(request.getParameter("id"));
        //调用方法删除
        int cnt = typeService.remove(id);
        //判断返回结果，-1不能删，0删除失败，1删除成功
        if (cnt == -1){
            out.println("<script>alert('有子类，不能删除');location.href='type_list.jsp';</script>");
        }else if(cnt == 0){
            out.println("<script>alert('删除失败');location.href='type_list.jsp';</script>");
        }else{
            out.println("<script>alert('删除成功');location.href='type_list.jsp';</script>");
            List<Type> types = typeService.getTypes();
            application.setAttribute("types",types);
        }
    }

    private void modify(HttpServletRequest request, HttpServletResponse response, PrintWriter out, ServletContext application) {
        //获取id，name，parentId
        long typeId = Integer.parseInt(request.getParameter("typeId"));
        String typeName = request.getParameter("typeName");
        long parentType = Integer.parseInt(request.getParameter("parentType"));

        //调用方法修改
        int cnt = typeService.modify(typeId, typeName, parentType);

        if (cnt > 0){
            out.println("<script>alert('修改成功');location.href='type_list.jsp';</script>");
            List<Type> types = typeService.getTypes();
            application.setAttribute("types",types);
        }else {
            out.println("<script>alert('修改失败');location.href='type_add.jsp';</script>");
        }
    }

    private void modifyPre(HttpServletRequest request, HttpServletResponse response, PrintWriter out, ServletContext application) throws ServletException, IOException {
        //获取对象的id
        long id = Integer.parseInt(request.getParameter("id"));
        //获取type对象
        Type type = typeService.getTypeById(id);
        //存到request
        request.setAttribute("type",type);
        //转发
        request.getRequestDispatcher("type_modify.jsp").forward(request,response);
    }

    private void add(HttpServletRequest request, HttpServletResponse response, PrintWriter out, ServletContext application) {
        String typename = request.getParameter("typeName");
        long parentType = Integer.parseInt(request.getParameter("parentType"));

        int cnt = typeService.add(typename, parentType);
        if (cnt > 0){
            out.println("<script>alert('添加成功');location.href='type_list.jsp';</script>");
            List<Type> types = typeService.getTypes();
            application.setAttribute("types",types);
        }else {
            out.println("<script>alert('添加失败');location.href='type_add.jsp';</script>");
        }
    }
}
