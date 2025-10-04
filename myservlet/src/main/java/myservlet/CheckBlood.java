package myservlet;

import java.io.PrintWriter;
import java.sql.SQLException;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/bloodMatch")  // URL mapping same रहेगी
public class CheckBlood extends HttpServlet
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
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            // Load MySQL Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Get Patient ID from form
            String pid = req.getParameter("pid");

            // SQL Query
            String sql = "SELECT b.bname FROM bloodbank b " +
                         "INNER JOIN donor d ON b.bbid = d.bbid " +
                         "INNER JOIN patient p ON p.bbid = d.bbid " +
                         "WHERE p.pbg = d.dbg AND p.pid = ?";

            // Connection
            conn = DriverManager.getConnection(url, uname, pass);
            ps = conn.prepareStatement(sql);
            ps.setString(1, pid);
            rs = ps.executeQuery();

            // Include back to ChecksBlood.html
            RequestDispatcher rd = req.getRequestDispatcher("/ChecksBlood.html");
            rd.include(req, res);

            boolean found = false;
            out.println("<div style='margin-top:20px; text-align:center;'>");
            while (rs.next()) {
                found = true;
                out.println("<h3>✅ Blood Matched At -> " + rs.getString("bname") + "</h3>");
            }
            if (!found) {
                out.println("<h3>❌ No record found for Patient ID: " + pid + "</h3>");
            }
            out.println("</div>");

        } catch (SQLException e) {
            out.println("<h3>⚠ SQL Error: " + e.getMessage() + "</h3>");
        } catch (ClassNotFoundException e) {
            out.println("<h3>⚠ JDBC Driver not found. Please add MySQL connector JAR.</h3>");
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
