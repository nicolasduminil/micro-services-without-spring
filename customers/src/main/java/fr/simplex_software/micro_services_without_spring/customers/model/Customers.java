package fr.simplex_software.micro_services_without_spring.customers.model;

import lombok.*;

import javax.validation.*;
import javax.validation.constraints.*;
import javax.xml.bind.annotation.*;
import java.util.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@XmlRootElement(name = "customers")
@XmlAccessorType (XmlAccessType.FIELD)
public class Customers
{
  @XmlElement(name = "customer")
  @NotNull
  private List<Customer> customers;
}
