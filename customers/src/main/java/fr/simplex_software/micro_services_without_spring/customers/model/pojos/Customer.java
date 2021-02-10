package fr.simplex_software.micro_services_without_spring.customers.model.pojos;

import lombok.*;
import org.eclipse.microprofile.openapi.annotations.media.*;

import javax.validation.*;
import javax.validation.constraints.*;
import javax.xml.bind.annotation.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@XmlRootElement
@XmlAccessorType (XmlAccessType.FIELD)
@Schema
public class Customer
{
  @XmlElement
  @NotNull
  @NotEmpty
  @Size(min = 1, max = 10, message="{CUSTOMER_REF_INVALID_SIZE}")
  @Schema(example = "Customer1", required = true, minLength = 1, maxLength = 10)
  private String customerRef;
  @XmlElement
  @NotNull
  @Schema(example = "LOYAL", required = true)
  private CustomerType customerType;
  @XmlElement
  @NotNull
  @Valid
  private CustomerContactDetails contactDetails;
}
