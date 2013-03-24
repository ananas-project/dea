package ananas.lib.dea;

public interface DEA {

	String state_ok = "OK";
	String state_too_short = "TOO_SHORT";
	String state_too_long = "TOO_LONG";
	String state_error = "ERROR";

	String getState();

	boolean isValid();

	String getNumberString();

	String toTargetURL();

	class Factory {

		public static DEA parse(String deaString) {
			return DEAParser.parse(deaString);
		}

	}
}
