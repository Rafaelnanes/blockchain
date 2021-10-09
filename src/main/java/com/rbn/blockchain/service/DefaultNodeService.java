package com.rbn.blockchain.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Getter
@Service
public class DefaultNodeService {

  private final Set<String> nodes = new HashSet<>();

  @Autowired
  private HttpServletRequest httpServletRequest;

  @Autowired
  private RestTemplate restTemplate;

  @Value("${current.host.url: }")
  private String currentHostUrl;

  public Set<String> register(String nodeUrl) {
    String currentServerName = getServerUrl();
    if (nodeUrl.equals(currentServerName)) {
      log.info("Cannot self register: {}", nodeUrl);
      return nodes;
    }

    if (nodes.contains(nodeUrl)) {
      log.info("Node already exists: {}", nodeUrl);
      return nodes;
    }
    registerBroadcast(nodeUrl);
    log.info("Nodes registered: {}", nodes);
    return nodes;
  }

  public Set<String> register(List<String> nodesUrl) {
    nodesUrl.forEach(this::register);
    return nodes;
  }

  private void registerBroadcast(String nodeUrl) {
    nodes.add(nodeUrl);
    for (String node : nodes) {
      try {
        HashSet<String> allNodes = new HashSet<>(nodes);
        allNodes.add(getServerUrl());

        String url = node + "/nodes/bulk";
        ResponseEntity<Void> voidResponseEntity =
            restTemplate.postForEntity(URI.create(url), allNodes, Void.class);
        log.info("Requesting to: {}, Response: {}", url, voidResponseEntity.getStatusCode());
      } catch (Exception e) {
        log.warn("Error registering node: {}: {}", node, e.getMessage());
        nodes.remove(node);
      }
    }
  }

  private String getServerUrl() {
    String finalHostUrl = StringUtils.hasText(currentHostUrl) ? currentHostUrl : httpServletRequest.getServerName();
    String currentServerUrl =
        String.format("http://%s:%d", finalHostUrl, httpServletRequest.getServerPort());
    log.info("Final host url: {}", finalHostUrl);
    return currentServerUrl;
  }

}
