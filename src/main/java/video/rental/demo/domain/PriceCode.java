package video.rental.demo.domain;

public abstract class PriceCode {	
	public static final int REGULAR = 1;
	public static final int NEW_RELEASE = 2;
	public static final int CHILDREN = 3;
	
	public abstract int getPriceCodeType() ;
	public abstract double getCharge(int day);	
	
	public static PriceCode getPriceCode(int type) {		
		switch(type) {
			case REGULAR:
				return new RegularPriceCode();
			case NEW_RELEASE:
				return new NewPriceCode();
			case CHILDREN:
				return new ChildrenPriceCode();			
		}
		return new RegularPriceCode();
	}
}

