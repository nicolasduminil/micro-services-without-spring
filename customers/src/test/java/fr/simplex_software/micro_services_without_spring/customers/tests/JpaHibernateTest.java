package fr.simplex_software.micro_services_without_spring.customers.tests;

import org.junit.jupiter.api.*;

import javax.persistence.*;
import java.io.*;
import java.sql.*;

public class JpaHibernateTest
{
  private static EntityManagerFactory emf;
  private static EntityManager em;

  @BeforeAll
  public static void init() throws FileNotFoundException, SQLException
  {
    emf = Persistence.createEntityManagerFactory("customers-test");
    em = emf.createEntityManager();
  }

  @AfterAll
  public static void tearDown()
  {
    em.clear();
    em.close();
    emf.close();
  }

  public static EntityManagerFactory getEmf()
  {
    return emf;
  }

  public static EntityManager getEm()
  {
    return em;
  }
}
