package fr.simplex_software.micro_services_without_spring.customers.model;

import lombok.*;

import javax.xml.bind.annotation.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@XmlRootElement(name="customer")
@XmlAccessorType (XmlAccessType.FIELD)
public class Customer
{
  private String customerRef;
  private CustomerType customerType;
  private CustomerContactDetails contactDetails;
}
