package com.boardgamegeek.geekplay.plays;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import li.naska.bgg.exception.BggAuthenticationRequiredError;
import li.naska.bgg.exception.BggBadRequestError;
import li.naska.bgg.exception.BggResourceNotFoundError;
import li.naska.bgg.exception.BggResourceNotOwnedError;
import li.naska.bgg.security.BggAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static java.util.Collections.singletonList;

@Service
public class PlaysService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${bgg.api.geekplay}")
    private String geekplayEndpoint;

    private static BggAuthenticationToken getAuthentication() {
        return (BggAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
    }

    public Play savePlay(String username, Play play) {
        BggAuthenticationToken auth = getAuthentication();
        if (!auth.getPrincipal().equals(username)) {
            throw new BggResourceNotOwnedError("resource belongs to another user");
        }
        ObjectNode node = new ObjectMapper().valueToTree(play);
        node.put("ajax", 1);
        node.put("action", "save");
        node.put("version", 2);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(singletonList(MediaType.APPLICATION_JSON));
        headers.set("Cookie", auth.buildBggRequestHeader());
        HttpEntity<ObjectNode> request = new HttpEntity<>(node, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(geekplayEndpoint, request, String.class);
        Configuration conf = Configuration.defaultConfiguration().addOptions(Option.DEFAULT_PATH_LEAF_TO_NULL);
        DocumentContext documentContext = JsonPath.using(conf).parse(response.getBody());
        Integer playId = documentContext.read("$.playid", Integer.class);
        if (playId != null) {
            play.setObjectid(playId);
            return play;
        }
        String error = documentContext.read("$.error");
        if ("you must login to save plays".equals(error)) {
            throw new BggAuthenticationRequiredError(error);
        } else {
            throw new BggBadRequestError(Optional.ofNullable(error).orElse("unknown"));
        }
    }

    public void deletePlay(String username, Integer playId) {
        BggAuthenticationToken auth = getAuthentication();
        if (!auth.getPrincipal().equals(username)) {
            throw new BggResourceNotOwnedError("resource belongs to another user");
        }
        ObjectNode node = new ObjectMapper().createObjectNode();
        node.put("action", "delete");
        node.put("finalize", "1");
        node.put("playid", playId);
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.setAccept(singletonList(MediaType.APPLICATION_JSON));
        requestHeaders.set("Cookie", auth.buildBggRequestHeader());
        HttpEntity<ObjectNode> request = new HttpEntity<>(node, requestHeaders);
        ResponseEntity<String> response = restTemplate.postForEntity(geekplayEndpoint, request, String.class);
        if (response.getStatusCode() != HttpStatus.FOUND) {
            throw new BggResourceNotFoundError("play not found");
        }
    }

}
