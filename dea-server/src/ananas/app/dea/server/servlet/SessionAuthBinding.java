package ananas.app.dea.server.servlet;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openid4java.consumer.ConsumerManager;
import org.openid4java.discovery.DiscoveryException;
import org.openid4java.discovery.DiscoveryInformation;
import org.openid4java.message.AuthRequest;
import org.openid4java.message.sreg.SRegRequest;

import ananas.lib.util.logging.AbstractLoggerFactory;
import ananas.lib.util.logging.Logger;

public class SessionAuthBinding {

	// const
	public static final String session_attr_key = SessionAuthBinding.class
			.getName();

	public static final String is_return_key = "is_return";

	private static final Logger logger = (new AbstractLoggerFactory() {
	}).getLogger();

	// var
	private boolean mIsOnline;

	private DiscoveryInformation mDiscInfo;

	private String mReturnToURL;

	private final String mId;

	private String mOpenID;

	public SessionAuthBinding(String id) {
		this.mId = id;
	}

	public boolean isOnline() {
		return this.mIsOnline;
	}

	public boolean doProc(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		String is_return = request.getParameter(is_return_key);
		is_return = (is_return == null) ? "false" : is_return;

		boolean ret;
		this._printMyself();
		if (is_return.equals("true")) {
			ret = this._doProcReturn(request, response);
		} else {
			ret = this._doProcInvoke(request, response);
		}
		this._printMyself();
		return ret;
	}

	private boolean _doProcReturn(HttpServletRequest request,
			HttpServletResponse response) {

		DiscoveryInformation discInfo = this.mDiscInfo;
		String return_to = this.mReturnToURL;
		this.mDiscInfo = null;
		this.mReturnToURL = null;

		ParameterMap param = new ParameterMap(request);
		boolean rlt = this._checkResult(discInfo, param, return_to);
		this.mIsOnline = rlt;

		response.setStatus(HttpServletResponse.SC_TEMPORARY_REDIRECT);
		if (rlt) {
			response.setHeader("Location", "online.html");
		} else {
			response.setHeader("Location", "log-error.html");
		}
		return rlt;
	}

	static class ParameterMap {

		private final HttpServletRequest mRequest;

		public ParameterMap(HttpServletRequest request) {
			this.mRequest = request;
		}

		public Enumeration keys() {
			return this.mRequest.getParameterNames();
		}

		public String get(String key) {
			return this.mRequest.getParameter(key);
		}

	}

	private boolean _checkResult(DiscoveryInformation info, ParameterMap map,
			String return_to) {

		Map<String, String> map2 = new HashMap<String, String>();

		map2.put("openid.op_endpoint", info.getOPEndpoint().toString());
		// map2.put("openid.identity", info.getDelegateIdentifier());
		map2.put("openid.return_to", return_to);
		// map2.put("openid.claimed_id", info.getClaimedIdentifier()
		// .getIdentifier());

		for (String key : map2.keySet()) {
			String v1 = map.get(key);
			String v2 = map2.get(key);
			logger.trace("    v1:" + key + " = " + v1);
			logger.trace("    v2:" + key + " = " + v2);
			if (v1 == null) {
				return false;
			}
			if (!v1.equals(v2)) {
				return false;
			}
		}

		Enumeration keys = map.keys();
		for (; keys.hasMoreElements();) {
			String key = keys.nextElement().toString();
			String value = map.get(key);
			logger.trace("    param:" + key + " = " + value);
		}

		String mode = map.get("openid.mode");
		if (!"id_res".equals(mode)) {
			return false;
		}

		String openid = map.get("openid.identity");
		this.mOpenID = openid;
		return true;
	}

	private boolean _doProcInvoke(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		// this.mIsOnline = false;
		String openid = request.getHeader("openid");

		DiscoveryInformation discInfo = this
				.performDiscoveryOnUserSuppliedIdentifier(openid);

		String return_to = request.getRequestURL().toString();
		return_to = this.genReturnToURL(return_to);
		this.mReturnToURL = return_to;
		AuthRequest authRequest = this.createOpenIdAuthRequest(discInfo,
				return_to);

		String destURL = authRequest.getDestinationUrl(true);
		response.setStatus(HttpServletResponse.SC_OK);
		response.setHeader("Location", destURL);
		response.setHeader("Status-Code",
				HttpServletResponse.SC_TEMPORARY_REDIRECT + "");
		response.getOutputStream().println("goto " + destURL);

		this.mDiscInfo = discInfo;

		return true;
	}

	private void _printMyself() {
		DiscoveryInformation info = this.mDiscInfo;
		StringBuilder sb = new StringBuilder();

		sb.append("[" + this.getClass().getName());
		sb.append(" session_id=" + this.mId);
		sb.append(" openid=" + this.mOpenID);
		sb.append(" is_online=" + this.mIsOnline);
		sb.append(" return_to=" + this.mReturnToURL);
		sb.append(" disc_info=" + (info == null ? null : info.getClass()) + "]");

		logger.info(sb.toString());

	}

	private String genReturnToURL(String url) {
		int hash = (this.mId + "" + System.currentTimeMillis()).hashCode();
		return url + "?is_return=true&hash=" + hash;
	}

	public DiscoveryInformation performDiscoveryOnUserSuppliedIdentifier(
			String openid) {

		DiscoveryInformation ret = null;
		ConsumerManager consumerManager = this.getConsumerManager();
		try {
			// Perform discover on the User-Supplied Identifier
			@SuppressWarnings("unchecked")
			List<DiscoveryInformation> discoveries = consumerManager
					.discover(openid);
			// Pass the discoveries to the associate() method...
			ret = consumerManager.associate(discoveries);
		} catch (DiscoveryException e) {
			String message = "Error occurred during discovery!";
			logger.error(message, e);
			throw new RuntimeException(message, e);
		}
		return ret;
	}

	private static ConsumerManager s_ConsumerManager = null;

	private ConsumerManager getConsumerManager() {
		ConsumerManager cm = s_ConsumerManager;
		if (cm == null) {
			cm = new ConsumerManager();
			s_ConsumerManager = cm;
		}
		return cm;
	}

	public AuthRequest createOpenIdAuthRequest(
			DiscoveryInformation discoveryInformation, String returnToUrl) {

		AuthRequest ret = null;
		//
		try {
			// Create the AuthRequest object
			ret = this.getConsumerManager().authenticate(discoveryInformation,
					returnToUrl);
			// Create the Simple Registration Request
			SRegRequest sRegRequest = SRegRequest.createFetchRequest();
			sRegRequest.addAttribute("email", false);
			sRegRequest.addAttribute("fullname", false);
			sRegRequest.addAttribute("dob", false);
			sRegRequest.addAttribute("postcode", false);
			ret.addExtension(sRegRequest);
		} catch (Exception e) {
			String message = "Exception occurred while building "
					+ "AuthRequest object!";
			logger.error(message, e);
			throw new RuntimeException(message, e);
		}
		return ret;
	}

	public String getId() {
		return this.mId;
	}

}
