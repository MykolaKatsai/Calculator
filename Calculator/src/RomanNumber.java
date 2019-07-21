import java.util.HashMap;

import interfaces.Number;

/**
 * 
 */
class RomanNumber implements Number {
	private final int VALUE;
	private final String NUMBER;

	private static HashMap<String, Integer> romanNumbers = new HashMap<String, Integer>();
	private static HashMap<Integer, String> arabicNumbers = new HashMap<Integer, String>();
	private static boolean initialized = false;

	public RomanNumber(String num) {
		if (!initialized)
			initialize();
		VALUE = romanToArabic(num);
		NUMBER = arabicToRoman(VALUE);
		if (!NUMBER.equals(num))
			System.out.println("Введене римське число " + num + " не коректне, можливо малося на увазі: " + NUMBER
					+ " = " + VALUE + ", робота продовжується саме з цим числом.");

	}

	public RomanNumber(int val) {
		if (!initialized)
			initialize();
		NUMBER = arabicToRoman(val);
		VALUE = val;
	}

	public int getArabicValue() {
		return VALUE;
	}

	public String getRomanValue() {
		return NUMBER;
	}

	/**
	 * 
	 */
	private int romanToArabic(String num) {
		int res = 0;
		int localSum = 0;
		int lastVal = 0;

		for (int i = 0; i < num.length(); i++) {
			if (!romanNumbers.containsKey(String.valueOf(num.charAt(i))))
				throw new IllegalArgumentException("Строка " + num + " не є римським числом");
			int value = romanNumbers.get(String.valueOf(num.charAt(i)));
			;

			if (value > lastVal && lastVal != 0) {
				res += value - localSum;
				localSum = 0;
			} else if (value < lastVal) {
				res += localSum;
				localSum = value;
			} else
				localSum += value;

			lastVal = value;
		}
		res += localSum;
		return res;
	}

	/**
	 * 
	 */
	private String arabicToRoman(int val) {
		if (val == 0)
			return "";
		String num = Integer.toString(val);
		int digit = Integer.parseInt(num.substring(0, 1));
		if (digit == 9) {
			int subpart = (int) Math.pow(10, num.length() - 1);
			int mainPart = 10 * subpart;
			return arabicNumbers.get(subpart) + arabicNumbers.get(mainPart) + arabicToRoman(val - mainPart + subpart);
		} else if (digit > 4) {
			int mainPart = 5 * (int) Math.pow(10, num.length() - 1);
			return arabicNumbers.get(mainPart) + arabicToRoman(val - mainPart);
		} else if (digit == 4) {
			int subpart = (int) Math.pow(10, num.length() - 1);
			int mainPart = 5 * subpart;
			return arabicNumbers.get(subpart) + arabicNumbers.get(mainPart) + arabicToRoman(val - mainPart + subpart);
		} else {
			int mainPart = (int) Math.pow(10, num.length() - 1);
			return arabicNumbers.get(mainPart) + arabicToRoman(val - mainPart);
		}

	}

	/**
	 * 
	 */
	private static void initialize() {
		romanNumbers.put("I", 1);
		romanNumbers.put("V", 5);
		romanNumbers.put("X", 10);
		romanNumbers.put("L", 50);
		romanNumbers.put("C", 100);
		romanNumbers.put("D", 500);
		romanNumbers.put("M", 1000);

		arabicNumbers.put(1, "I");
		arabicNumbers.put(5, "V");
		arabicNumbers.put(10, "X");
		arabicNumbers.put(50, "L");
		arabicNumbers.put(100, "C");
		arabicNumbers.put(500, "D");
		arabicNumbers.put(1000, "M");

		initialized = true;
	}

	public static boolean isRomanNumber(String num) {
		if (!initialized)
			initialize();
		for (int i = 0; i < num.length(); i++)
			if (!romanNumbers.containsKey(String.valueOf(num.charAt(i))))
				return false;
		return true;
	}

	@Override
	public Number add(Number n) {
		return new RomanNumber(VALUE + n.getIntegerValue());
	}

	@Override
	public Number subtract(Number n) {
		return new RomanNumber(VALUE - n.getIntegerValue());
	}

	@Override
	public Number multiply(Number n) {
		return new RomanNumber(VALUE * n.getIntegerValue());
	}

	@Override
	public Number divide(Number n) {
		return new RomanNumber(VALUE / n.getIntegerValue());
	}

	@Override
	public int getIntegerValue() {
		return VALUE;
	}

}
