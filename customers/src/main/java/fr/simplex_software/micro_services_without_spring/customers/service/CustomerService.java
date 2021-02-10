package fr.simplex_software.micro_services_without_spring.customers.service;

import fr.simplex_software.micro_services_without_spring.customers.model.pojos.*;

public interface CustomerService
{
  public Customers getCustomers();
  public Customer getCustomer(Long key);
  public Customer getCustomerByRef(String ref);
  public Long getCustomerIdByRef(String ref);
  public void createCustomer(Long key, Customer customer);
  public void updateCustomer(Long key, Customer customer);
  public void removeCustomer(Long key);
}
