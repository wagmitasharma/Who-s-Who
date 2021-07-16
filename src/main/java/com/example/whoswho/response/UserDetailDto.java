package com.example.whoswho.response;

import com.example.whoswho.models.UserProfileInfo;
import com.example.whoswho.slackResponseDto.SlackChannelDto;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class UserDetailDto {

  @JsonProperty("user")
  private UserProfileInfo user;

  @JsonProperty("dept")
  private List<SlackChannelDto> dept;

  @JsonProperty("projects")
  private List<SlackChannelDto> projects;

  @JsonProperty("teamMembers")
  private List<UserProfileInfo> teamMembers;

  public List<SlackChannelDto> getDept() {
    return dept;
  }

  public void setDept(List<SlackChannelDto> dept) {
    this.dept = dept;
  }

  public List<SlackChannelDto> getProjects() {
    return projects;
  }

  public void setProjects(List<SlackChannelDto> projects) {
    this.projects = projects;
  }

  public List<UserProfileInfo> getTeamMembers() {
    return teamMembers;
  }

  public void setTeamMembers(List<UserProfileInfo> teamMembers) {
    this.teamMembers = teamMembers;
  }

  public UserProfileInfo getUser() {
    return user;
  }

  public void setUser(UserProfileInfo user) {
    this.user = user;
  }
}
