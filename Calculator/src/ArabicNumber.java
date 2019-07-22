import interfaces.Number;

public class ArabicNumber implements Number {
	private final int VALUE;

	public ArabicNumber(int value) {
		VALUE = value;
	}

	@Override
	public Number add(Number n) {
		return new ArabicNumber(VALUE + n.getIntegerValue());
	}

	@Override
	public Number subtract(Number n) {
		return new ArabicNumber(VALUE - n.getIntegerValue());
	}

	@Override
	public Number multiply(Number n) {
		return new ArabicNumber(VALUE * n.getIntegerValue());
	}

	@Override
	public Number divide(Number n) {
		return new ArabicNumber(VALUE / n.getIntegerValue());
	}

	@Override
	public int getIntegerValue() {
		return VALUE;
	}

	public String toString() {
		return new Integer(VALUE).toString();
	}

}
