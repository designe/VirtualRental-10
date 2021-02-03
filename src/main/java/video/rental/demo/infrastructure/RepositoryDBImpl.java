package video.rental.demo.infrastructure;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import video.rental.demo.domain.Customer;
import video.rental.demo.domain.Repository;
import video.rental.demo.domain.Video;

public class RepositoryDBImpl implements Repository {

	// JPA EntityManager
	static EntityManager em = PersistenceManager.INSTANCE.getEntityManager();

	@Override
	public Customer findCustomerById(int code) {
		RepositoryDBImpl.getEm().getTransaction().begin();
		Customer customer = RepositoryDBImpl.getEm().find(Customer.class, code);
		RepositoryDBImpl.getEm().getTransaction().commit();
		return customer;
	}

	@Override
	public Video findVideoByTitle(String title) {
		RepositoryDBImpl.getEm().getTransaction().begin();
		Video video = RepositoryDBImpl.getEm().find(Video.class, title);
		RepositoryDBImpl.getEm().getTransaction().commit();
		return video;
	}

	@Override
	public List<Customer> findAllCustomers() {
		TypedQuery<Customer> query = RepositoryDBImpl.getEm().createQuery("SELECT c FROM Customer c", Customer.class);
		return query.getResultList();
	}

	@Override
	public List<Video> findAllVideos() {
		TypedQuery<Video> query = RepositoryDBImpl.getEm().createQuery("SELECT c FROM Video c", Video.class);
		return query.getResultList();
	}

	@Override
	public void saveCustomer(Customer customer) {
		try {
			RepositoryDBImpl.getEm().getTransaction().begin();
			RepositoryDBImpl.getEm().persist(customer);
			RepositoryDBImpl.getEm().getTransaction().commit();
		} catch (PersistenceException e) {
			return;
		}
	}

	@Override
	public void saveVideo(Video video) {
		try {
			RepositoryDBImpl.getEm().getTransaction().begin();
			RepositoryDBImpl.getEm().persist(video);
			RepositoryDBImpl.getEm().getTransaction().commit();
		} catch (PersistenceException e) {
			return;
		}
		return;
	}

	private static EntityManager getEm() {
		return em;
	}

}
