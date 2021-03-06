package top.lmoon.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpClient;
import org.springframework.stereotype.Component;

import com.alibaba.druid.util.HttpClientUtils;

import top.lmoon.util.HttpUtil;

/**
 * The MainServlet returns the to-do list html on GET requests and handles the
 * creation of new to-do list entries on POST requests.
 */
@Component
@WebServlet(name = "testsl", urlPatterns = "/testsl")
public class TestServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html; charset=UTF-8");
        String string = HttpUtil.get("http://www.runoob.com/redis/lists-ltrim.html");
        PrintWriter writer = resp.getWriter();
        writer.write(string);
        writer.flush();
        writer.close();
       
        
    }

    private String escapeHtml(String text) {
        return text.replace("<", "&lt;");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String summary = req.getParameter("summary");
        String description = req.getParameter("description");

        resp.sendRedirect("index.html");
    }
}
