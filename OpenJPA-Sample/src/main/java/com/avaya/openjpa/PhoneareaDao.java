package com.avaya.openjpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

public class PhoneareaDao {
	
	
	private EntityManager em = null;

	public PhoneareaDao(EntityManager em) {
		this.em = em;
	}

	public Phonearea findPhonearea(String mobile) {		
		return em.find(Phonearea.class, mobile);
		
//		Query query = em.createQuery("select T from Phonearea T");
//		return (Phonearea) query.getSingleResult();
	}
	
	public static void main(String[] args) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("EliteUCService");
		EntityManager em = emf.createEntityManager();
		
		PhoneareaDao pDao = new PhoneareaDao(em);
		Phonearea p = pDao.findPhonearea("1869862");
		if(p == null)
			System.out.println("it's false");
		else 
			System.out.println("It's true");
		
		em.close();
		emf.close();
	}
}
