package fr.simplex_software.micro_services_without_spring.customers.model;

import lombok.*;

import javax.xml.bind.annotation.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CustomerAddress
{
  private int number;
  private String street;
  private String city;
  private String province;
  private String zip;
  private String country;
}
