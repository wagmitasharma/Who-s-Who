package com.example.whoswho.services;

import com.example.whoswho.Utils;
import com.example.whoswho.response.ChannelDto;
import com.example.whoswho.slackResponseDto.Conversations;
import com.example.whoswho.slackResponseDto.SlackChannelDto;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

@Service
public class GetService {

  @Autowired
  RestTemplate restTemplate;
  @Autowired
  Utils utils;

  static String publicChannel = "https://slack.com/api/conversations.list?limit=1000&pretty=1&exclude_archived=true";
  static String privateChannel = "https://slack.com/api/conversations.list?limit=1000&types=private_channel&pretty=1&exclude_archived=true";
  static String channelMembers = "";
  static String getMember = "";

  public List<ChannelDto> getAll() {
    List<ChannelDto> channelDtoList = getListOfChannels();
    List<ChannelDto> teamChannelList = channelDtoList.stream().filter(c -> c.getName().matches("(.*)team(.*)"))
      .collect(Collectors.toList());
    List<ChannelDto> deptChannelList = channelDtoList.stream().filter(c -> c.getName().matches("(.*)dept(.*)"))
      .collect(Collectors.toList());
    List<ChannelDto> projectChannelList = channelDtoList.stream().filter(c -> c.getName().matches("(.*)project(.*)"))
      .collect(Collectors.toList());
    return teamChannelList;
  }

  private List<ChannelDto> getListOfChannels() {

    String jsonStringOfPublic = getPublicChannelJsonString();
    String jsonStringOfPrivate = getPrivateChannelJsonString();
    String jsonString;
    if (jsonStringOfPrivate.length()>2) {
      jsonString = jsonStringOfPublic.substring(0, jsonStringOfPublic.length() - 1) + ',' +
        jsonStringOfPrivate.substring(1);
    } else jsonString = jsonStringOfPublic;
    jsonString = "{\"channels\":" + jsonString + "}";
    Conversations conversations = new Gson().fromJson(jsonString, Conversations.class);

    List<ChannelDto> channelDtoList = new ArrayList<>();
    conversations.getChannels().forEach(c -> channelDtoList.add(getChannelDto(c)));

    return channelDtoList;
  }

  private String getPublicChannelJsonString() {
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + utils.getToken());
    HttpEntity entity = new HttpEntity(headers);
    Object arrayList =  restTemplate.exchange(publicChannel,
      HttpMethod.GET, entity, ConcurrentMap.class).getBody().get("channels");

    GsonBuilder gsonBuilder = new GsonBuilder();
    Gson gson = gsonBuilder.create();
    return gson.toJson(arrayList);
  }

  private String getPrivateChannelJsonString() {
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + utils.getToken());
    HttpEntity entity = new HttpEntity(headers);
    Object arrayList =  restTemplate.exchange(privateChannel,
      HttpMethod.GET, entity, ConcurrentMap.class).getBody().get("channels");

    GsonBuilder gsonBuilder = new GsonBuilder();
    Gson gson = gsonBuilder.create();
    return gson.toJson(arrayList);
  }

  private ChannelDto getChannelDto(SlackChannelDto slackChannelDto) {
    ChannelDto channelDto = new ChannelDto();
    channelDto.setName(slackChannelDto.getName());
    channelDto.setId(slackChannelDto.getId());
    channelDto.setDesc(slackChannelDto.getTopic().get("value"));
    return channelDto;
  }
}
