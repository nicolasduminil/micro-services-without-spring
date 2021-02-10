package fr.simplex_software.micro_services_without_spring.customers.service;

import fr.simplex_software.micro_services_without_spring.customers.model.entities.*;
import fr.simplex_software.micro_services_without_spring.customers.model.pojos.*;

import javax.enterprise.inject.*;
import javax.persistence.*;
import java.util.*;

@Alternative
public class CustomerServiceJpa implements CustomerService
{
  //@PersistenceContext(unitName = "customers")
  private EntityManager entityManager;

  @Override
  public Customers getCustomers()
  {
    List<Customer> customers = entityManager.createQuery("SELECT c FROM Customer c", Customer.class).getResultList();
    return Customers.builder().customers(customers).build();
  }

  @Override
  public Customer getCustomer(Long id)
  {
    Customer customer = entityManager.createQuery("SELECT distinct c FROM Customer c WHERE c.id=:id", Customer.class).setParameter("id", id).getSingleResult();
    return customer;
  }

  @Override
  public Customer getCustomerByRef(String ref)
  {
    Customer customer = entityManager.createQuery("SELECT distinct c FROM Customer c WHERE c.customerRef=:ref", Customer.class).setParameter("ref", ref).getSingleResult();
    return customer;
  }

  @Override
  public Long getCustomerIdByRef(String ref)
  {
    /*CustomerEntity customer =  entityManager.createQuery("SELECT distinct c FROM Customer c WHERE c.customerRef=:ref", CustomerEntity.class).setParameter("ref", ref).getSingleResult();
    return Customer.builder().customerRef(customer.getCustomerRef()).customerType(customer.getCustomerType())
      .contactDetails(CustomerContactDetails.builder().firstName(customer.getContactDetails().getFirstName())
        .lastName(customer.getContactDetails().getLastName()).address(CustomerAddress.builder()
          .number(customer.getContactDetails().getAddress().getNumber())*/
    return null;
  }

  @Override
  public void createCustomer(Long id, Customer customer)
  {
    entityManager.persist(CustomerEntity.builder().id(id).customerRef(customer.getCustomerRef()));
  }

  @Override
  public void updateCustomer(Long key, Customer customer)
  {

  }

  @Override
  public void removeCustomer(Long key)
  {

  }

}
