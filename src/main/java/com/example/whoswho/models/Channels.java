package com.example.whoswho.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Channels {

  @JsonProperty("id")
  private String id;

  @JsonProperty("name")
  private String name;

  @JsonProperty("is_archived")
  private String is_archived;

  @JsonProperty("desc")
  private String desc;

  @JsonProperty("topic")
  private Map<String, String> topic;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getIs_archived() {
    return is_archived;
  }

  public void setIs_archived(String is_archived) {
    this.is_archived = is_archived;
  }

  public Object getDesc() {
    return desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Map<String, String> getTopic() {
    return topic;
  }

  public void setTopic(Map topic) {
    this.topic = topic;
  }
}
