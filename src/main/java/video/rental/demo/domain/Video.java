package video.rental.demo.domain;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "VIDEO", uniqueConstraints = { @UniqueConstraint(columnNames = { "title" }) })
public class Video {
	@Id
	private String title;
	private Rating videoRating;
	private PriceCode priceCode;
	public static final int REGULAR = 1;
	public static final int NEW_RELEASE = 2;
	public static final int CHILDREN = 3;

	private int videoType;
	public static final int VHS = 1;
	public static final int CD = 2;
	public static final int DVD = 3;

	private LocalDate registeredDate;
	private boolean rented;

	public Video() {
	} // for hibernate

	public Video(String title, int videoType, int priceCode, Rating videoRating, LocalDate registeredDate) {
		this.title = title;
		this.videoType = videoType;
		setPriceCode(priceCode);
		this.videoRating = videoRating;
		this.registeredDate = registeredDate;
		this.rented = false;
	}

	public int getLateReturnPointPenalty() {
		int pentalty = 0;
		switch (videoType) {
		case VHS:
			pentalty = 1;
			break;
		case CD:
			pentalty = 2;
			break;
		case DVD:
			pentalty = 3;
			break;
		}
		return pentalty;
	}

	public PriceCode getPriceCode() {
		return priceCode;
	}

	public void setPriceCode(int code) {
		switch(code) {
		case REGULAR:
			priceCode = new RegularPriceCode();
			break;
		case NEW_RELEASE:
			priceCode = new NewPriceCode();
			break;
		case CHILDREN:
			priceCode = new ChildrenPriceCode();
			break;
		}
	}

	public String getTitle() {
		return title;
	}

	public Rating getVideoRating() {
		return videoRating;
	}

	public boolean isRented() {
		return rented;
	}

	public void setRented(boolean rented) {
		this.rented = rented;
	}

	public LocalDate getRegisteredDate() {
		return registeredDate;
	}

	public int getVideoType() {
		return videoType;
	}

	public boolean rentFor(Customer customer) {
		if (!isUnderAge(customer)) {
			setRented(true);
			Rental rental = new Rental(this);
			List<Rental> customerRentals = customer.getRentals();
			customerRentals.add(rental);
			customer.setRentals(customerRentals);
			return true;
		} else {
			return false;
		}
	}

	private Calendar getCurrentDate() {
		Calendar calNow = Calendar.getInstance();
		calNow.setTime(new java.util.Date());
		return calNow;
	}
	
	private int calculateAgeDifference(Calendar calDateOfBirth) {
		int ageMo = (getCurrentDate().get(Calendar.MONTH) - calDateOfBirth.get(Calendar.MONTH));
		int ageYr = (getCurrentDate().get(Calendar.YEAR) - calDateOfBirth.get(Calendar.YEAR));
		
		if (ageMo < 0) {
			ageYr--;
		}
		
		return ageYr;
	}
	
	public boolean isUnderAge(Customer customer) {
		// calculate customer's age in years and months

		// parse customer date of birth
		Calendar calDateOfBirth = Calendar.getInstance();
		try {
			calDateOfBirth.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(customer.getDateOfBirth().toString()));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		int age = calculateAgeDifference(calDateOfBirth);

		// determine if customer is under legal age for rating
		switch (videoRating) {
		case TWELVE:
			return age < 12;
		case FIFTEEN:
			return age < 15;
		case EIGHTEEN:
			return age < 18;
		default:
			return false;
		}
	}
}
