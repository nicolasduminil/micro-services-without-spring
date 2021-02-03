package fr.simplex_software.micro_services_without_spring.customers.model;

import lombok.*;

import javax.validation.constraints.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CustomerAddress
{
  @Min(value=1, message="{CUSTOMER_ADDRESS_NUMBER_TOO_LOW}")
  private int number;
  @NotEmpty
  @Size(min = 1, max = 30, message="{CUSTOMER_ADDRESS_STREET_INVALID_SIZE}")
  private String street;
  @NotEmpty
  @Size(min = 1, max = 20, message="{CUSTOMER_ADDRESS_CITY_INVALID_SIZE}")
  private String city;
  @NotEmpty
  @Size(min = 1, max = 20, message="{CUSTOMER_ADDRESS_PROVINCE_INVALID_SIZE}")
  private String province;
  @NotEmpty
  @Size(min = 1, max = 5, message="{CUSTOMER_ADDRESS_ZIP_INVALID_SIZE}")
  private String zip;
  @NotEmpty
  @Size(min = 1, max = 20, message="{CUSTOMER_ADDRESS_COUNTRY_INVALID_SIZE}")
  private String country;
}
