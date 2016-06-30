package com.ttnd.HibernateCaching.main;

import org.hibernate.CacheMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.ttnd.HibernateCaching.entities.User;
import com.ttnd.HibernateCaching.utils.HibernateUtil;

public class HibernateCachingSecondLevel {

	public static void main(String[] args) {
		User user1, user2;
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		Session session = sessionFactory.openSession();
		try {
		Transaction tx=session.beginTransaction();
		user1 = createUsers("Vishal", "Maurya", "vishal.maurya@tothenew.com");
		user2 = createUsers("Saksham", "Gupta", "saksham.gupta@tothenew.com");
		session.save(user1);
		session.save(user2);
		tx.commit();
		}
		finally {
			session.close();
		}
		
		/*
		 * Hibernate query in different session
		 */
		session = sessionFactory.openSession();
		try{
		session.get(User.class, user1.getId());
		}
		finally {
			session.close();
		}
		session = sessionFactory.openSession();
		try {
			session.get(User.class, user1.getId());
		}
		finally {
			session.close();
		}
		
		/*
		 * Cache Mode
		 */
		session = sessionFactory.openSession();
		session.setCacheMode(CacheMode.IGNORE);
		try {
			user1=(User)session.get(User.class, user1.getId());
			user1.setEmail("xyz@abc.com");
			session.flush();
			
		}
		catch(UnsupportedOperationException unSupportedOperation) {
			System.out.println(unSupportedOperation.getMessage());
		}
		finally {
			session.close();
		}
		
	}
	
	/*
	 * Create Users
	 */
	public static User createUsers(String firstName, String lastName, String email) {
		User user = new User();
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setEmail(email);
		return user;
	}
}
