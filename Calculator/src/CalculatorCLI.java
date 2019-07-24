import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CalculatorCLI {

	public static void main(String[] args) {
		Calculator calc = new Calculator();
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		String next = "";

		System.out
				.println("Програма обчислює вирази з арабських або римських чисел, щоб завершити роботу введіть (end)");
		while (true) {
			System.out.print("Введіть вираз: ");
			try {
				next = input.readLine();
			} catch (IOException e) {
				System.out.println(e);
			}
			if (next.equals("end"))
				return;
			try {
				System.out.println(calc.calculate(next));
				System.out.println();
			} catch (IllegalArgumentException e) {
				System.out.println("Exception: " + e.getMessage());
				System.out.println();
			} catch (UnsupportedOperationException e) {
				System.out.println("Exception: " + e.getMessage());
				System.out.println();
			}catch (Exception e) {
				System.out.println("Exception: невідома помилка");
				System.out.println();
			}
		}
	}

}
