package fr.simplex_software.micro_services_without_spring.customers.tests;

import fr.simplex_software.micro_services_without_spring.customers.model.entities.*;
import fr.simplex_software.micro_services_without_spring.customers.model.mappers.*;
import fr.simplex_software.micro_services_without_spring.customers.model.pojos.*;
import org.junit.jupiter.api.*;

import javax.persistence.*;
import java.io.*;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

public class TestEntities extends JpaHibernateTest
{
  @Test
  public void testCustomers()
  {
    Customer customer = unmarshalXmlFileToCustomer(new File("src/test/resources/customer.xml"));
    assertThat(customer).isNotNull();
    getEm().getTransaction().begin();
    getEm().persist(CustomerMapper.INSTANCE.fromCustomer(customer));
    getEm().getTransaction().commit();
    Query q = getEm().createQuery("select c from CustomerEntity c");
    List<CustomerEntity> customers = q.getResultList();
    assertThat(customers).isNotNull();
    assertThat(customers.size()).isNotNull();
    assertThat(customers.size()).isNotZero();
    assertThat(customers.size()).isEqualTo(1);
    CustomerEntity customerEntity = customers.get(0);
    q = getEm().createQuery("select c from CustomerEntity c where id = :id").setParameter("id", customerEntity.getId());
    customerEntity = (CustomerEntity) q.getSingleResult();
    assertThat(customerEntity).isNotNull();
    assertThat(customerEntity.getCustomerRef()).isNotNull();
    assertThat(customerEntity.getCustomerRef()).isEqualTo("Customer1");
    customerEntity.setCustomerRef("Customer2");
    getEm().getTransaction().begin();
    getEm().merge(customerEntity);
    getEm().getTransaction().commit();
    customerEntity = (CustomerEntity) q.getSingleResult();
    assertThat(customerEntity).isNotNull();
    assertThat(customerEntity.getCustomerRef()).isNotNull();
    assertThat(customerEntity.getCustomerRef()).isEqualTo("Customer2");
    getEm().getTransaction().begin();
    getEm().remove(customerEntity);
    Query finalQ = q;
    assertThrows(NoResultException.class, finalQ::getSingleResult);
  }
}
