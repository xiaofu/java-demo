package com.github.xiaofu.demo.hibernate.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
@Entity
public class Person {

	@Id
	@GeneratedValue
    private Long id;
    private int age;
    private String firstname;
    private String lastname;
    private String name;
    
    @Embedded
    private  Country bornIn;
    //private Set<String> emailAddresses = new HashSet<String>();

    public Country getBornIn() {
		return bornIn;
	}

	public void setBornIn(Country bornIn) {
		this.bornIn = bornIn;
	}

	/*public void setEmailAddresses(Set<String> emailAddresses) {
		this.emailAddresses = emailAddresses;
	}

	public Set<String> getEmailAddresses() {
        return emailAddresses;
    }*/
    
    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
/*	private Set<Event> events = new HashSet<Event>();

    public Set<Event> getEvents() {
        return events;
    }

    public void setEvents(Set<Event> events) {
        this.events = events;
    }*/
    public Person() {}

    // Accessor methods for all properties, private setter for 'id'

}
