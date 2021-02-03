package fr.simplex_software.micro_services_without_spring.customers.service.exceptions;

public class CustomerReferenceNotFound extends RuntimeException
{
  public CustomerReferenceNotFound (String msg)
  {
    super(msg);
  }
}
