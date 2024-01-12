package ijse.lk.practiseinclass;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ijse.lk.practiseinclass.db.DBConnection;
import jakarta.json.*;
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

            JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();

            while (rst.next()) {
                writer.println("<p>ID: " + rst.getString(1) + "</p>");
                writer.println("<p>Name: " + rst.getString(2) + "</p>");
                writer.println("<p>Address: " + rst.getString(3) + "</p>");
                writer.println("<p>Salary: " + rst.getDouble(4) + "</p>");

                JsonObjectBuilder builder = Json.createObjectBuilder()
                        .add("id", rst.getString(1))
                        .add("name", rst.getString(2))
                        .add("address", rst.getString(3))
                        .add("salary", rst.getDouble(4));

                jsonArrayBuilder.add(builder.build());
            }

            JsonArray jsonArray = jsonArrayBuilder.build();
            String jsonString = jsonArray.toString();

            response.setContentType("application/json");
            writer.println("<h1>JSON Array: " + jsonString + "</h1>");
            writer.println("</body></html>");

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while processing the request.");
        }
    }



    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("text/html");

        Connection connection = null;

        try {
            try {

                JsonReader reader = Json.createReader(request.getReader());
                JsonObject jsonObject = reader.readObject();
                String id = jsonObject.getString("id");
                String name = jsonObject.getString("name");
                String address = jsonObject.getString("address");
                Double salary = Double.valueOf(jsonObject.getString("salary"));

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
        try {
            JsonReader reader = Json.createReader(req.getReader());
            JsonObject jsonObject = reader.readObject();
            String id = jsonObject.getString("id");
            String name = jsonObject.getString("name");
            String address = jsonObject.getString("address");
            Double salary = Double.valueOf(jsonObject.getString("salary"));

            Connection connection = DBConnection.getDbConnection().getConnection();
            PreparedStatement stm = connection.prepareStatement("UPDATE customer SET cusName=?, cusAddress=?, cusSalary=? WHERE cusID=?");
            stm.setString(1, name);
            stm.setString(2, address);
            stm.setDouble(3, salary);
            stm.setString(4, id);

            stm.executeUpdate();

            resp.getWriter().println("Customer has been updated successfully");

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while processing the request.");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            JsonReader reader = Json.createReader(req.getReader());
            JsonObject jsonObject = reader.readObject();
            String id = jsonObject.getString("id");

            Connection connection = DBConnection.getDbConnection().getConnection();
            PreparedStatement stm = connection.prepareStatement("DELETE FROM customer WHERE cusID=?");
            stm.setString(1, id);

            stm.executeUpdate();

            resp.getWriter().println("Customer has been deleted successfully");

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while processing the request.");
        }
    }

}
