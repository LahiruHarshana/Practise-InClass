package ijse.lk.practiseinclass;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import ijse.lk.practiseinclass.db.DBConnection;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class CustomerServlet extends HttpServlet {
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

        Connection connection = null;

        try {
            try {
                connection = DBConnection.getDbConnection().getConnection();
                PreparedStatement stm = connection.prepareStatement("INSERT INTO customer (cusID, cusName, cusAddress,cusSalary) VALUES (?, ?, ?,?)");
                stm.setString(1, id);
                stm.setString(2, name);


                stm.executeUpdate();

                response.getWriter().println("Customer has been saved successfully");

            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

        }catch (Exception e){
            e.printStackTrace();

        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>hello Servlet</h1>");
        out.println("</body></html>");
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        String name = request.getParameter("name");
        String id = request.getParameter("id");
        String address = request.getParameter("address");
        Double salary = Double.parseDouble(request.getParameter("salary"));

        System.out.println("Name: " + name);
        System.out.println("Id: " + id);
        System.out.println("Address: " + address);
        System.out.println("Salary: " + salary);

        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>hello Post</h1>");
        out.println("</body></html>");
    }

}
