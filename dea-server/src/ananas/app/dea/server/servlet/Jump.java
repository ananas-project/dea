package ananas.app.dea.server.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Jump
 */
public class Jump extends HttpServlet {

	private static final long serialVersionUID = -8062453349436412736L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Jump() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String query = request.getQueryString();
		String key = "dea";
		String value = request.getParameter(key);

		ServletOutputStream out = response.getOutputStream();

		if (value == null) {
			response.setContentType("text/plain");

			String str = "request with query string like this : ?" + key
					+ "=1234567890.dea1234.com";
			out.println("current query is : " + query);
			out.println(str);
			return;
		}

		String targetURL = "http://open.weibo.com";

		response.setStatus(HttpServletResponse.SC_TEMPORARY_REDIRECT);
		response.setHeader("Location", targetURL);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
