package test.com.rbn.blockchain;

import com.rbn.blockchain.service.DefaultNodeService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import test.com.rbn.blockchain.util.ConfigTests;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Configuration
@Import({DefaultNodeService.class})
@ContextConfiguration(classes = ConfigTests.class)
@ExtendWith(SpringExtension.class)
public class DefaultNodeServiceTests {

  @Autowired
  private DefaultNodeService defaultNodeService;

  @Test
  void simple_creation() {
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

}
