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

@WebServlet("/javaServlet")
public class javaServlet extends HttpServlet
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

        try
        {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Get form parameters
            String did = req.getParameter("did");
            String bbid = req.getParameter("bbid");
            String name = req.getParameter("dname");
            String bloodGroup = req.getParameter("dbg");
            char gender = req.getParameter("dgen").charAt(0);

            // Contact number: parse safely into long
            long contact = 0;
            try {
                contact = Long.parseLong(req.getParameter("dcon"));
            } catch (NumberFormatException e) {
                out.println("<h3>⚠ Invalid Contact Number! Please enter digits only.</h3>");
                return; // Stop execution if invalid input
            }

            // SQL query
            String qry = "INSERT INTO donor(did, bbid, dname, dbg, dgen, dcon) VALUES(?, ?, ?, ?, ?, ?)";

            // Connect to database
            conn = DriverManager.getConnection(url, uname, pass);
            pmt = conn.prepareStatement(qry);

            // Set values in PreparedStatement
            pmt.setString(1, did);
            pmt.setString(2, bbid);
            pmt.setString(3, name);
            pmt.setString(4, bloodGroup);
            pmt.setString(5, String.valueOf(gender));
            pmt.setLong(6, contact);

            int count = pmt.executeUpdate();

            // Forward to register.html
            RequestDispatcher rd = req.getRequestDispatcher("/register.html");
            if (count > 0)
            {
                rd.include(req, res);
                out.println("<h3>✅ Data has been Inserted Successfully</h3>");
            } 
            else
            {
                out.println("<h1>❌ Failed to Insert Donor Data</h1>");
            }
            
        } 
        catch (SQLException e)
        {
            e.printStackTrace();
            out.println("<h1>⚠ SQL Error: " + e.getMessage() + "</h1>");
        }
        catch (ClassNotFoundException e)
        {
            out.println("<h1>⚠ JDBC Driver not found. Please add MySQL connector JAR in Tomcat lib.</h1>");
        }
        finally
        {
            try
            {
                if (pmt != null) pmt.close();
                if (conn != null) conn.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
