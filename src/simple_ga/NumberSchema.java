package simple_ga;

public class NumberSchema {
	Signs sign;
	String numbs;

	public Signs getSign() {
		return sign;
	}

	public void setSign(Signs sign) {
		this.sign = sign;
	}

	public String getNumbs() {
		return numbs;
	}

	public void setNumbs(String numbs) {
		this.numbs = numbs;
	}

	public NumberSchema(Signs sign, String numbs) {
		this.sign = sign;
		this.numbs = numbs;
	}

	@Override
	public String toString() {
		return numbs + "(" + sign.name() + ")";
	}

}
