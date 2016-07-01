package com.ttnd.HibernateCaching.main;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.stat.Statistics;

import com.ttnd.HibernateCaching.entities.Book2;
import com.ttnd.HibernateCaching.entities.Chapter;
import com.ttnd.HibernateCaching.utils.HibernateUtil;

public class HibernateCachingSecondLevelCollection {

	public static void main(String[] args) {
		Book2 book = new Book2();
		book.setTitle("Artificial Intelligence");
		Chapter chap1 = new Chapter();
		Chapter chap2 = new Chapter();
		chap1.setTitle("Regression");
		chap2.setTitle("Recursion");
		book.getChapters().add(chap1);
		book.getChapters().add(chap2);
		
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		Session session = sessionFactory.openSession();
		try{
			Transaction tx = session.beginTransaction();
			session.save(chap1);
			session.save(chap2);
			Long bookId = (Long) session.save(book);
			book.setId(bookId);
			tx.commit();
		}
		finally {
			session.close();
		}
		sessionFactory.getCache().evictAllRegions();
		session = sessionFactory.openSession();
		
		try{
			Statistics stats = sessionFactory.getStatistics();
			session.byId(Book2.class).load(book.getId());
			//session.get(Book2.class, book.getId());
			System.out.println(stats.getSecondLevelCachePutCount());
		}
		finally {
			session.close();
		}
		
		
	}
}
