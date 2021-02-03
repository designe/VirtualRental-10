package video.rental.demo.domain;

public class TextReport extends Report {

	public TextReport(Customer customer) {
		super(customer);
	}

	@Override
	String getHeader(String title) {
		return title + this.getCustomer().getName() + pushLineBreak();
	}
	
	@Override
	String pushLineBreak() {
		return "\n";
	}

	@Override
	String pushTab() {
		return "\t";
	}

}
