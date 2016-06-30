package com.ttnd.HibernateCaching.main;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.ttnd.HibernateCaching.entities.User;
import com.ttnd.HibernateCaching.utils.HibernateUtil;

public class HibernateCachingFirstLevel {

	public static void main(String[] args) {
		User user = null;
		Long userId = null;
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = sf.openSession();
		try{
		
		session.beginTransaction();

		/*
		 * Creating First User
		 */
		user = new User();
		user.setFirstName("Vishal");
		user.setLastName("Maurya");
		user.setEmail("vishal.maurya@tothenew.com");
		
		session.getTransaction().commit();
		
		/*
		 * Saving User to the database and getting their id
		 */
		userId = (Long)session.save(user);
		}
		finally
		{
			session.close();
		}
		
		/*
		 * Running same query in the same session
		 */
		session = sf.openSession();
		try{
			System.out.println(">>>>>>>>>>>>>INSIDE SAME SESSION<<<<<<<<<<<<<<<<<<<");
			user = (User) session.get(User.class, userId);
			
			User secondTime=(User) session.get(User.class, userId);
			
		}
		finally
		{
			session.close();
		}
		
		/*
		 * Running same query in different session
		 */
		session = sf.openSession();
		try
		{
			System.out.println(">>>>>>>>>>>>>INSIDE DIFFERENT SESSION<<<<<<<<<<<<<<");
			user = (User) session.get(User.class, userId);
		}
		finally{
			session.close();
		}
		session = sf.openSession();
		try{
			user = (User) session.get(User.class, userId);
		}
		finally{
			session.close();
		}
	}
}
