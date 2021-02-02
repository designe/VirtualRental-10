package video.rental.demo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RentalSystemController {
	private UIExpress mExpress;
	private DBManager db;
	
	public RentalSystemController(UIExpress express, DBManager database) {
		mExpress = express;
		db = database;
	}
	
	public UIExpress getUIExpress() {
		return mExpress;
	}
	
	void setUIExpress(UIExpress uiExpress) {
		mExpress = uiExpress;
	}
	
	public void clearRentals() {
		mExpress.writeText("Enter customer code: ");
		int customerCode = mExpress.readInteger();

		Customer foundCustomer = db.findCustomerById(customerCode);

		if (foundCustomer == null) {
			mExpress.writeText("No customer found");
		} else {
			mExpress.writeText("Id: " + foundCustomer.getCode() + "\nName: " + foundCustomer.getName() + "\tRentals: "
					+ foundCustomer.getRentals().size());
			for (Rental rental : foundCustomer.getRentals()) {
				mExpress.writeText("\tTitle: " + rental.getVideo().getTitle() + " ");
				mExpress.writeText("\tPrice Code: " + rental.getVideo().getPriceCode());
			}

			List<Rental> rentals = new ArrayList<Rental>();
			foundCustomer.setRentals(rentals);

			db.saveCustomer(foundCustomer);
		}
	}

	public void returnVideo() {
		mExpress.writeText("Enter customer code: ");
		int customerCode = mExpress.readInteger();

		Customer foundCustomer = db.findCustomerById(customerCode);
		if (foundCustomer == null)
			return;

		mExpress.writeText("Enter video title to return: ");
		String videoTitle = mExpress.readText();

		List<Rental> customerRentals = foundCustomer.getRentals();

		for (Rental rental : customerRentals) {
			if (rental.getVideo().getTitle().equals(videoTitle) && rental.getVideo().isRented()) {
				Video video = rental.returnVideo();
				video.setRented(false);
				db.saveVideo(video);
				break;
			}
		}

		db.saveCustomer(foundCustomer);
	}

	public void listVideos() {
		mExpress.writeText("List of videos");

		List<Video> videos = db.findAllVideos();

		for (Video video : videos) {
			mExpress.writeText(
					"Video type: " + video.getVideoType() + 
					"\tPrice code: " + video.getPriceCode() + 
					"\tRating: " + video.getVideoRating() +
					"\tTitle: " + video.getTitle()
					); 
		}
		mExpress.writeText("End of list");
	}

	public void listCustomers() {
		mExpress.writeText("List of customers");

		List<Customer> customers = db.findAllCustomers();

		for (Customer customer : customers) {
			mExpress.writeText("ID: " + customer.getCode() + "\nName: " + customer.getName() + "\tRentals: "
					+ customer.getRentals().size());
			for (Rental rental : customer.getRentals()) {
				mExpress.writeText("\tTitle: " + rental.getVideo().getTitle() + " ");
				mExpress.writeText("\tPrice Code: " + rental.getVideo().getPriceCode());
				mExpress.writeText("\tReturn Status: " + rental.getStatus());
			}
		}
		mExpress.writeText("End of list");
	}

	public void getCustomerReport() {
		mExpress.writeText("Enter customer code: ");
		int code = mExpress.readInteger();

		Customer foundCustomer = db.findCustomerById(code);

		if (foundCustomer == null) {
			mExpress.writeText("No customer found");
		} else {
			String result = foundCustomer.getReport();
			mExpress.writeText(result);
		}
	}

	public void rentVideo() {
		mExpress.writeText("Enter customer code: ");
		int code = mExpress.readInteger();

		Customer foundCustomer = db.findCustomerById(code);
		if (foundCustomer == null)
			return;

		mExpress.writeText("Enter video title to rent: ");
		String videoTitle = mExpress.readText();

		Video foundVideo = db.findVideoByTitle(videoTitle);

		if (foundVideo == null)
			return;

		if (foundVideo.isRented() == true)
			return;

		Boolean status = foundVideo.rentFor(foundCustomer);
		if (status == true) {
			db.saveVideo(foundVideo);
			db.saveCustomer(foundCustomer);
		} else {
			return;
		}
	}

	public void register(String object) {
		if (object.equals("customer")) {
			mExpress.writeText("Enter customer name: ");
			String name = mExpress.readText();

			mExpress.writeText("Enter customer code: ");
			int code = mExpress.readInteger();

			mExpress.writeText("Enter customer birthday: ");
			String dateOfBirth = mExpress.readText();

			Customer customer = new Customer(code, name, LocalDate.parse(dateOfBirth));
			db.saveCustomer(customer);
		} else {
			mExpress.writeText("Enter video title to register: ");
			String title = mExpress.readText();

			mExpress.writeText("Enter video type( 1 for VHD, 2 for CD, 3 for DVD ):");
			int videoType = mExpress.readInteger();

			mExpress.writeText("Enter price code( 1 for Regular, 2 for New Release 3 for Children ):");
			int priceCode = mExpress.readInteger();

			mExpress.writeText("Enter video rating( 1 for 12, 2 for 15, 3 for 18 ):");
			int videoRating = mExpress.readInteger();
			
			LocalDate registeredDate = LocalDate.now();
			Rating rating;
			if (videoRating == 1) rating = Rating.TWELVE;
			else if (videoRating == 2) rating = Rating.FIFTEEN;
			else if (videoRating == 3) rating = Rating.EIGHTEEN;
			else throw new IllegalArgumentException("No such rating " + videoRating);
			
			Video video = new Video(title, videoType, priceCode, rating, registeredDate);

			db.saveVideo(video);
		}
	}
	
}
