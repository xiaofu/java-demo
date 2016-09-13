package com.github.xiaofu.demo.hibernate.demo;

import static org.junit.Assert.*;

import java.util.Date;

import org.hibernate.Session;
import org.junit.Test;

import com.github.xiaofu.demo.hibernate.domain.Event;

public class EventTest {

	@Test
	public void test() {
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

}
