package com.rbn.blockchain.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Service
public class DefaultNodeService {

  private final Set<String> nodes = new HashSet<>();

  @Autowired
  private HttpServletRequest httpServletRequest;

  public void register(String nodeUrl) {
    String currentServerName =
        String.format("%s:%d", httpServletRequest.getServerName(), httpServletRequest.getServerPort());
    if (nodeUrl.contains(currentServerName)) {
      return;
    }
    nodes.add(nodeUrl);
  }

  public void register(List<String> nodesUrl) {
    nodesUrl.forEach(this::register);
  }

}
