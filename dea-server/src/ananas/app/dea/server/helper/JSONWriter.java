package ananas.app.dea.server.helper;

import java.io.IOException;
import java.io.OutputStream;

public class JSONWriter {

	private final OutputStream out;
	private boolean mIsFirstObjectItem;
	private boolean mIsFirstArrayItem;

	public JSONWriter(OutputStream out) {
		this.out = out;
	}

	public void objectBegin() throws IOException {
		out.write('{');
		this.mIsFirstObjectItem = true;
	}

	public void objectEnd() throws IOException {
		out.write('}');
		this.mIsFirstObjectItem = false;
	}

	public JSONWriter objectItem(String key) throws IOException {
		if (this.mIsFirstObjectItem) {
			this.mIsFirstObjectItem = false;
		} else {
			out.write(',');
		}
		this.string(key);
		out.write(':');
		return this;
	}

	public void string(String string) throws IOException {
		if (string == null) {
			out.write("null".getBytes("UTF-8"));
		} else {
			this.stringBegin();
			this.stringBody(string);
			this.stringEnd();
		}
	}

	public void stringBegin() throws IOException {
		out.write('"');
	}

	public void stringBody(String string) throws IOException {
		out.write(string.getBytes("UTF-8"));
	}

	public void stringEnd() throws IOException {
		out.write('"');
	}

}
