package video.rental.demo.domain;

public class HTMLReport extends Report {

	public HTMLReport(Customer customer) {
		super(customer);
	}

	@Override
	String getHeader(String title) {
		return "<h1>" + title + this.getCustomer().getName() + "</h1>";
	}

	@Override
	String pushLineBreak() {
		return "<br/>";
	}

	@Override
	String pushTab() {
		return "<span class='tab'>&#9;</span>";
	}

}
