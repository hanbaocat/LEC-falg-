package com.mybook.Controller;

import com.alibaba.fastjson.JSON;
import com.mybook.Service.BookService;
import com.mybook.pojo.Book;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/book.let")
public class BookServlet extends HttpServlet {
    BookService bookService = new BookService();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }
    /**
     * /book.let?type=add 添加图书
     * /book.let?type=modifypre&id=xx 修改前准备
     * /book.let?type=modify        修改
     * /book.let?type=remove&id=xx    删除
     * /book.let?type=query&pageIndex=1 :分页查询(request:转发)
     * /book.let?type=details&id=xx   展示书籍详细信息
     * /book.let?type=doajax&name=xx  :使用ajax查询图书名对应的图书信息
     *
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out  = response.getWriter();

        HttpSession session = request.getSession();
        if(session.getAttribute("user")==null){
            out.println("<script>alert('请登录');parent.window.location.href='login.html';</script>");
            return;
        }

        String method = request.getParameter("type");
        switch(method){
            case "add":
                try {
                    add(request,response,out);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "modify":
                modify(request,response,out);
                break;
            case "remove":
                remove(request,response,out);
                break;
            case "modifypre":
                modifypre(request,response,out);
                break;
            case "query":
                query(request,response,out);
                break;
            case "details":
                details(request,response,out);
                break;
            case "doajax":
                doajax(request,response,out);
                break;
        }
    }
    private void add(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        long typeId = Integer.parseInt(request.getParameter("typeId"));
        String name = request.getParameter("name");
        double price = Double.parseDouble(request.getParameter("price"));
        String desc = request.getParameter("desc");
        String publish = request.getParameter("publish");
        String author = request.getParameter("author");
        long stock = Integer.parseInt(request.getParameter("stock"));
        String address = request.getParameter("address");
        Book book = new Book(typeId,name,price,desc,publish,author,stock,address,"123465");
        int cnt = bookService.add(book);
        if (cnt > 0){
            out.println("<script>alert('添加成功');location.href='book.let?type=query&pageIndex=1';</script>");
        }else{
            out.println("<script>alert('添加失败');location.href='book.let?type=query&pageIndex=1';</script>");
        }

//        //要实现文件上传 传几把
//        //获取一个磁盘工厂
//        DiskFileItemFactory factory = new DiskFileItemFactory();
//        //设置大小
//        factory.setSizeThreshold(1024*9);
//        //临时仓库
//        File file = new File("D:\\temp");
//        if(!file.exists()){
//            file.mkdir();//创建文件夹
//        }
//        //设置仓库
//        factory.setRepository(file);
//
//        //文件上传+表单数据
//        ServletFileUpload fileUpload = new ServletFileUpload(factory);
//
//        //将请求解析成一个FileItem文件（文件+表单元素）
//        List<FileItem> fileItems =  fileUpload.parseRequest((RequestContext) request);
//
//        //遍历FileItem
//        Book book = new Book();
//        for(FileItem item:fileItems){
//            if(item.isFormField()) {
//                //获取元素名称和用户填写的值
//                String name = item.getFieldName();
//                String value = item.getString("utf-8");//防止乱码
//                switch (name) {
//                    case "typeId":
//                        book.setTypeId(Long.parseLong(value));
//                        break;
//                    case "name":
//                        book.setName(value);
//                        break;
//                    case "price":
//                        book.setPrice(Double.parseDouble(value));
//                        break;
//                    case "desc":
//                        book.setDesc(value);
//                        break;
//                    case "publish":
//                        book.setPublish(value);
//                        break;
//                    case "author":
//                        book.setAuthor(value);
//                        break;
//                    case "stock":
//                        book.setStock(Long.parseLong(value));
//                        break;
//                    case "address":
//                        book.setAddress(value);
//                        break;
//                }
//            }else {
//                //文件：图片的文件名 文城.png
//                String fileName = item.getName();
//                //避免文件替换：当前系统时间
//                //获取后缀名 png
//                String filterName = fileName.substring(fileName.lastIndexOf("."));
//                //修改文件名
//                fileName = DateHelper.getImageName() + filterName;
//                //保存到哪里
//                //虚拟路径：Images/cover/1-1.png
//                //文件的读写：实际路径 D;//xx --> 虚拟路径Images/cover对应的实际路径
//                String path = request.getServletContext().getRealPath("/Images/cover");
//                //d:/xxx/xx/时间.png
//                String filePath = path+'/'+fileName;
//                //数据库表中的路径 Images/cover101-1.png:相对项目的根目录位置
//                String dbPath = "Images/cover/"+fileName;
//                book.setPic(dbPath);
//
//                //保存文件
//                item.write(new File(filePath));
//            }
//        }
//        //t添加book
//        int cnt = bookService.add(book);
//        if (cnt > 0){
//            out.println("<script>alert('添加成功';location.href='book.let?type=query&pageIndex=1')</script>");
//        }else{
//            out.println("<script>alert('添加失败';location.href='book.let?type=query&pageIndex=1')</script>");
//        }
    }

    private void remove(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
        long id = Integer.parseInt(request.getParameter("id"));
        int cnt = bookService.remove(id);
        if (cnt > 0){
            out.println("<script>alert('删除成功');location.href='book.let?type=query&pageIndex=1';</script>");
        }else{
            out.println("<script>alert('删除失败');location.href='book.let?type=query&pageIndex=1';</script>");
        }
    }

    private void modify(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
        long id = Integer.parseInt(request.getParameter("id"));
        long typeId = Integer.parseInt(request.getParameter("typeId"));
        String name = request.getParameter("name");
        double price = Double.parseDouble(request.getParameter("price"));
        String desc = request.getParameter("desc");
        String publish = request.getParameter("publish");
        String author = request.getParameter("author");
        long stock = Integer.parseInt(request.getParameter("stock"));
        String address = request.getParameter("address");

        int cnt = bookService.modify(id, typeId, name, price, desc, "123465", publish, author, stock, address);
        if (cnt > 0){
            out.println("<script>alert('修改成功');location.href='book.let?type=query&pageIndex=1';</script>");
        }else{
            out.println("<script>alert('修改失败');location.href='book.let?type=query&pageIndex=1';</script>");
        }
    }

    private void modifypre(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws ServletException, IOException {
        long id = Integer.parseInt(request.getParameter("id"));
        Book book = bookService.getBookById(id);
        request.setAttribute("book",book);
        request.getRequestDispatcher("book_modify.jsp").forward(request,response);
    }

    private void query(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws ServletException, IOException {
        //页数，页码，信息
        int pageCount = bookService.getPageCount(4);
        int pageIndex = Integer.parseInt(request.getParameter("pageIndex"));
        System.out.println(pageIndex);
        System.out.println(pageCount);
        if (pageIndex <= 1){
            pageIndex = 1;
        }
        if (pageIndex >= pageCount){
            pageIndex = pageCount;
        }
        List<Book> books = bookService.getByPage(pageIndex, 4);
        request.setAttribute("books",books);
        request.setAttribute("pageCount",pageCount);
        request.getRequestDispatcher("book_list.jsp?pageIndex="+pageIndex).forward(request,response);
    }

    private void details(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws ServletException, IOException {
        long id = Integer.parseInt(request.getParameter("id"));
        Book book = bookService.getBookById(id);
        request.setAttribute("book",book);
        request.getRequestDispatcher("book_details.jsp").forward(request,response);
    }

    private void doajax(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
        String name = request.getParameter("name");
        Book book = bookService.getBookByName(name);
        if(book==null){
            out.print("{}");
        }else{
            String bookJson = JSON.toJSONString(book);
            out.print(bookJson);
        }
    }
}
