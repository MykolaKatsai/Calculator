import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CalculatorCLI {

	public static void main(String[] args) {
		Calculator calc = new Calculator();
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		String next = "";
		while (true) {
			try {
				next = input.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (next.equals("end"))
				return;
			System.out.println(calc.calculate(next));
			System.out.println();
		}
	}

}
