package sswapServiceMediator;

import java.io.IOException;
import sswapServiceMediator.CompareString;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class AlignmentServlet
 */
@WebServlet("/AlignmentServlet")
public class AlignmentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AlignmentServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.getWriter().append("Welcome");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String uri1=request.getParameter("uri1");
		String uri2=request.getParameter("uri2");
		
		PrintWriter writer= response.getWriter();
		List results=CompareString.ngramCalculate(uri1, uri2);
		 for(int i=0; i<results.size();i++)
		 {
			 writer.println((i+1)+". "+results.get(i));
		 } 
	}

}
