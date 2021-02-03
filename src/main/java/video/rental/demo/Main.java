package video.rental.demo;

import video.rental.demo.application.Interactor;
import video.rental.demo.domain.Repository;
import video.rental.demo.infrastructure.RepositoryMemImpl;
import video.rental.demo.presentation.CmdUI;

public class Main {
	private static CmdUI ui;

	public static void main(String[] args) {
		Repository repository = new RepositoryMemImpl();
		ui = new CmdUI(new Interactor(repository));
		ui.start();
	}
}
