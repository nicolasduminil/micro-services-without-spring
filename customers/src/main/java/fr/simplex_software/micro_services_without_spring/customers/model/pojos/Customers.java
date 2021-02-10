package fr.simplex_software.micro_services_without_spring.customers.model.pojos;

import lombok.*;
import org.eclipse.microprofile.openapi.annotations.media.*;

import javax.validation.constraints.*;
import javax.xml.bind.annotation.*;
import java.util.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@XmlRootElement(name = "customers")
@XmlAccessorType (XmlAccessType.FIELD)
@Schema
public class Customers
{
  @XmlElement(name = "customer")
  @NotNull
  private List<Customer> customers;
}
