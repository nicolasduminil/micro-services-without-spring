package fr.simplex_software.micro_services_without_spring.customers.model.mappers;

import fr.simplex_software.micro_services_without_spring.customers.model.entities.*;
import fr.simplex_software.micro_services_without_spring.customers.model.pojos.*;
import org.mapstruct.*;
import org.mapstruct.factory.*;

@Mapper
public interface CustomerMapper
{
  public CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);
  public Customer fromEntity(CustomerEntity customerEntity);
  public CustomerEntity fromCustomer (Customer customer);
  public void updateEntityFromPOJO(Customer customer, @MappingTarget CustomerEntity customerEntity);
}
