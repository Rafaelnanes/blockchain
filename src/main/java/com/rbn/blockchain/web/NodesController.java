package com.rbn.blockchain.web;

import com.rbn.blockchain.model.NodeRequest;
import com.rbn.blockchain.service.DefaultNodeService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/nodes")
@AllArgsConstructor
public class NodesController {

  private final DefaultNodeService nodeService;

  @GetMapping
  public Set<String> get() {
    return nodeService.getNodes();
  }

  @PostMapping
  public Set<String> post(@RequestBody NodeRequest nodeRequest) {
    return nodeService.register(nodeRequest.getNodeUrl());
  }

  @PostMapping("/bulk")
  public Set<String> registerBulk(@RequestBody List<String> nodesUrl) {
    return nodeService.register(nodesUrl);
  }

}
