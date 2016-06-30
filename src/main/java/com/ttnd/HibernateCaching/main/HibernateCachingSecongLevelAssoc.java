package com.ttnd.HibernateCaching.main;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.ttnd.HibernateCaching.entities.Book;
import com.ttnd.HibernateCaching.entities.Publisher;
import com.ttnd.HibernateCaching.utils.HibernateUtil;

public class HibernateCachingSecongLevelAssoc {

	public static void main(String[] args) {
		/*
		 * Adding some dummy data
		 */
		Publisher publisher = new Publisher();
		Book book1 = new Book();
		Book book2 = new Book();
		publisher.setName("Saurabh Mittal");
		book1.setTitle("How to hit bird's eye!");
		book1.setPublisher(publisher);
		book2.setTitle("Case Study of Google App Engine");
		
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		Session session = sessionFactory.openSession();
		try {
		Transaction tx = session.beginTransaction();
		session.save(publisher);
		Long id = (Long) session.save(book1);
		book1.setId(id);
		session.save(book2);
		tx.commit();
		}
		finally {
			session.close();
		}
		sessionFactory.getCache().evictAllRegions();
		session =sessionFactory.openSession();
		try{
			Book getBook1=(Book)session.get(Book.class, book1.getId()) ;
		}
		finally {
			session.close();
		}

	}

}
