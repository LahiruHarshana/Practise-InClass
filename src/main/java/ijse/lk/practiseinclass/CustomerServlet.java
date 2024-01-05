package ijse.lk.practiseinclass;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ijse.lk.practiseinclass.db.DBConnection;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "CustomerServlet", value = "/customer")
public class CustomerServlet extends HttpServlet {
    private String message;
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        try {
            Connection connection = DBConnection.getDbConnection().getConnection();
            PreparedStatement pstm = connection.prepareStatement("select * from customer");
            ResultSet rst = pstm.executeQuery();

            response.setContentType("text/html;charset=UTF-8");
            PrintWriter writer = response.getWriter();

            writer.println("<html><body>");

            String jsonArr = "";

            while (rst.next()) {
                writer.println("<p>ID: " + rst.getString(1) + "</p>");
                writer.println("<p>Name: " + rst.getString(2) + "</p>");
                writer.println("<p>Address: " + rst.getString(3) + "</p>");
                writer.println("<p>Salary: " + rst.getDouble(4) + "</p>");

                String jsonObject = "{\"id\":\"" + rst.getString(1) + "\",\"name\":\"" + rst.getString(2) + "\",\"address\":\"" + rst.getString(3) + "\",\"salary\":\"" + rst.getDouble(4) + "\"}";
                jsonArr += jsonObject + ",";

            }
            writer.println("</body></html>");

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while processing the request.");
        }
    }


    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
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
                stm.setString(3, address);
                stm.setDouble(4, salary);

                stm.executeUpdate();

                response.getWriter().println("Customer has been saved successfully");

            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

        } catch (Exception e) {
            e.printStackTrace();

            PrintWriter out = response.getWriter();
            out.println("<html><body>");
            out.println("<h1>hello Servlet</h1>");
            out.println("</body></html>");
        }
    }

}
