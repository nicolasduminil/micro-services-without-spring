package fr.simplex_software.micro_services_without_spring.customers.model.pojos;

import lombok.*;
import org.eclipse.microprofile.openapi.annotations.media.*;

import javax.mail.internet.*;
import javax.validation.*;
import javax.validation.constraints.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Schema
public class CustomerContactDetails
{
  @NotEmpty
  @Size(min = 1, max = 20, message="{CUSTOMER_CONTACT_DETAILS_FIRST_NAME_INVALID_SIZE}")
  @Schema(example = "Emory", required = true, minLength = 1, maxLength = 20)
  private String firstName;
  @NotEmpty
  @Size(min = 1, max = 40, message="{CUSTOMER_CONTACT_DETAILS_LAST_NAME_INVALID_SIZE}")
  @Schema(example = "Barton", required = true, minLength = 1, maxLength = 40)
  private String lastName;
  @NotNull
  @Valid
  private CustomerAddress address;
  @NotNull
  @Schema(example = "emory.barton@cocoks.com", required = true)
  private InternetAddress emailAddress;
  @NotNull
  @Valid
  private CustomerPhoneNumber phoneNumber;
}
