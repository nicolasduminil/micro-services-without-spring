package fr.simplex_software.micro_services_without_spring.customers.model.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "CUSTOMER_PHONE_NUMBERS")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class CustomerPhoneNumberEntity
{
  @Id
  @SequenceGenerator(name = "CUSTOMER_PHONE_NUMBER_ID_GENERATOR", sequenceName = "CUSTOMER_PHONE_NUMBER_SEQ")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CUSTOMER_PHONE_NUMBER_ID_GENERATOR")
  @Column(name = "CUSTOMER_PHONE_NUMBER_ID", unique = true, nullable = false, length = 8)
  private Long id;
  @Column(name = "AREA_CODE", nullable = false)
  private int area;
  @Column(name = "EXCHANGE", nullable = false)
  private int exchange;
  @Column(name = "EXTENSION", nullable = false)
  private int extension;
  @OneToOne(mappedBy = "phoneNumber")
  private CustomerContactDetailsEntity contactDetails;
}
