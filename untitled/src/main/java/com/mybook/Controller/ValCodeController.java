package com.mybook.Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

@WebServlet(urlPatterns = "/code.let",loadOnStartup = 1)
//生成验证码
public class ValCodeController extends HttpServlet {
    Random random = new Random();

    //生成随机字符串
    private String getRandomStr(){
        String str ="123456789qwertyuiopasdfghjklzxcvbnmQWERTYUUUUUUUUIIOPASDFGHJKLZXCVBNM";
        StringBuilder sb = new StringBuilder();
        for(int i = 0;i < 4;i++) {
            int index = random.nextInt(str.length());
            char x = str.charAt(index);
            sb.append(x);
        }
        return sb.toString();
    }
    //生成背景颜色
    private Color getBGColor(){
        int red = random.nextInt(256);
        int green = random.nextInt(256);
        int blue = random.nextInt(256);;

        return new Color(red,green,blue);
    }
    //生成字体颜色
    private Color getCharColor(Color bgColor){
        int red = 256 - bgColor.getRed();
        int green = 256 - bgColor.getGreen();
        int blue = 256 - bgColor.getBlue();

        return new Color(red,green,blue);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //设置响应格式。jpg
        response.setContentType("image/jpeg");
        //图片对象,宽度八十，高度30，使用三原色绘画
        BufferedImage bufferedImage = new BufferedImage(80,30,BufferedImage.TYPE_INT_RGB);
        //获取画布对象
        Graphics g = bufferedImage.getGraphics();
        //设置背景颜色
        Color bgColor = this.getBGColor();
        g.setColor(bgColor);
        //画背景
        g.fillRect(0,0,80,30);
        //设置前景颜色
        g.setColor(this.getCharColor(bgColor));
        //设置字体
        g.setFont(new Font("黑体",Font.BOLD,26));
        //随机字符串存到session
        String str = this.getRandomStr();
        HttpSession session = request.getSession();
        session.setAttribute("code",str.toLowerCase());
        //画前景
        g.drawString(str,10,28);
        //画噪点
        for(int i = 0;i < 30;i++){
            int x = random.nextInt(80);
            int y = random.nextInt(30);
            g.fillRect(x,y,1,1);
        }
        //吧图片对象写进流里面
        ServletOutputStream sos = response.getOutputStream();
        ImageIO.write(bufferedImage,"jpeg",sos);
    }
}
