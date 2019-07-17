import java.util.HashMap;
import java.util.Stack;
import java.util.StringTokenizer;

public class Calculator {
	private final static int INCREMENT = 16;
	private static HashMap<String, Integer> mathOperations = new HashMap<String, Integer>();

	public Calculator() {
		mathOperations.put("+", 11);
		mathOperations.put("-", 11);
		mathOperations.put("*", 12);
		mathOperations.put("/", 12);
	}

	/**
	 * @param a         - type: int, -2^32 <= a <= 2^32
	 * @param b         - type: int, -2^32 <= a <= 2^32
	 * @param operation - type: String, expected: '+', '-', '*', '\'
	 * @return binary result
	 */
	private int count(int a, int b, String operation) {
		int res = 0;
		if (operation.equals("+"))
			res = a + b;
		else if (operation.equals("-"))
			res = a - b;
		else if (operation.equals("*"))
			res = a * b;
		else if (operation.equals("/")) {
			if (b == 0)
				throw new IllegalArgumentException("Ділення на нуль");
			res = a / b;
		} else
			throw new UnsupportedOperationException("Невідомий оператор, очикувалось: '+', '-', '*', '\\'");
		return res;
	}

	private RomanNumber count(RomanNumber a, RomanNumber b, String operation) {
		int sum = count(a.getArabicValue(), b.getArabicValue(), operation);
		return new RomanNumber(sum);
	}

	private StringTokenizer tokenizeExpression(String expression) {
		String tokenized = "";
		int bracketsCounter = 0;

		for (int i = 0; i < expression.length(); i++) {
			char currentChar = expression.charAt(i);
			if (currentChar == '(' || currentChar == ')'
					|| mathOperations.containsKey(expression.substring(i, i + 1))) {

				if (currentChar == '(')
					bracketsCounter++;
				else if (currentChar == ')')
					bracketsCounter--;

				if (i == 0)
					tokenized += currentChar + " ";
				else if (i == expression.length() - 1)
					if (tokenized.charAt(tokenized.length() - 1) != ' ')
						tokenized += " " + currentChar;
					else
						tokenized += currentChar;
				else {
					if (tokenized.charAt(tokenized.length() - 1) != ' ')
						tokenized += " " + currentChar + " ";
					else
						tokenized += currentChar + " ";
				}

			} else if (currentChar != ' ')
				tokenized += currentChar;
		}
		if (bracketsCounter != 0)
			throw new IllegalArgumentException("Кількість дужок, що відкриваються і закриваються не збігається");

		if (!isValidExpression(new StringTokenizer(tokenized)))
			throw new IllegalArgumentException("Введенно некоректний вираз(можливо числа різних видів)");

		return new StringTokenizer(tokenized);
	}

	private boolean isValidExpression(StringTokenizer expression) {
		String currentToken = "";
		boolean hasRoman = false;
		boolean hasArabic = false;

		while (expression.hasMoreTokens()) {
			currentToken = expression.nextToken();
			if (mathOperations.containsKey(currentToken))
				continue;
			try {
				Integer.parseInt(currentToken);
				hasArabic = true;
			} catch (NumberFormatException e) {
				if (RomanNumber.isRomanNumber(currentToken))
					hasRoman = true;
				else
					return false;
			}
			if (hasRoman && hasArabic)
				return false;
		}

		return true;
	}

	public String calculate(String expression) {
		StringTokenizer st = tokenizeExpression(expression);
		Stack<Integer> numbers = new Stack<Integer>();
		Stack<String> operations = new Stack<String>();

		boolean isRomanNumbers = false;
		int currentIncrement = 0;

		String currentToken = "";
		while (st.hasMoreTokens()) {
			currentToken = st.nextToken();
			if (currentToken.equals("("))
				currentIncrement += INCREMENT;
			else if (currentToken.equals(")"))
				currentIncrement += INCREMENT;
			else if (mathOperations.containsKey(currentToken)) {
				if (numbers.size() > 1 && mathOperations.get(operations.peek()) >= mathOperations.get(currentToken)) {
					int second = numbers.pop();
					int first = numbers.pop();
					int res = count(first, second, operations.pop());
					numbers.push(res);
				}
				operations.push(currentToken);
			} else {
				if (isRomanNumbers)
					numbers.push(new RomanNumber(currentToken).getArabicValue());
				else {
					try {
						numbers.push(Integer.parseInt(currentToken));
					} catch (NumberFormatException e) {
						isRomanNumbers = true;
						numbers.push(new RomanNumber(currentToken).getArabicValue());
					}
				}
			}

		}
		while (!operations.empty()) {
			int second = numbers.pop();
			int first = numbers.pop();
			int res = count(first, second, operations.pop());
			numbers.push(res);
		}

		return numbers.pop().toString();
	}

	public static void main(String[] args) {
		Calculator calc = new Calculator();

		System.out.println(calc.calculate("IIX+II*II+X"));
	}
}
