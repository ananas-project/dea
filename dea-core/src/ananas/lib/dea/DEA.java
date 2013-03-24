package ananas.lib.dea;

public interface DEA {

	int state_ok = 0;
	int state_too_short = 1;
	int state_too_long = 2;

	int getState();

	boolean isValid();

	String getNumberString();

	class Factory {

		public static DEA parse(String deaString) {
			return DEAParser.parse(deaString);
		}

	}
}
