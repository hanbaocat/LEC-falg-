package com.mybook.Listenner;

import com.mybook.Service.TypeService;
import com.mybook.pojo.Type;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.util.List;
@WebListener
public class TypeServletContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        //获取数据库中所有类型信息
        TypeService typeService = new TypeService();
        List<Type> types = typeService.getTypes();
        //获取application对象
        ServletContext application = sce.getServletContext();
        //存进去
        application.setAttribute("types",types);
//        types.forEach(System.out::println);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContextListener.super.contextDestroyed(sce);
    }
}
