package com.example.whoswho.services;

import com.example.whoswho.Utils;
import com.example.whoswho.models.UserProfileInfo;
import com.slack.api.Slack;
import com.slack.api.SlackConfig;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.response.users.profile.UsersProfileGetResponse;
import com.slack.api.model.User;
import com.slack.api.util.http.listener.ResponsePrettyPrintingListener;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;


@Service
public class UserProfileInfoService {

  @Autowired
  RestTemplate restTemplate;
  @Autowired
  Utils utils;

  static String userId = "UEE5V9B4M";

  public UserProfileInfo getUserProfileInfo() {

    User.Profile userProfile = getUserProfileJsonString() ;
    UserProfileInfo userProfileInfo = new UserProfileInfo();
    userProfileInfo.setId(userId);
    userProfileInfo.setName(userProfile.getRealName());
    userProfileInfo.setEmail(userProfile.getEmail());
    userProfileInfo.setPhone(userProfile.getPhone());
    userProfileInfo.setImage(userProfile.getImageOriginal());

    return userProfileInfo;
  }

  private User.Profile getUserProfileJsonString() {



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
