package myservlet;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/aaa")
public class JavaServlet extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException 
    {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();
        String name = req.getParameter("uname");
        String email = req.getParameter("Email");

        if (name.equals("Aryan")  && email.equals("aryanmis621@gmail.com"))
        {
        	RequestDispatcher rd=req.getRequestDispatcher("/NewFile1.html");
        	rd.forward(req, res);
            System.out.println("Login Successful :");
            System.out.println("Name :"+name);
            System.out.println("Email :"+email);
        }
        else 
        {
        	out.println("<h1 style='color:red;'>Login Failed!</h1>");

            RequestDispatcher rd=req.getRequestDispatcher("/NewFile.html");
            rd.include(req,res);
            System.out.println("Not Matched");
        }
    }
}
