package com.example.whoswho.services;

import com.example.whoswho.Utils;
import com.example.whoswho.models.UserProfileInfo;
import com.example.whoswho.response.ChannelDto;
import com.example.whoswho.response.UserDetailDto;
import com.example.whoswho.slackResponseDto.Conversations;
import com.example.whoswho.slackResponseDto.SlackChannelDto;
import com.slack.api.Slack;
import com.slack.api.SlackConfig;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.response.users.profile.UsersProfileGetResponse;
import com.slack.api.model.User;
import com.slack.api.util.http.listener.ResponsePrettyPrintingListener;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserProfileInfoService {

  @Autowired
  Utils utils;
  @Autowired
  GetChannelDetailService getChannelDetailService;
  @Autowired
  UserGetConversations userGetConversations;

  public Object getUserProfileInfo(String userId, boolean isTeamMember) {

    User.Profile userProfile = getUserProfileJsonString(userId) ;
    UserProfileInfo userProfileInfo = new UserProfileInfo();
    userProfileInfo.setId(userId);
    userProfileInfo.setName(userProfile.getRealName());
    userProfileInfo.setEmail(userProfile.getEmail());
    userProfileInfo.setPhone(userProfile.getPhone());
    userProfileInfo.setImage(userProfile.getImage192());
    userProfileInfo.setImageHd(userProfile.getImage1024());
    if (userProfileInfo.getImageHd()==null)
      userProfileInfo.setImageHd(userProfileInfo.getImage());
    UserDetailDto userDetailDto =  new UserDetailDto();
    userDetailDto.setUser(userProfileInfo);
    if (!isTeamMember){
      Conversations conversations =  new UserGetConversations().getAllUserConversations(userId, utils.getToken());

      userDetailDto.setProjects(conversations.getChannels().stream().filter(c -> c.getName().matches("(.*)project(.*)" +
        "|(.*)rakbank(.*)|(.*)falcon(.*)"))
        .collect(Collectors.toList()));
      userDetailDto.setDept(conversations.getChannels().stream().filter(c -> c.getName().matches("(.*)dept(.*)" +
        "|(.*)flutter(.*)|(.*)android(.*)"))
        .collect(Collectors.toList()));
      List<SlackChannelDto> userTeam = conversations.getChannels().stream().filter(c -> c.getName().matches("(.*)team(.*)"))
        .collect(Collectors.toList());
      if(userTeam.size()!=0) {
        userDetailDto.setTeamMembers(getChannelDetailService.getMemberOfChannel(userTeam.get(0).getId()));
        userDetailDto.getTeamMembers().removeIf(t -> t.getId().equals(userId));
      }
      return userDetailDto;
    }
    return userProfileInfo;
  }

  private User.Profile getUserProfileJsonString(String userId) {

    SlackConfig config = new SlackConfig();
    config.getHttpClientResponseHandlers().add(new ResponsePrettyPrintingListener());

    var client = Slack.getInstance(config).methods();
    var logger = LoggerFactory.getLogger("my-awesome-slack-app");
    UsersProfileGetResponse result = null;
    try {

      result = client.usersProfileGet(r -> r
              .user(userId)
              .token(utils.getToken())
      );
    } catch (IOException | SlackApiException e) {
      logger.error("error: {}", e.getMessage(), e);
    }

    return result.getProfile();
  }
}
