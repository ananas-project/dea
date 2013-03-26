package ananas.app.dea.server.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.classic.Session;

import ananas.app.dea.server.helper.JSONWriter;
import ananas.app.dea.server.helper.SessionAuthBinding;
import ananas.app.dea.server.orm.TOpenidSha1;

/**
 * Servlet implementation class Account
 */
public class Account extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Account() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		this._doProc(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		this._doProc(request, response);
	}

	private void _doProc(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		this._tryDB();

		String openid = null;

		HttpSession session = request.getSession();
		if (session != null) {
			SessionAuthBinding bind = (SessionAuthBinding) session
					.getAttribute(SessionAuthBinding.session_attr_key);
			if (bind != null)
				if (bind.isOnline()) {
					openid = bind.getOpenID();
				}
		}

		ServletOutputStream out = response.getOutputStream();

		JSONWriter wtr = new JSONWriter(out);
		wtr.objectBegin();
		// wtr.objectItem("abc").string("123");
		// wtr.objectItem("defg").string("4567");
		wtr.objectItem("openid").string(openid);
		wtr.objectEnd();
	}

	private void _tryDB() {

		Configuration conf = (new Configuration()).configure();
		SessionFactory sf = conf.buildSessionFactory();
		Session session = sf.openSession();

		TOpenidSha1 obj = new TOpenidSha1();
		obj.setOpenid("http://abc.com/xk");
		obj.setDea(723824);

		Transaction trans = session.beginTransaction();
		session.save(obj);
		trans.commit();

	}
}
