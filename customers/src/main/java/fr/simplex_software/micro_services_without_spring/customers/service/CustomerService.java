package fr.simplex_software.micro_services_without_spring.customers.service;

import fr.simplex_software.micro_services_without_spring.customers.model.*;
import fr.simplex_software.micro_services_without_spring.customers.service.exceptions.*;
import lombok.extern.slf4j.*;

import javax.enterprise.context.*;
import java.io.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.atomic.*;

import static java.util.stream.Collectors.*;

@ApplicationScoped
@Slf4j
public class CustomerService
{
  private Map<Long, Customer> customers = new HashMap<>();

  public Customers getCustomers()
  {
    return Customers.builder().customers(customers.values().stream().collect(toList())).build();
  }

  public Customer getCustomer(Long key)
  {
    return customers.get(key);
  }

  public Customer getCustomerByRef(String ref)
  {
    return customers.values().stream().filter(customer -> ref.equals(customer.getCustomerRef())).findFirst().orElse(null);
  }

  public Long getCustomerIdByRef(String ref)
  {
    return customers.entrySet().stream().filter(es -> ref.equals(es.getValue()
      .getCustomerRef())).map(Map.Entry::getKey).findFirst()
      .orElseThrow(() -> new CustomerReferenceNotFound("Cannot find customer with reference " + ref));
  }

  public void createCustomer(Long key, Customer customer)
  {
    customers.put(key, customer);
  }

  public void updateCustomer(Long key, Customer customer)
  {
    customers.put(key, customer);
  }

  public void removeCustomer(Long key)
  {
    customers.remove(key);
  }
}
