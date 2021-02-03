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
	
	public String content() {
		String result = getHeader("Customer Report for ");

		double totalCharge = 0;
		int totalPoint = 0;

		for (Rental each : mCustomer.getRentals()) {
			double eachCharge = 0;
			int eachPoint = 0;
			int daysRented = 0;

			daysRented = each.getDaysRented();

			switch (each.getVideo().getPriceCode()) {
			case Video.REGULAR:
				eachCharge += 2;
				if (daysRented > 2)
					eachCharge += (daysRented - 2) * 1.5;
				break;
			case Video.NEW_RELEASE:
				eachCharge = daysRented * 3;
				break;
			case Video.CHILDREN:
				eachCharge += 1.5;
				if (daysRented > 3)
					eachCharge += (daysRented - 3) * 1.5;
				break;
			}
			
			eachPoint++;
			if ((each.getVideo().getPriceCode() == Video.NEW_RELEASE))
				eachPoint++;

			if (daysRented > each.getDaysRentedLimit())
				eachPoint -= Math.min(eachPoint, each.getVideo().getLateReturnPointPenalty());

			result += pushTab() + each.getVideo().getTitle() + pushTab() + "Days rented: " + daysRented + pushTab() + "Charge: " + eachCharge
					+ pushTab() + "Point: " + eachPoint + pushLineBreak();

			totalCharge += eachCharge;
			totalPoint += eachPoint;
		}
		
		result += getFooter(totalCharge, totalPoint);
		
		String[] msg = {"Congrat! You earned one free coupon", "Congrat! You earned two free coupon"};
		int[] points = {10,30};
		new RewardPolicy(new Reward(2, msg, points)).displayIfRewardIsAvailable(totalPoint);
		
		return result;
	}
}
