package fr.simplex_software.micro_services_without_spring.customers.model;

import lombok.*;

import javax.mail.internet.*;
import javax.validation.*;
import javax.validation.constraints.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CustomerContactDetails
{
  @NotEmpty
  @Size(min = 1, max = 20, message="{CUSTOMER_CONTACT_DETAILS_FIRST_NAME_INVALID_SIZE}")
  private String firstName;
  @NotEmpty
  @Size(min = 1, max = 40, message="{CUSTOMER_CONTACT_DETAILS_LAST_NAME_INVALID_SIZE}")
  private String lastName;
  @NotNull
  @Valid
  private CustomerAddress address;
  @NotNull
  private InternetAddress emailAddress;
  @NotNull
  @Valid
  private CustomerPhoneNumber phoneNumber;
}
