package video.rental.demo.domain;


public class ChildrenPriceCode extends PriceCode {

	@Override
	public int getPriceCodeType() {
		// TODO Auto-generated method stub
		return PriceCode.CHILDREN;
	}

	@Override
	public double getCharge(int daysRented) {
		double eachCharge = 1.5;
		if (daysRented > 3)
			eachCharge += (daysRented - 3) * 1.5;
		return eachCharge;
	}
}
