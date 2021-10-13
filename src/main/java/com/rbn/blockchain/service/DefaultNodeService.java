package com.rbn.blockchain.service;

import com.rbn.blockchain.model.Blockchain;
import com.rbn.blockchain.model.wallet.Block;
import com.rbn.blockchain.model.wallet.Wallet;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Getter
@Service
public class DefaultNodeService {

  private final Set<String> nodes = new HashSet<>();

  private final Wallet nodeWallet = new Wallet();

  @Autowired
  private HttpServletRequest httpServletRequest;

  @Autowired
  private RestTemplate restTemplate;

  @Value("${current.host.url: }")
  private String currentHostUrl;

  public Set<String> registerBulk(String nodeUrl) {
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

  public Set<String> registerBulk(List<String> nodesUrl) {
    nodesUrl.remove(getServerUrl());
    nodes.addAll(nodesUrl);
    log.info("Nodes registered bulk: {}", nodes);
    return nodes;
  }

  public void mine(String data) {
    for (String node : nodes) {
      if (node.equals(getServerUrl())) {
        continue;
      }
      sendMineRequest(data, node);
    }
  }

  public void sendMineRequest(String data, String node) {
    try {
      String url = node + "/blocks/mine";
      log.info("Request to mine to: {}", url);
      ResponseEntity<Void> voidResponseEntity =
          restTemplate.postForEntity(URI.create(url), data, Void.class);
      log.info("Request to mine: {}, Response: {}", url, voidResponseEntity.getStatusCode());
    } catch (Exception e) {
      log.warn("Broadcast new block error to node: {}: {}", node, e.getMessage());
    }

  }

  public void newBlockBroadcast(Block block) {
    for (String node : nodes) {
      try {
        String url = node + "/blocks";
        log.info("Broadcasting new block to: {}", url);
        ResponseEntity<Void> voidResponseEntity =
            restTemplate.postForEntity(URI.create(url), block, Void.class);
        log.info("Broadcast new block to: {}, Response: {}", url, voidResponseEntity.getStatusCode());
      } catch (Exception e) {
        log.warn("Broadcast new block error to node: {}: {}", node, e.getMessage());
      }
    }
  }

  public List<Blockchain> updateBlockchain() {
    List<Blockchain> blockchainList = new ArrayList<>();
    for (String node : nodes) {
      try {
        String url = node + "/blockchain";
        ResponseEntity<Blockchain> responseEntity = restTemplate.getForEntity(URI.create(url), Blockchain.class);
        log.info("Request consensus to: {}, Response: {}", url, responseEntity.getStatusCode());
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
          blockchainList.add(responseEntity.getBody());
        }
      } catch (Exception e) {
        log.warn("Error getting consensus node: {}: {}", node, e.getMessage());
      }
    }
    return blockchainList;
  }


  private void registerBroadcast(String nodeUrl) {
    nodes.add(nodeUrl);
    for (String node : nodes) {
      try {
        HashSet<String> allNodes = new HashSet<>(nodes);
        allNodes.add(getServerUrl());
        allNodes.remove(node);

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
    return String.format("http://%s:%d", finalHostUrl, httpServletRequest.getServerPort());
  }

}
