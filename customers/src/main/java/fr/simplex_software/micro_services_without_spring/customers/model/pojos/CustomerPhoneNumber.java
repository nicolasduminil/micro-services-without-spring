package fr.simplex_software.micro_services_without_spring.customers.model.pojos;

import fr.simplex_software.micro_services_without_spring.customers.model.entities.*;
import lombok.*;
import org.eclipse.microprofile.openapi.annotations.media.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Schema
public class CustomerPhoneNumber
{
  @Schema(example = "33", required = true)
  private int area;
  @Schema(example = "01", required = true)
  private int exchange;
  @Schema(example = "6172", required = true)
  private int extension;
  @OneToOne
  @MapsId
  @JoinColumn(name = "CUSTOMER_CONTACT_DETAILS_ID")
  private CustomerContactDetailsEntity customerContactDetailsEntity;
}
