package fr.simplex_software.micro_services_without_spring.customers.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CustomerPhoneNumber
{
  private int area;
  private int exchange;
  private int extension;
}
