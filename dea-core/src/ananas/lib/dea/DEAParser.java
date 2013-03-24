package ananas.lib.dea;

class DEAParser {

	static DEA parse(String s) {
		int part;
		char part0ch;
		String part0, part1, part2, partTail;
		part = 0;
		part0ch = 0;
		part0 = part1 = part2 = partTail = null;
		final int len = s.length();
		final StringBuilder sb = new StringBuilder();
		for (int i = 0; i < len; i++) {
			final char ch = s.charAt(i);
			if (ch < '0' || '9' < ch) {
				continue;
			}
			switch (part) {
			case 0:
				part0ch = ch;
				part++;
				break;
			case 1:
				sb.append(ch);
				if (isFull(sb, 1)) {
					part1 = sb.toString();
					sb.setLength(0);
					part++;
				}
				break;
			case 2:
				sb.append(ch);
				if (isFull(sb, 6)) {
					part2 = sb.toString();
					sb.setLength(0);
					part++;
				}
				break;
			default:
				sb.append(ch);
				break;
			}
		}
		if (sb.length() > 0) {
			partTail = sb.toString();
		}

		int ip0 = (part0ch - '0');
		if (0 < ip0 && ip0 < part0_values.length) {
			part0 = part0_values[ip0];
		}

		String state;
		if (part0 == null) {
			state = DEA.state_error;
		} else if (part1 == null) {
			state = DEA.state_too_short;
		} else if (part2 == null) {
			state = DEA.state_too_short;
		} else if (partTail != null) {
			state = DEA.state_too_long;
		} else {
			state = DEA.state_ok;
		}

		return new MyImpl(part0ch, part0, part1, part2, partTail, state);
	}

	private static final String[] part0_values = { null, "org", "com", "net",
			"edu" };

	private static boolean isFull(final StringBuilder sb, final int minLength) {
		final int maxLength = 32;
		final int strlen = sb.length();
		if (strlen < minLength || strlen > maxLength) {
			return false;
		}
		final String str = sb.toString();
		for (int i = 2; i >= 1; i--) {
			String prefix = (strlen - i) + "";
			if (str.startsWith(prefix)) {
				if (i == prefix.length()) {
					return true;
				}
			}
		}
		return false;
	}

	private final static class MyImpl implements DEA {

		private final char mPart0ch;
		private final String mPart0;
		private final String mPart1;
		private final String mPart2;
		private final String mPartT;
		private final String mState;

		public MyImpl(char part0ch, String part0, String part1, String part2,
				String partT, String state) {

			this.mPart0ch = part0ch;
			this.mPart0 = antiNull(part0);
			this.mPart1 = antiNull(part1);
			this.mPart2 = antiNull(part2);
			this.mPartT = antiNull(partT);
			this.mState = antiNull(state);
		}

		private String antiNull(String s) {
			return ((s == null) ? "" : s);
		}

		@Override
		public String getState() {
			return this.mState;
		}

		@Override
		public boolean isValid() {
			return (this.mState == DEA.state_ok);
		}

		@Override
		public String getNumberString() {
			return (mPart0ch + mPart1 + mPart2 + mPartT);
		}

		@Override
		public String toTargetURL() {
			String full = (this.mPart0ch + this.mPart1 + this.mPart2);
			return ("http://" + full + ".dea" + this.mPart0ch + this.mPart1
					+ "." + this.mPart0 + "/");
		}

		public String toString() {
			return this.getNumberString();
		}
	}

}
