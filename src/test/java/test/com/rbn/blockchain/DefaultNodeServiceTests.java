package test.com.rbn.blockchain;

import com.rbn.blockchain.service.DefaultNodeService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@ContextConfiguration(classes = DefaultNodeServiceTests.Config.class)
@ExtendWith(SpringExtension.class)
public class DefaultNodeServiceTests {

  @Autowired
  private DefaultNodeService defaultNodeService;

  @Test
  void simpleCreation() {
    defaultNodeService.registerBulk("http://localhost:8081");
    defaultNodeService.registerBulk("http://localhost:8082");
    Set<String> nodes = defaultNodeService.registerBulk("http://localhost:80");
    Assertions.assertFalse(nodes.isEmpty());
    Assertions.assertEquals(2, nodes.size());
    Assertions.assertTrue(nodes.contains("http://localhost:8081"));
    Assertions.assertTrue(nodes.contains("http://localhost:8082"));
  }

  @Test
  void bulk() {
    List<String> list = new ArrayList<>();
    list.add("http://localhost:8081");
    list.add("http://localhost:8082");
    list.add("http://localhost:80");
    Set<String> nodes = defaultNodeService.registerBulk(list);
    Assertions.assertFalse(nodes.isEmpty());
    Assertions.assertEquals(2, nodes.size());
    Assertions.assertTrue(nodes.contains("http://localhost:8081"));
    Assertions.assertTrue(nodes.contains("http://localhost:8082"));
  }

  @Configuration
  @Import(DefaultNodeService.class)
  public static class Config {

    @Bean
    public HttpServletRequest getHttpServletRequest() {
      return new MockHttpServletRequest();
    }

    @Bean
    public RestTemplate getRestTemplate() {
      RestTemplate restTemplate = Mockito.mock(RestTemplate.class);
      ResponseEntity<Object> responseEntity = new ResponseEntity<>(HttpStatus.OK);
      Mockito.when(restTemplate.postForEntity(Mockito.any(URI.class), Mockito.any(Set.class), Mockito.any()))
             .thenReturn(responseEntity);
      return restTemplate;
    }

  }

}
