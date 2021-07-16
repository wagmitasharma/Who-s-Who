package com.example.whoswho;

import com.example.whoswho.models.UserProfileInfo;
import com.example.whoswho.services.GetService;
import com.example.whoswho.services.UserProfileInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class Controller {

  @Autowired
  GetService getService;

  @Autowired
  UserProfileInfoService userProfileInfoService;

  @GetMapping("/getAll")
  public ResponseEntity<Object> getAll() {
    return new ResponseEntity<>(getService.getAll(), HttpStatus.ACCEPTED);
  }

  @GetMapping("/getUserProfile")
  public ResponseEntity<Object> getUserProfile() {
    return new ResponseEntity<>(userProfileInfoService.getUserProfileInfo(), HttpStatus.ACCEPTED);
  }

  @GetMapping("/healthCheck")
  public ResponseEntity<Object> healthCheck() {
    return new ResponseEntity<>("Health Check", HttpStatus.ACCEPTED);
  }
}
