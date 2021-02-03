package video.rental.demo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

public class VideoRentalTest {
	
	private GoldenMaster goldenMaster;
	
	@BeforeEach
	void init() {
		goldenMaster = new GoldenMaster();
	}
	
	@Test
	@Disabled
	void generate_goldenmaster() {
		// Given (Arrange)
		
		// When (Act)
		goldenMaster.generate();

		// Then (Assert)
		
	}

	@Test
	@EnabledOnOs({OS.MAC, OS.LINUX})
//	@Disabled
	void runResult_should_match_goldenmaster1() {
		// Given (Arrange)
		String expected = goldenMaster.load();
		
		// When (Act)
		String actual = goldenMaster.getRunResult();

		// Then (Assert)
		assertEquals(expected, actual);
	}
	
	@Test
	@EnabledOnOs({OS.WINDOWS})
//	@Disabled
	void runResult_should_match_goldenmaster2() {
		// Given (Arrange)
		String expected = goldenMaster.load();
		
		// When (Act)
		String actual = goldenMaster.getRunResult();

		// Then (Assert)
		assertEquals(expected, actual.replaceAll("\r\n", "\n"));
	}
}
