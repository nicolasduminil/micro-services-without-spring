package fr.simplex_software.micro_services_without_spring.customers.model;

import lombok.*;

import javax.mail.*;
import javax.mail.internet.*;
import javax.xml.bind.annotation.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CustomerContactDetails
{
  private String firstName;
  private String lastName;
  private CustomerAddress address;
  private InternetAddress emailAddress;
  private CustomerPhoneNumber phoneNumber;
}
