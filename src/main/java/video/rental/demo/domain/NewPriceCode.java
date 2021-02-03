package video.rental.demo.domain;


public class NewPriceCode extends PriceCode {

	@Override
	public int getPriceCodeType() {
		// TODO Auto-generated method stub
		return PriceCode.CHILDREN;
	}

	@Override
	public double getCharge(int daysRented) {
		return  daysRented * 3;		
	}
}
