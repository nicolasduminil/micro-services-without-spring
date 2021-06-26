package fr.simplex_software.micro_services_without_spring.customers.service;

import fr.simplex_software.micro_services_without_spring.customers.model.pojos.*;

public interface CustomerService
{
  Customers getCustomers();
  Customer getCustomer(Long key);
  Customer getCustomerByRef(String ref);
  Long getCustomerIdByRef(String ref);
  void createCustomer(Long key, Customer customer);
  void updateCustomer(Long key, Customer customer);
  void removeCustomer(Long key);
}
