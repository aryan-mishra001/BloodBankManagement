package myservlet;

import java.io.PrintWriter;
import java.sql.SQLException;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/Udeletion")
public class Udeletion extends HttpServlet
{
    private static final long serialVersionUID = 1L;
    private static final String url = "jdbc:mysql://localhost:3306/blood";
    private static final String uname = "aryan";
    private static final String pass = "aryan@123";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException
    {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();

        Connection conn = null;
        PreparedStatement pmt = null;

        try {
            // Load Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Get donor id and blood bank id
            String did = req.getParameter("did");
            String bbid = req.getParameter("bbid");

            // SQL query
            String qry = "DELETE FROM donor WHERE did = ? AND bbid = ?";

            // Connection
            conn = DriverManager.getConnection(url, uname, pass);
            pmt = conn.prepareStatement(qry);
            pmt.setString(1, did);
            pmt.setString(2, bbid);

            int count = pmt.executeUpdate();

            // Forward to delete.html
            RequestDispatcher rd = req.getRequestDispatcher("/Udeletion.html");
            rd.include(req, res);

            if (count > 0) {
                out.println("<h3>✅ Donor Deleted Successfully!</h3>");
            } else {
                out.println("<h3>❌ No matching record found for Donor ID: " + did + " and Blood Bank ID: " + bbid + "</h3>");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            out.println("<h1>⚠ SQL Error: " + e.getMessage() + "</h1>");
        } catch (ClassNotFoundException e) {
            out.println("<h1>⚠ JDBC Driver not found. Please add MySQL connector JAR in Tomcat lib.</h1>");
        } finally {
            try {
                if (pmt != null) pmt.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
