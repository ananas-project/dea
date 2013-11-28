package ananas.dea.lite.server.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * Servlet implementation class Proc
 */
@WebServlet("/Proc")
public class Proc extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Proc() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		InputStream in = request.getInputStream();
		Reader rdr = new InputStreamReader(in, Charset.forName("UTF-8"));
		String txt = this.__read_string(rdr);
		System.out.println("req-txt:" + txt);

		ServletOutputStream out = response.getOutputStream();

		JSONObject json = new JSONObject();
		json.put("commit", "hello, dea!");
		json.put("request-text", txt);
		byte[] ba = JSON.toJSONBytes(json);
		out.write(ba);

	}

	private String __read_string(Reader rdr) throws IOException {

		StringBuilder sb = new StringBuilder();
		char[] buf = new char[1024];
		for (;;) {
			int cc = rdr.read(buf);
			if (cc < 0)
				break;
			sb.append(buf, 0, cc);
		}
		return sb.toString();
	}

}
