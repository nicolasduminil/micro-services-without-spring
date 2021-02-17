package fr.simplex_software.micro_services_without_spring.customers.model.entities;

import fr.simplex_software.micro_services_without_spring.customers.model.pojos.*;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "CUSTOMERS")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class CustomerEntity
{
  @Id
  @SequenceGenerator(name = "CUSTOMERS_ID_GENERATOR", sequenceName = "CUSTOMERS_SEQ")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CUSTOMERS_ID_GENERATOR")
  @Column(name = "CUSTOMER_ID", unique = true, nullable = false, length = 8)
  private Long id;
  @Column(name = "CUSTOMER_REFERENCE", unique = true, nullable = false, length = 10)
  private String customerRef;
  @Enumerated
  @Column(name = "CUSTOMER_TYPE", nullable = false, length = 10)
  private CustomerType customerType;
  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "CUSTOMER_CONTACT_DETAILS_ID", referencedColumnName = "CUSTOMER_CONTACT_DETAILS_ID")
  private CustomerContactDetailsEntity contactDetails;
}
