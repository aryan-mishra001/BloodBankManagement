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

@WebServlet("/PatientServlet")
public class PatientServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    // Database credentials
    private static final String url = "jdbc:mysql://localhost:3306/blood";
    private static final String uname = "aryan";         
    private static final String pass = "aryan@123";   
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        res.setContentType("text/html");
        PrintWriter out = res.getWriter();

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            // Load JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Get form parameters
            String pid = req.getParameter("pid");
            String bbid = req.getParameter("bbid");
            String pname = req.getParameter("pname");
            String pbg = req.getParameter("pbg");
            String pgen = req.getParameter("pgen");

            // Age: safe parsing
            int page = 0;
            try {
                page = Integer.parseInt(req.getParameter("page"));
            } catch (NumberFormatException e) {
                out.println("<h3>⚠ Invalid Age! Please enter numeric value.</h3>");
                return;
            }

            // Contact: safe parsing
            String pcon = req.getParameter("pcon");

            // SQL query
            String sql = "INSERT INTO patient(pid, bbid, pname, pbg, pgen, page, pcon) VALUES (?, ?, ?, ?, ?, ?, ?)";

            // Database connection
            conn = DriverManager.getConnection(url, uname, pass);
            ps = conn.prepareStatement(sql);

            // Bind values
            ps.setString(1, pid);
            ps.setString(2, bbid);
            ps.setString(3, pname);
            ps.setString(4, pbg);
            ps.setString(5, pgen);
            ps.setInt(6, page);
            ps.setString(7, pcon);

            int count = ps.executeUpdate();

            // Forward back to patient.html
            RequestDispatcher rd = req.getRequestDispatcher("/patient.html");
            if (count > 0) {
                rd.include(req, res);
                out.println("<h3 style='color:lime;'>✅ Patient Registered Successfully!</h3>");
            } else {
                rd.include(req, res);
                out.println("<h3 style='color:red;'>❌ Failed to Register Patient!</h3>");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            out.println("<h3 style='color:red;'>⚠ SQL Error: " + e.getMessage() + "</h3>");
        } catch (ClassNotFoundException e) {
            out.println("<h3 style='color:red;'>⚠ JDBC Driver not found. Please add MySQL connector JAR in Tomcat lib.</h3>");
        } finally {
            try {
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
