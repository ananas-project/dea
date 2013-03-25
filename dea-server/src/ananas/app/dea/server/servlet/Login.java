package ananas.app.dea.server.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ananas.lib.util.logging.AbstractLoggerFactory;
import ananas.lib.util.logging.Logger;

/**
 * Servlet implementation class Login
 */
public class Login extends HttpServlet {

	private static final long serialVersionUID = 821690116665861264L;

	private static final Logger logger = (new AbstractLoggerFactory() {
	}).getLogger();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Login() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		this.doProcSwitch(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		this.doProcSwitch(request, response);
	}

	private void doProcSwitch(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		SessionAuthBinding auth = null;
		HttpSession session = request.getSession();
		if (session != null) {
			auth = (SessionAuthBinding) session
					.getAttribute(SessionAuthBinding.session_attr_key);
		}
		if (auth == null) {
			auth = new SessionAuthBinding(session.getId());
			logger.info("new " + auth);
			if (auth.doProc(request, response)) {
				session.setAttribute(SessionAuthBinding.session_attr_key, auth);
			}
		} else {
			if (session.getId().equals(auth.getId())) {
				auth.doProc(request, response);
			} else {
				session.removeAttribute(SessionAuthBinding.session_attr_key);
			}
		}
	}

}
