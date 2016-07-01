package com.ttnd.HibernateCaching.main;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.ttnd.HibernateCaching.entities.User;
import com.ttnd.HibernateCaching.utils.HibernateUtil;

public class HibernateCachingQueryCaching {

	public static void main(String[] args) {
		User user1, user2, user3;
		user1 = createUser("Vishal", "Maurya", "vishal.maurya@tothenew.com");
		user2 = createUser("Saurabh", "Mittal", "saurabh.mittal@tothenew.com");
		user3 = createUser("James", "abc", "james.abc@tothenew.com");
		/*
		 * Saving Users
		 */
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		Session session = sessionFactory.openSession();
		try {
			Transaction tx = session.beginTransaction();
			session.save(user1);
			session.save(user2);
			session.save(user3);
			tx.commit();
		}
		finally {
			session.close();
		}
		sessionFactory.getCache().evictAllRegions();
		/*
		 * Hit Query Without Caching
		 */
		session = sessionFactory.openSession();
		try{
		Query query = session.createQuery("FROM User u where u.email like :email");
		query.setParameter("email", user1.getEmail());
		List<User> users = (List<User>)query.list();
		}
		finally {
			session.close();
		}
		/*
		 * Hit Again
		 */
		session = sessionFactory.openSession();
		try{
		Query query = session.createQuery("FROM User u where u.email like :email");
		query.setParameter("email", user1.getEmail());
		List<User> users = (List<User>)query.list();
		}
		finally {
			session.close();
		}
		
		/*
		 * Hit Query with Caching
		 */
		session = sessionFactory.openSession();
		try{
		Query query = session.createQuery("FROM User u where u.email like :email");
		query.setParameter("email", user1.getEmail());
		query.setCacheable(true);
		List<User> users = (List<User>)query.list();
		}
		finally {
			session.close();
		}
		
		/*
		 * Hit again
		 */
		session = sessionFactory.openSession();
		try{
		Query query = session.createQuery("FROM User u where u.email like :email");
		query.setParameter("email", user1.getEmail());
		query.setCacheable(true);
		List<User> users = (List<User>)query.list();
		}
		finally {
			session.close();
		}
	}
	
	private static User createUser(String firstName, String lastName, String email) {
		User user = new User();
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setEmail(email);
		return user;
	}
}
