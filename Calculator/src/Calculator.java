import java.io.IOException;
import java.util.HashMap;
import java.util.Stack;
import java.util.StringTokenizer;
import interfaces.Number;

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
	private Number count(Number a, Number b, String operation) {
		Number res = null;
		if (operation.equals("+"))
			res = a.add(b);
		else if (operation.equals("-"))
			res = a.subtract(b);
		else if (operation.equals("*"))
			res = a.multiply(b);
		else if (operation.equals("/")) {
			if (b.getIntegerValue() == 0)
				throw new IllegalArgumentException("Ділення на нуль");
			res = a.divide(b);
		} else
			throw new UnsupportedOperationException("Невідомий оператор, очикувалось: '+', '-', '*', '\'");
		return res;
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

				if (tokenized.length() == 0)
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
		try {
			isValidExpression(new StringTokenizer(tokenized));
		} catch (IllegalArgumentException e) {
			throw e;
		}

		return new StringTokenizer(tokenized);
	}

	private void isValidExpression(StringTokenizer expression) {
		String currentToken = "";
		boolean hasRoman = false;
		boolean hasArabic = false;

		while (expression.hasMoreTokens()) {
			currentToken = expression.nextToken();
			if (mathOperations.containsKey(currentToken) || currentToken.equals("(") || currentToken.equals(")"))
				continue;
			try {
				Integer.parseInt(currentToken);
				hasArabic = true;
			} catch (NumberFormatException e) {
				if (RomanNumber.isRomanNumber(currentToken))
					hasRoman = true;
				else
					throw new IllegalArgumentException("Введенно некоректний вираз");
			}
			if (hasRoman && hasArabic)
				throw new IllegalArgumentException("Введенно некоректний вираз(присутні числа різних видів)");
		}
	}

	private Number countExpression(StringTokenizer expression) {
		Stack<Number> numbers = new Stack<Number>();
		Stack<String> operations = new Stack<String>();

		boolean isRomanNumbers = false;
		String currentToken = "";
		while (expression.hasMoreTokens()) {
			currentToken = expression.nextToken();
			if (currentToken.equals("("))
				numbers.push(countExpression(expression));
			else if (currentToken.equals(")")) {
				if (numbers.empty())
					throw new IllegalArgumentException("Введенно некоректний вираз(присутні порожні дужки)");
				while (!operations.empty()) {
					Number second = numbers.pop();
					Number first = numbers.pop();
					Number res = count(first, second, operations.pop());
					numbers.push(res);
				}
				if (numbers.size() > 1)
					throw new IllegalArgumentException("Введенно некоректний вираз(пропущено оператор)");
				return numbers.pop();
			} else if (mathOperations.containsKey(currentToken)) {
				if (numbers.size() > 1 && mathOperations.get(operations.peek()) >= mathOperations.get(currentToken)) {
					Number second = numbers.pop();
					Number first = numbers.pop();
					Number res = count(first, second, operations.pop());
					numbers.push(res);
				}
				operations.push(currentToken);
			} else {
				if (isRomanNumbers)
					numbers.push(new RomanNumber(currentToken));
				else {
					try {
						numbers.push(new ArabicNumber(Integer.parseInt(currentToken)));
					} catch (NumberFormatException e) {
						isRomanNumbers = true;
						numbers.push(new RomanNumber(currentToken));
					}
				}
			}

		}
		while (!operations.empty()) {
			Number second = numbers.pop();
			Number first = numbers.pop();
			Number res = count(first, second, operations.pop());
			numbers.push(res);
		}
		if (numbers.size() > 1)
			throw new IllegalArgumentException("Введенно некоректний вираз(пропущено оператор)");
		return numbers.pop();
	}

	public String calculate(String expression) {
		StringTokenizer st = tokenizeExpression(expression);
		return countExpression(st).toString();
	}

	public static void main(String[] args) {
		Calculator calc = new Calculator();

		System.out.println(calc.calculate(" (6)7"));
	}
}
