package com.example.whoswho.slackResponseDto;

import com.example.whoswho.models.Channels;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class SlackChannelDto extends Channels {

  @JsonProperty("topic")
  private Map<String, String> topic;

  public Map<String, String> getTopic() {
    return topic;
  }

  public void setTopic(Map topic) {
    this.topic = topic;
  }
}
