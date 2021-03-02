package fr.simplex_software.micro_services_without_spring.customers.tests;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.mockito.junit.jupiter.*;

import javax.ws.rs.core.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TestUriBuilderProducer
{
  @InjectMocks
  @Spy
  private fr.simplex_software.micro_services_without_spring.customers.producer.UriBuilderProducer uriBuilderProducer ;
  @Mock
  private UriBuilder uriBuilder;

  @Test
  public void testUriBuilderProducer()
  {
    assertThat(uriBuilder).isNotNull();
    assertThat(uriBuilderProducer).isNotNull();
    when (uriBuilderProducer.getUriBuilder()).thenReturn(uriBuilder);
    assertThat (uriBuilderProducer.getUriBuilder()).isEqualTo(uriBuilder);
  }
}
