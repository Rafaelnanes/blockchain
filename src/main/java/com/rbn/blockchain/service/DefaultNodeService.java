package com.rbn.blockchain.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Getter
@Service
public class DefaultNodeService {

  private final Set<String> nodes = new HashSet<>();

  @Autowired
  private HttpServletRequest httpServletRequest;

  public Set<String> register(String nodeUrl) {
    String currentServerName = getServerUrl();
    if (nodeUrl.equals(currentServerName)) {
      return nodes;
    }
    nodes.add(nodeUrl);
    registerBroadcast();
    return nodes;
  }

  public Set<String> register(List<String> nodesUrl) {
    Set<String> collect = nodesUrl.stream()
                                  .filter(x -> !x.equals(getServerUrl()))
                                  .collect(Collectors.toSet());
    nodes.addAll(collect);
    return nodes;
  }

  private void registerBroadcast() {
    Set<String> collect = new HashSet<>(nodes);
    collect.add(getServerUrl());
    for (String node : nodes) {
      try {
        String url = node + "/nodes/bulk";
        ResponseEntity<Void> voidResponseEntity =
            new RestTemplate().postForEntity(URI.create(url), collect, Void.class);
        log.info("Requesting to: {}, Response: {}", url, voidResponseEntity.getStatusCode());
      } catch (Exception e) {
        log.warn("Error registering node: {}: {}", node, e.getMessage());
      }
    }

  }

  private String getServerUrl() {
    return String.format("http://%s:%d", httpServletRequest.getServerName(), httpServletRequest.getServerPort());
  }

}
