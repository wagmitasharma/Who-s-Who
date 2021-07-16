package com.example.whoswho.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Conversations {

  private List<Channels> channels;

  public List<Channels> getChannels() {
    return channels;
  }

  public void setChannels(List<Channels> channels) {
    this.channels = channels;
  }
}
