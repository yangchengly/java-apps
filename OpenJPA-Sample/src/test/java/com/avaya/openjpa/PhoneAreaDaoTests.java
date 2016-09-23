package com.avaya.openjpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.Test;

public class PhoneAreaDaoTests {
	
	@Test
	public void test1() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("EliteUCService");
		EntityManager em = emf.createEntityManager();
		
		PhoneareaDao pDao = new PhoneareaDao(em);
		Phonearea p = pDao.findPhonearea("1869862");
		if(p == null)
			System.out.println("it's false");
		else {
			System.out.println("It's true");			
			System.out.println("Area Code:" + p.getAreacode());
		}
		
		
		
		em.close();
		emf.close();
	}
	
//	@Test
//	public void test2() {
//		System.out.println("Junit Hell World");
//	}
	
	public static void main(String args[]) {
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
	
//	public static void main(String[] args) {
//		PhoneAreaDao dao = new PhoneAreaDaoImpl();
//		PhoneAreaEntry entry = dao.getPhoneAreaEntry("1869862");
//	}
	
}
