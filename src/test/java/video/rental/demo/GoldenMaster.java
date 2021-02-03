package video.rental.demo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import video.rental.demo.application.Interactor;
import video.rental.demo.domain.Repository;
import video.rental.demo.infrastructure.RepositoryMemImpl;
import video.rental.demo.presentation.CmdUI;
import video.rental.demo.utils.SampleGenerator;

public class GoldenMaster {
	
	private static final String GoldenMasterFile = "./goldenmaster/goldenmaster.txt";

	private CmdUI ui;
	
	private String simulatedInput = "1\n" // List customer
			+ "2\n"	// List video
			+ "3\n"	// Register customer
			+ "Peter\n" + "3\n" + "1980-07-11\n" 
			+ "1\n"	// List customer
			+ "4\n" // Register video
			+ "V4\n" + "1\n" + "2\n" + "3\n"
			+ "2\n" // List video 
			+ "5\n" // Rent video
			+ "2\n" + "V3\n"
			+ "1\n"	// List customer
			+ "6\n" // Return video
			+ "0\n" + "V1\n"
			+ "7\n" // Show customer report
			+ "0\n"
			+ "6\n" // Return video
			+ "0\n" + "V2\n"
			+ "8\n" // Show customer and clear rentals
			+ "0\n"
			+ "1\n" // List customers
			+ "0\n"; // Bye

	public String getRunResult() {
		redirectInput();
		ByteArrayOutputStream ostream = redirectOutput();
		
		Repository repository = new RepositoryMemImpl();
		Interactor interactor = new Interactor(repository);
		new SampleGenerator(repository).generateSamples();
		ui = new CmdUI(interactor);
		ui.start();
		
		return ostream.toString();
	}

	private void redirectInput() {
		ByteArrayInputStream istream = new ByteArrayInputStream(simulatedInput.getBytes());
		System.setIn(istream);
	}

	private ByteArrayOutputStream redirectOutput() {
		ByteArrayOutputStream ostream = new ByteArrayOutputStream();
		PrintStream pstream = new PrintStream(ostream);
		System.setOut(pstream);
		return ostream;
	}

	public void generate() {
		try {
			Files.write(Paths.get(GoldenMasterFile), getRunResult().getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
//	List<String> -> String:  "record1\nrecord2\nrecord3"
	
	public String load() {
		try {
			return Files.readAllLines(Paths.get(GoldenMasterFile)).stream().collect(Collectors.joining("\n", "", "\n"));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
