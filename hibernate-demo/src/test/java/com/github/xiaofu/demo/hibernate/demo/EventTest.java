package com.github.xiaofu.demo.hibernate.demo;

import static org.junit.Assert.*;

import java.util.Date;

import org.hibernate.Session;
import org.junit.Test;

import com.github.xiaofu.demo.hibernate.domain.Event;
import com.github.xiaofu.demo.hibernate.domain.Person;

public class EventTest {

	@Test
	public void createAndStoreEvent() {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        String title="new title";
        Date theDate=new Date();
        Event theEvent = new Event();
        theEvent.setTitle(title);
        theEvent.setDate(theDate);
        session.save(theEvent);

        session.getTransaction().commit();
	}
	
	@Test
	public void createAndStorePeople() {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
       
        Person theEvent = new Person();
        theEvent.setAge(2);
        theEvent.setFirstname("xiao");
        theEvent.setLastname("fu");
        session.save(theEvent);

        session.getTransaction().commit();
	}
	@Test
	public void addPersonToEvent()
	{
		 Session session = HibernateUtil.getSessionFactory().getCurrentSession();
	        session.beginTransaction();

	        Person aPerson = (Person) session
	                .createQuery("select p from Person p left join fetch p.events where p.id = :pid")
	                .setParameter("pid", 1l)
	                .uniqueResult(); // Eager fetch the collection so we can use it detached
	        Event anEvent = (Event) session.load(Event.class, 1l);

	        session.getTransaction().commit();

	        // End of first unit of work

	    //    aPerson.getEvents().add(anEvent); // aPerson (and its collection) is detached

	        // Begin second unit of work

	        Session session2 = HibernateUtil.getSessionFactory().getCurrentSession();
	        session2.beginTransaction();
	        session2.update(aPerson); // Reattachment of aPerson

	        session2.getTransaction().commit();
	}
	@Test
	   public void addEmailToPerson() {
		long personId=1;
		 String emailAddress="ww@123.com";
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        Person aPerson = (Person) session.load(Person.class, personId);
        // adding to the emailAddress collection might trigger a lazy load of the collection
        //aPerson.getEmailAddresses().add(emailAddress);

        session.getTransaction().commit();
    }

}
