package sda;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sda.ifx.parseLog;
import sda.nomn.Lanciatore;

/**
 * Servlet implementation class theParser
 */
@WebServlet("/theParser")
public class theParser extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public theParser() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		  String rsp=null;
		try {
			rsp = new parseLog().parseIFXLog();
			response.getWriter().append("Risposta del parser INFORMIX: "+rsp);
			new Lanciatore().main(null);
			response.getWriter().append("/n Completato PArsin NMON: "+rsp);
		} catch (Exception e) {
			response.getWriter().append("Parsing in errore");
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
