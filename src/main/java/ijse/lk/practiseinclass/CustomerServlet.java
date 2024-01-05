package ijse.lk.practiseinclass;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ijse.lk.practiseinclass.db.DBConnection;
import jakarta.json.JsonObjectBuilder;
import jakarta.servlet.ServletException;
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

            PrintWriter writer = response.getWriter();

            writer.println("<html><body>");

            String jsonArr = "";

            while (rst.next()) {
                writer.println("<p>ID: " + rst.getString(1) + "</p>");
                writer.println("<p>Name: " + rst.getString(2) + "</p>");
                writer.println("<p>Address: " + rst.getString(3) + "</p>");
                writer.println("<p>Salary: " + rst.getDouble(4) + "</p>");

                JsonObjectBuilder builder = jakarta.json.Json.createObjectBuilder();
                builder.add("id", rst.getString(1));
                builder.add("name", rst.getString(2));
                builder.add("address", rst.getString(3));
                builder.add("salary", rst.getDouble(4));
                jsonArr += jsonObject + ",";

            }
            jsonArr="["+jsonArr.substring(0,jsonArr.length()-1)+"]";
            System.out.printf(jsonArr);
            response.setContentType("application/json");
            writer.println("<h1>JSON Array: " + jsonArr + "</h1>");
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

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getParameter("id");
        req.getParameter("name");
        req.getParameter("address");
    }
}
