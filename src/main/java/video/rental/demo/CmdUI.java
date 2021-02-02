package video.rental.demo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

public class CmdUI {
	private static DBManager db = DBManager.getInstance();
	private static Scanner scanner = new Scanner(System.in);
	private RentalSystemController mRentalSystem;
	private UIExpress mUIExpress;

	public CmdUI() {
		
		mRentalSystem = new RentalSystemController(new UIExpress() {
			@Override
			public void writeText(String msg) {
				System.out.println(msg);				
			}

			@Override
			public String readText() {
				return scanner.next();
			}

			@Override
			public int readInteger() {
				return Integer.parseInt(readText());
			}
			
		}, db);
		
		mUIExpress = mRentalSystem.getUIExpress();
		
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
				mRentalSystem.listCustomers();
				break;
			case 2:
				mRentalSystem.listVideos();
				break;
			case 3:
				mRentalSystem.register("customer");
				break;
			case 4:
				mRentalSystem.register("video");
				break;
			case 5:
				mRentalSystem.rentVideo();
				break;
			case 6:
				mRentalSystem.returnVideo();
				break;
			case 7:
				mRentalSystem.getCustomerReport();
				break;
			case 8:
				mRentalSystem.clearRentals();
				break;
			default:
				break;
			}
		}
		mUIExpress.writeText("Bye");
	}

	public int getCommand() {
		mUIExpress.writeText("\nSelect a command !");
		mUIExpress.writeText("\t 0. Quit");
		mUIExpress.writeText("\t 1. List customers");
		mUIExpress.writeText("\t 2. List videos");
		mUIExpress.writeText("\t 3. Register customer");
		mUIExpress.writeText("\t 4. Register video");
		mUIExpress.writeText("\t 5. Rent video");
		mUIExpress.writeText("\t 6. Return video");
		mUIExpress.writeText("\t 7. Show customer report");
		mUIExpress.writeText("\t 8. Show customer and clear rentals");

		int command = mUIExpress.readInteger();

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
