package com.example.whoswho.response;

import com.example.whoswho.models.Channels;
import com.example.whoswho.models.UserProfileInfo;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ChannelDto extends Channels {

  @JsonProperty("members")
  private List<UserProfileInfo> members;

  public List<UserProfileInfo> getMembers() {
    return members;
  }

  public void setMembers(List<UserProfileInfo> members) {
    this.members = members;
  }
}
