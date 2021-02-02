package video.rental.demo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

public class CmdUI implements UIExpress {
	private static DBManager db = DBManager.getInstance();
	
	private static Scanner scanner = new Scanner(System.in);

	@Override
	public void writeText(String msg) {
		System.out.println(msg);
	}

	@Override
	public String readText() {
		return scanner.next();
	}
	
	private int readInteger() {
		return Integer.parseInt(readText());
	}
	
	public void start() {
		generateSamples();

		boolean quit = false;
		while (!quit) {
			int command = getCommand();
			switch (command) {
			case 0:
				quit = true;
				break;
			case 1:
				listCustomers();
				break;
			case 2:
				listVideos();
				break;
			case 3:
				register("customer");
				break;
			case 4:
				register("video");
				break;
			case 5:
				rentVideo();
				break;
			case 6:
				returnVideo();
				break;
			case 7:
				getCustomerReport();
				break;
			case 8:
				clearRentals();
				break;
			default:
				break;
			}
		}
		writeText("Bye");
	}

	public void clearRentals() {
		writeText("Enter customer code: ");
		int customerCode = readInteger();

		Customer foundCustomer = db.findCustomerById(customerCode);

		if (foundCustomer == null) {
			writeText("No customer found");
		} else {
			writeText("Id: " + foundCustomer.getCode() + "\nName: " + foundCustomer.getName() + "\tRentals: "
					+ foundCustomer.getRentals().size());
			for (Rental rental : foundCustomer.getRentals()) {
				writeText("\tTitle: " + rental.getVideo().getTitle() + " ");
				writeText("\tPrice Code: " + rental.getVideo().getPriceCode());
			}

			List<Rental> rentals = new ArrayList<Rental>();
			foundCustomer.setRentals(rentals);

			db.saveCustomer(foundCustomer);
		}
	}

	public void returnVideo() {
		writeText("Enter customer code: ");
		int customerCode = readInteger();

		Customer foundCustomer = db.findCustomerById(customerCode);
		if (foundCustomer == null)
			return;

		writeText("Enter video title to return: ");
		String videoTitle = readText();

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
		writeText("List of videos");

		List<Video> videos = db.findAllVideos();

		for (Video video : videos) {
			writeText(
					"Video type: " + video.getVideoType() + 
					"\tPrice code: " + video.getPriceCode() + 
					"\tRating: " + video.getVideoRating() +
					"\tTitle: " + video.getTitle()
					); 
		}
		writeText("End of list");
	}

	public void listCustomers() {
		writeText("List of customers");

		List<Customer> customers = db.findAllCustomers();

		for (Customer customer : customers) {
			writeText("ID: " + customer.getCode() + "\nName: " + customer.getName() + "\tRentals: "
					+ customer.getRentals().size());
			for (Rental rental : customer.getRentals()) {
				writeText("\tTitle: " + rental.getVideo().getTitle() + " ");
				writeText("\tPrice Code: " + rental.getVideo().getPriceCode());
				writeText("\tReturn Status: " + rental.getStatus());
			}
		}
		writeText("End of list");
	}

	public void getCustomerReport() {
		writeText("Enter customer code: ");
		int code = readInteger();

		Customer foundCustomer = db.findCustomerById(code);

		if (foundCustomer == null) {
			writeText("No customer found");
		} else {
			String result = foundCustomer.getReport();
			writeText(result);
		}
	}

	public void rentVideo() {
		writeText("Enter customer code: ");
		int code = readInteger();

		Customer foundCustomer = db.findCustomerById(code);
		if (foundCustomer == null)
			return;

		writeText("Enter video title to rent: ");
		String videoTitle = readText();

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
			writeText("Enter customer name: ");
			String name = readText();

			writeText("Enter customer code: ");
			int code = readInteger();

			writeText("Enter customer birthday: ");
			String dateOfBirth = readText();

			Customer customer = new Customer(code, name, LocalDate.parse(dateOfBirth));
			db.saveCustomer(customer);
		} else {
			writeText("Enter video title to register: ");
			String title = readText();

			writeText("Enter video type( 1 for VHD, 2 for CD, 3 for DVD ):");
			int videoType = readInteger();

			writeText("Enter price code( 1 for Regular, 2 for New Release 3 for Children ):");
			int priceCode = readInteger();

			writeText("Enter video rating( 1 for 12, 2 for 15, 3 for 18 ):");
			int videoRating = readInteger();
			
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

	public int getCommand() {
		writeText("\nSelect a command !");
		writeText("\t 0. Quit");
		writeText("\t 1. List customers");
		writeText("\t 2. List videos");
		writeText("\t 3. Register customer");
		writeText("\t 4. Register video");
		writeText("\t 5. Rent video");
		writeText("\t 6. Return video");
		writeText("\t 7. Show customer report");
		writeText("\t 8. Show customer and clear rentals");

		int command = readInteger();

		return command;
	}

	private void generateSamples() {
		Customer james = new Customer(0, "James", LocalDate.parse("1975-05-15"));
		Customer brown = new Customer(1, "Brown", LocalDate.parse("2002-03-17"));
        Customer shawn = new Customer(2, "Shawn", LocalDate.parse("2010-11-11"));
		db.saveCustomer(james);
		db.saveCustomer(brown);
		db.saveCustomer(shawn);

		Video v1 = new Video("V1", Video.CD, Video.REGULAR, Rating.FIFTEEN, LocalDate.of(2018, 1, 1));
		v1.setRented(true);
		Video v2 = new Video("V2", Video.DVD, Video.NEW_RELEASE, Rating.TWELVE, LocalDate.of(2018, 3, 1));
		v2.setRented(true);
        Video v3 = new Video("V3", Video.VHS, Video.NEW_RELEASE, Rating.EIGHTEEN, LocalDate.of(2018, 3, 1));

		db.saveVideo(v1);
		db.saveVideo(v2);
		db.saveVideo(v3);

		Rental r1 = new Rental(v1);
		Rental r2 = new Rental(v2);

		List<Rental> rentals = james.getRentals();
		rentals.add(r1);
		rentals.add(r2);
		james.setRentals(rentals);
		db.saveCustomer(james);
	}


}
