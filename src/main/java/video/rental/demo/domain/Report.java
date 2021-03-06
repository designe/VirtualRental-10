package video.rental.demo.domain;

import java.util.List;

public abstract class Report {
	
	private Customer mCustomer;
	
	public Customer getCustomer() {
		return mCustomer;
	}

	public void setCustomer(Customer customer) {
		this.mCustomer = customer;
	}

	public Report(Customer customer) {
		mCustomer = customer;
	}
	
	abstract String pushLineBreak();
	abstract String pushTab();
	abstract String getHeader(String title);
	
	String getFooter(double totalCharge, int totalPoint) {
		return "Total charge: " + totalCharge + pushTab() + "Total Point:" + totalPoint + pushLineBreak();
	}
	
	private String extractFinalResult(String videoTitle, int daysRented, double rentalCharge, int rentalPoint) {
		return pushTab() + videoTitle + pushTab() + "Days rented: " + daysRented + pushTab() + "Charge: " + rentalCharge
				+ pushTab() + "Point: " + rentalPoint + pushLineBreak();
	}
	
	public String content() {
		String result = getHeader("Customer Report for ");

		double totalCharge = 0;
		int totalPoint = 0;

		for (Rental rental : mCustomer.getRentals()) {
			double rentalCharge = 0;
			int rentalPoint = rental.getRentalPoint(); // 기본적으로 포인트는 1점부터 시작
			int daysRented = rental.getDaysRented();
			
			PriceCode priceCode = rental.getVideo().getPriceCode();
			
			rentalCharge = priceCode.getCharge(daysRented);

			result += extractFinalResult(rental.getVideo().getTitle(), daysRented, rentalCharge, rentalPoint);

			totalCharge += rentalCharge;
			totalPoint += rentalPoint;
		}
		
		result += getFooter(totalCharge, totalPoint);
		
		String[] msg = {"Congrat! You earned one free coupon", "Congrat! You earned two free coupon"};
		int[] points = {10,30};
		new RewardPolicy(new Reward(2, msg, points)).displayIfRewardIsAvailable(totalPoint);
		
		return result;
	}
}
