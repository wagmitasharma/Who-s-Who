package com.example.whoswho.services;

import com.example.whoswho.Utils;
import com.example.whoswho.models.UserProfileInfo;
import com.example.whoswho.response.ChannelDto;
import com.example.whoswho.response.GetAllDto;
import com.example.whoswho.slackResponseDto.Conversations;
import com.example.whoswho.slackResponseDto.SlackChannelDto;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class GetChannelDetailService {

  @Autowired
  UserProfileInfoService userProfileInfoService;
  @Autowired
  RestTemplate restTemplate;
  @Autowired
  Utils utils;

  static String publicChannel = "https://slack.com/api/conversations.list?limit=1000&pretty=1&exclude_archived=true";
  static String privateChannel = "https://slack.com/api/conversations.list?limit=1000&types=private_channel&pretty=1&exclude_archived=true";
  static String channelMembers = "https://slack.com/api/conversations.members?pretty=1&channel=";

  public GetAllDto getAll() {
    List<ChannelDto> channelDtoList = getListOfChannels();
    List<ChannelDto> teamChannelList = channelDtoList.stream().filter(c -> c.getName().matches("(.*)team(.*)"))
      .collect(Collectors.toList());
    List<ChannelDto> deptChannelList = channelDtoList.stream().filter(c -> c.getName().matches("(.*)dept(.*)" +
      "|(.*)flutter(.*)|(.*)android(.*)"))
      .collect(Collectors.toList());
    List<ChannelDto> projectChannelList = channelDtoList.stream().filter(c -> c.getName().matches("(.*)project(.*)" +
      "|(.*)rakbank(.*)|(.*)falcon(.*)"))
      .collect(Collectors.toList());

    CompletableFuture<List> teams = getTeamUsers(teamChannelList);
    CompletableFuture<List> dept = getDeptUsers(deptChannelList);
    CompletableFuture<List> projects = getProjectUsers(projectChannelList);
//    teamChannelList.stream().forEach(t -> t.setMembers(getMemeberOfChannel(t.getId())));
    GetAllDto getAllDto = new GetAllDto();
    try {
      getAllDto.setTeams(teams.get());
      getAllDto.setDept(dept.get());
      getAllDto.setProjects(projects.get());
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }
    return getAllDto;
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

  public List<UserProfileInfo> getMemberOfChannel(String channelId) {
      HttpHeaders headers = new HttpHeaders();
      headers.set("Authorization", "Bearer " + utils.getToken());
      HttpEntity entity = new HttpEntity(headers);
      Object arrayList = restTemplate.exchange(channelMembers + channelId,
        HttpMethod.GET, entity, ConcurrentMap.class).getBody().get("members");

      GsonBuilder gsonBuilder = new GsonBuilder();
      Gson gson = gsonBuilder.create();

      List memberIdList = new Gson().fromJson(gson.toJson(arrayList), List.class);

      List<UserProfileInfo> userProfileInfoList = new ArrayList<>();
      memberIdList.forEach(m -> userProfileInfoList.add((UserProfileInfo) userProfileInfoService.getUserProfileInfo(m.toString(), true)));

      return userProfileInfoList;
  }

  @Async
  private CompletableFuture<List> getTeamUsers(List<ChannelDto> teamChannelList) {
    return CompletableFuture.supplyAsync(() -> {
      teamChannelList.forEach(t -> t.setMembers(getMemberOfChannel(t.getId())));
      return teamChannelList;
    });
  }

  @Async
  private CompletableFuture<List> getProjectUsers(List<ChannelDto> projectChannelList) {
    return CompletableFuture.supplyAsync(() -> {
      projectChannelList.forEach(t -> t.setMembers(getMemberOfChannel(t.getId())));
      return projectChannelList;
    });
  }

  @Async
  private CompletableFuture<List> getDeptUsers(List<ChannelDto> deptChannelList) {
     return CompletableFuture.supplyAsync(() -> {
      deptChannelList.forEach(t -> t.setMembers(getMemberOfChannel(t.getId())));
      return deptChannelList;
    });
  }
}
