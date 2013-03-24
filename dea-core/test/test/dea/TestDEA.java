package test.dea;

import ananas.lib.dea.DEA;

public class TestDEA {

	public static void main(String[] args) {

		String[] list = { "410144", "dea:12345678", "723824", "130-9773-0172",
				"3-0-5.12345", "dea:1133709" };

		for (String str : list) {
			DEA dea = DEA.Factory.parse(str);
			System.out.println("input:" + str);
			System.out.println("      numStr:" + dea.getNumberString());
			System.out.println("     isValid:" + dea.isValid());
			System.out.println("       state:" + dea.getState());
			System.out.println("    toString:" + dea.toString());
			System.out.println("    toTargetURL:" + dea.toTargetURL());
			System.out.println();

		}

		int cnt = 0;
		int max = 99999999;
		System.out.println("valid dea from 1 to " + max);
		for (int i = 0; i <= max; i++) {

			DEA dea = DEA.Factory.parse("" + i);
			if (dea.isValid()) {
				System.out.println("[" + (cnt++) + "] " + dea.toTargetURL());
			}
		}

	}
}
