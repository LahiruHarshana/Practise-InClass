package ijse.lk.practiseinclass;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import ijse.lk.practiseinclass.db.DBConnection;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Customer;

@WebServlet(name = "CustomerServlet", value = "/customer")
public class CustomerServlet extends HttpServlet {
    private String message;
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

            response.setContentType("application/json");

            Connection connection = null;

            try {
                try {
                    connection = DBConnection.getDbConnection().getConnection();
                    PreparedStatement stm = connection.prepareStatement("SELECT * FROM customer");
                    PrintWriter writer = response.getWriter();
                    ResultSet rst = stm.executeQuery();

                    ArrayList<String> allCustomer = new ArrayList<>();

                    while (rst.next()){
                        allCustomer.add(JsonbBuilder.create().toJson(rst));
                    }
                    writer.print(allCustomer);
                    System.out.println(allCustomer);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }

            } catch (Exception e) {
            }

    }




    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("text/html");

        Connection connection = null;

        try {
            try {

                Jsonb jsonb = JsonbBuilder.create();
                Customer customer = jsonb.fromJson(request.getReader(), Customer.class);

                if (customer.getCusId() == null || customer.getCusId().matches("C\\d{3}") == false) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }
                if (customer.getCusName() == null || customer.getCusName().trim().isEmpty()) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }

                if (customer.getCusAddress() == null || customer.getCusAddress().trim().isEmpty()) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }

                if (customer.getCusSalary() == 0) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }

                connection = DBConnection.getDbConnection().getConnection();
                PreparedStatement stm = connection.prepareStatement("INSERT INTO customer VALUES (?,?,?,?)");
                stm.setObject(1, customer.getCusId());
                stm.setObject(2, customer.getCusName());
                stm.setObject(3, customer.getCusAddress());
                stm.setObject(4, customer.getCusSalary());

                if (stm.executeUpdate() > 0) {
                    response.setStatus(HttpServletResponse.SC_CREATED);
                } else {
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

        } catch (Exception e) {
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Connection connection = null;

        try {
            Jsonb jsonb = JsonbBuilder.create();
            Customer customer = jsonb.fromJson(req.getReader(), Customer.class);

            connection = DBConnection.getDbConnection().getConnection();

            if (customer.getCusId() == null || customer.getCusId().matches("C\\d{3}") == false) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            if (customer.getCusName() == null || customer.getCusName().trim().isEmpty()) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            if (customer.getCusAddress() == null || customer.getCusAddress().trim().isEmpty()) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            if (customer.getCusSalary() == 0) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            PreparedStatement stm = connection.prepareStatement("UPDATE customer SET cusName=?, cusAddress=?, cusSalary=? WHERE cusID=?");
            stm.setObject(1, customer.getCusName());
            stm.setObject(2, customer.getCusAddress());
            stm.setObject(3, customer.getCusSalary());
            stm.setObject(4, customer.getCusId());
            if (stm.executeUpdate() > 0) {
                resp.setStatus(HttpServletResponse.SC_CREATED);
            } else {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
            resp.getWriter().println("Customer has been updated successfully");

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while processing the request.");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Connection connection = null;

        try {
            Jsonb jsonb = JsonbBuilder.create();
            Customer customer = jsonb.fromJson(req.getReader(), Customer.class);

            if (customer.getCusId() == null || customer.getCusId().matches("C\\d{3}") == false) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            connection = DBConnection.getDbConnection().getConnection();
            PreparedStatement stm = connection.prepareStatement("DELETE FROM customer WHERE cusID=?");
            stm.setObject(1, customer.getCusId());

            if (stm.executeUpdate() > 0) {
                resp.setStatus(HttpServletResponse.SC_CREATED);
            } else {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
            resp.getWriter().println("Customer has been deleted successfully");

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while processing the request.");
        }
    }
}
