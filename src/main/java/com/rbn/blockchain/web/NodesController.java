package com.rbn.blockchain.web;

import com.rbn.blockchain.model.NodeRequest;
import com.rbn.blockchain.service.DefaultNodeService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/nodes")
@AllArgsConstructor
public class NodesController {

  private final DefaultNodeService nodeService;

  @PostMapping
  public void post(@RequestBody NodeRequest nodeRequest) {
    nodeService.register(nodeRequest.getNodeUrl());
  }

  @PostMapping("/bulk")
  public void registerBulk(@RequestBody List<String> nodesUrl) {
    nodeService.register(nodesUrl);
  }

}
