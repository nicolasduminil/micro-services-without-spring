package fr.simplex_software.micro_services_without_spring.customers.model.pojos;

import lombok.*;
import org.eclipse.microprofile.openapi.annotations.media.*;

import javax.validation.constraints.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Schema
public class CustomerAddress
{
  @Min(value=1, message="{CUSTOMER_ADDRESS_NUMBER_TOO_LOW}")
  @Schema(example = "26", required = true)
  private int number;
  @NotEmpty
  @Size(min = 1, max = 30, message="{CUSTOMER_ADDRESS_STREET_INVALID_SIZE}")
  @Schema(example = "Sawayn Brooks", required = true, minLength = 1, maxLength = 30)
  private String street;
  @NotEmpty
  @Size(min = 1, max = 20, message="{CUSTOMER_ADDRESS_CITY_INVALID_SIZE}")
  @Schema(example = "Kedham", required = true, minLength = 1, maxLength = 20)
  private String city;
  @NotEmpty
  @Size(min = 1, max = 20, message="{CUSTOMER_ADDRESS_PROVINCE_INVALID_SIZE}")
  @Schema(example = "Alsace", required = true, minLength = 1, maxLength = 20)
  private String province;
  @NotEmpty
  @Size(min = 1, max = 5, message="{CUSTOMER_ADDRESS_ZIP_INVALID_SIZE}")
  @Schema(example = "60018", required = true, minLength = 1, maxLength = 5)
  private String zip;
  @NotEmpty
  @Size(min = 1, max = 20, message="{CUSTOMER_ADDRESS_COUNTRY_INVALID_SIZE}")
  @Schema(example = "America", required = true, minLength = 1, maxLength = 20)
  private String country;
}
