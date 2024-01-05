package ijse.lk.practiseinclass.practiseinclass;

import java.io.*;

import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {
    private String message;

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String name = request.getParameter("name");
        String id = request.getParameter("id");
        String address = request.getParameter("address");
        Double salary = Double.parseDouble(request.getParameter("salary"));

        System.out.println("Name: " + name);
        System.out.println("Id: " + id);
        System.out.println("Address: " + address);
        System.out.println("Salary: " + salary);

        response.setContentType("text/html");

        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>hello Servlet</h1>");
        out.println("</body></html>");
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>hello Post</h1>");
        out.println("</body></html>");
    }



}
