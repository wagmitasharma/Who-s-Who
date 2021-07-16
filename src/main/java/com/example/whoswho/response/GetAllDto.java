package com.example.whoswho.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class GetAllDto {

  @JsonProperty("teams")
  private List<ChannelDto> teams;
  @JsonProperty("dept")
  private List<ChannelDto> dept;
  @JsonProperty("projects")
  private List<ChannelDto> projects;

  public List<ChannelDto> getTeams() {
    return teams;
  }

  public void setTeams(List<ChannelDto> teams) {
    this.teams = teams;
  }

  public List<ChannelDto> getDept() {
    return dept;
  }

  public void setDept(List<ChannelDto> dept) {
    this.dept = dept;
  }

  public List<ChannelDto> getProjects() {
    return projects;
  }

  public void setProjects(List<ChannelDto> projects) {
    this.projects = projects;
  }
}
