package fr.simplex_software.micro_services_without_spring.customers.service;

import fr.simplex_software.micro_services_without_spring.customers.model.entities.*;
import fr.simplex_software.micro_services_without_spring.customers.model.mappers.*;
import fr.simplex_software.micro_services_without_spring.customers.model.pojos.*;

import javax.enterprise.inject.*;
import javax.persistence.*;
import javax.transaction.*;
import java.util.*;
import java.util.stream.*;

import static org.apache.commons.lang3.Validate.*;

@Alternative
@Transactional
public class CustomerServiceJpa implements CustomerService
{
  @PersistenceContext(unitName = "customers")
  private EntityManager entityManager;

  public CustomerServiceJpa() {}

  public CustomerServiceJpa (EntityManager entityManager)
  {
    this.entityManager = notNull(entityManager, "entityManager must not be null");
  }

  @Override
  public Customers getCustomers()
  {
    List<CustomerEntity> customers = entityManager.createQuery("SELECT c FROM CustomerEntity c",
      CustomerEntity.class).getResultList();
    return Customers.builder().customers(customers.stream().map(CustomerMapper.INSTANCE::fromEntity)
      .collect(Collectors.toList())).build();
  }

  @Override
  public Customer getCustomer(Long id)
  {
    return CustomerMapper.INSTANCE.fromEntity(entityManager.find(CustomerEntity.class, id));
  }

  @Override
  public Customer getCustomerByRef(String ref)
  {
    return CustomerMapper.INSTANCE.fromEntity(entityManager
      .createQuery("SELECT distinct c FROM CustomerEntity c WHERE c.customerRef=:ref", CustomerEntity.class)
      .setParameter("ref", ref).getSingleResult());
  }

  @Override
  public Long getCustomerIdByRef(String ref)
  {
    CustomerEntity customer =  entityManager.createQuery("SELECT distinct c FROM CustomerEntity c WHERE c.customerRef=:ref",
      CustomerEntity.class).setParameter("ref", ref).getSingleResult();
    return customer.getId();
  }

  @Override
  public void createCustomer(Long id, Customer customer)
  {
    entityManager.persist(CustomerMapper.INSTANCE.fromCustomer(customer));
  }

  @Override
  public void updateCustomer(Long id, Customer customer)
  {
    CustomerEntity customerEntity = entityManager.find(CustomerEntity.class, id);
    CustomerMapper.INSTANCE.updateEntityFromPOJO(customer, customerEntity);
    entityManager.merge(customerEntity);
  }

  @Override
  public void removeCustomer(Long id)
  {
    entityManager.createQuery("DELETE from CustomerEntity c where c.id = :id").setParameter("id", id).executeUpdate();
  }
}
