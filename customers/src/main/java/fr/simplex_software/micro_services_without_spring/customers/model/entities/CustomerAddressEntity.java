package fr.simplex_software.micro_services_without_spring.customers.model.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "CUSTOMER_ADDRESSES")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class CustomerAddressEntity
{
  @Id
  @SequenceGenerator(name = "CUSTOMER_ADDRESS_ID_GENERATOR", sequenceName = "CUSTOMER_ADDRESS_SEQ")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CUSTOMER_ADDRESS_ID_GENERATOR")
  @Column(name = "CUSTOMER_ADDRESS_ID", unique = true, nullable = false, length = 5)
  private Long id;
  @Column(name = "STREET_NUMBER", nullable = false)
  private int number;
  @Column(name = "STREET_NAME", nullable = false, length = 30)
  private String street;
  @Column(name = "CITY", nullable = false, length = 20)
  private String city;
  @Column(name = "PROVINCE", nullable = false, length = 20)
  private String province;
  @Column(name = "ZIP_CODE", nullable = false, length = 5)
  private String zip;
  @Column(name = "COUNTRY", nullable = false, length = 20)
  private String country;
  @OneToOne
  @MapsId
  @JoinColumn(name = "CUSTOMER_CONTACT_DETAILS_ID")
  private CustomerContactDetailsEntity customerContactDetailsEntity;
}
