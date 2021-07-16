package com.example.whoswho.slackResponseDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Conversations {

  private List<SlackChannelDto> channels;

  public List<SlackChannelDto> getChannels() {
    return channels;
  }

  public void setChannels(List<SlackChannelDto> channels) {
    this.channels = channels;
  }
}
