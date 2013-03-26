package ananas.app.dea.server.orm;

import ananas.lib.util.SHA1Value;

public class TOpenidSha1 {

	private SHA1Value sha1;
	private String openid;
	private long dea;

	private static final SHA1Value default_sha1 = new SHA1Value();

	public TOpenidSha1() {
		this.sha1 = default_sha1;
	}

	public byte[] getSha1() {
		return sha1.getBytes();
	}

	public void setSha1(byte[] sha1) {
		this.sha1 = new SHA1Value(sha1);
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public long getDea() {
		return dea;
	}

	public void setDea(long dea) {
		this.dea = dea;
	}

}
