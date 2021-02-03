package video.rental.demo.domain;

public class RegularPriceCode extends PriceCode {

	@Override
	public int getPriceCodeType() {
		// TODO Auto-generated method stub
		return PriceCode.REGULAR;
	}

	@Override
	public double getCharge(int daysRented) {
		double eachCharge = 2;
		if (daysRented > 2)
			eachCharge += (daysRented - 2) * 1.5;
		
		return eachCharge;
	}

}
