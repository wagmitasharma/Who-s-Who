package com.example.whoswho.models;

import com.example.whoswho.slackResponseDto.SlackChannelDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserProfileInfo {


  @JsonProperty("id")
  private String id;

  @JsonProperty("name")
  private String name;

  @JsonProperty("phone")
  private String phone;

  @JsonProperty("email")
  private String email;

  @JsonProperty("image")
  private String image;
  @JsonProperty("imageHd")
  private String imageHd;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getPhone() { return phone; }

  public void setPhone(String phone) { this.phone = phone; }

  public String getEmail() { return email; }

  public void setEmail(String email) { this.email = email; }

  public String getImage() { return image; }

  public void setImage(String image) { this.image = image; }

  public String getImageHd() {
    return imageHd;
  }

  public void setImageHd(String imageHd) {
    this.imageHd = imageHd;
  }
}
