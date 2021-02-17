package fr.simplex_software.micro_services_without_spring.customers.model.entities;

import lombok.*;

import javax.mail.internet.*;
import javax.persistence.*;

@Entity
@Table(name = "CUSTOMER_CONTACT_DETAILS")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class CustomerContactDetailsEntity
{
  @Id
  @SequenceGenerator(name = "CUSTOMER_CONTACT_DETAILS_ID_GENERATOR", sequenceName = "CUSTOMER_CONTACT_DETAILS_SEQ")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CUSTOMER_CONTACT_DETAILS_ID_GENERATOR")
  @Column(name = "CUSTOMER_CONTACT_DETAILS_ID", unique = true, nullable = false, length = 8)
  private Long id;
  @Column(name = "FIRST_NAME", nullable = false, length = 20)
  private String firstName;
  @Column(name = "LAST_NAME", nullable = false, length = 40)
  private String lastName;
  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "CUSTOMER_ADDRESS_ID", referencedColumnName = "CUSTOMER_ADDRESS_ID")
  private CustomerAddressEntity address;
  @Column(name = "EMAIL_ADDRESS", nullable = false)
  private String emailAddress;
  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "CUSTOMER_PHONE_NUMBER_ID", referencedColumnName = "CUSTOMER_PHONE_NUMBER_ID")
  private CustomerPhoneNumberEntity phoneNumber;
  @OneToOne(mappedBy = "contactDetails")
  private CustomerEntity customer;
}
