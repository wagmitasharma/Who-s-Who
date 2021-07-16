package com.example.whoswho.services;

import com.example.whoswho.Utils;
import com.example.whoswho.models.Conversations;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

@Service
public class GetService {

  @Autowired
  RestTemplate restTemplate;
  @Autowired
  Utils utils;

  static String publicChannel = "https://slack.com/api/conversations.list";
  static String privateChannel = "https://slack.com/api/admin.usergroups.listChannels";
  static String channelMembers = "";
  static String getMember = "";

  public Conversations getAll() {

    String jsonString = getPublicChannelJsonString().substring(-1) + getPrivateChannelJsonString().substring(1);
    jsonString = "{\"channels\":" + jsonString + "}";
    Conversations conversations = new Gson().fromJson(jsonString, Conversations.class);

    conversations.getChannels().stream().forEach(c -> c.setDesc(c.getTopic().get("value")));
    return conversations;
  }

  private String getPublicChannelJsonString() {
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + utils.getToken());
    HttpEntity entity = new HttpEntity(headers);

    Map<String, String> params = new HashMap<>();
    params.put("limit","500");
    params.put("pretty","1");
    Object arrayList =  restTemplate.exchange(publicChannel,
      HttpMethod.GET, entity, ConcurrentMap.class, params).getBody().get("channels");

    GsonBuilder gsonBuilder = new GsonBuilder();
    Gson gson = gsonBuilder.create();
    return gson.toJson(arrayList);
  }

  private String getPrivateChannelJsonString() {
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + utils.getToken());
    HttpEntity entity = new HttpEntity(headers);

    Map<String, String> params = new HashMap<>();
    params.put("limit","500");
    params.put("pretty","1");
    Object arrayList =  restTemplate.exchange(privateChannel,
      HttpMethod.GET, entity, ConcurrentMap.class, params).getBody().get("channels");

    GsonBuilder gsonBuilder = new GsonBuilder();
    Gson gson = gsonBuilder.create();
    return gson.toJson(arrayList);
  }
}
