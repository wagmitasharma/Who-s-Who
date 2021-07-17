package com.example.whoswho.services;


import com.example.whoswho.models.Channels;
import com.example.whoswho.slackResponseDto.Conversations;
import com.example.whoswho.slackResponseDto.SlackChannelDto;
import com.slack.api.Slack;
import com.slack.api.SlackConfig;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.response.users.UsersConversationsResponse;
import com.slack.api.model.Conversation;
import com.slack.api.model.ConversationType;
import com.slack.api.util.http.listener.ResponsePrettyPrintingListener;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
public class UserGetConversations {

  /*
      use the api as:
      Conversations conversations =  new UserGetConversations().getAllUserConversations(userId, utils.getToken());
      System.out.println(conversations);
      userProfileInfo.setConversations(conversations);

   */

    public Conversations getAllUserConversations(String userId, String token) {

        Conversations conversations = new Conversations();
        List<Conversation> userConversation = getUserConversations(userId, token);
        List<SlackChannelDto> channels = new ArrayList<>();

        userConversation.stream().forEach(c -> {
            SlackChannelDto channel = new SlackChannelDto();
            channel.setId(c.getId());
            channel.setDesc(c.getTopic().getValue());
            channel.setName(c.getName());

            channels.add(channel);

        });
        conversations.setChannels(channels);

        return conversations;
    }

    private List<Conversation> getUserConversations(String userId, String token) {

        SlackConfig config = new SlackConfig();
        config.getHttpClientResponseHandlers().add(new ResponsePrettyPrintingListener());

        var client = Slack.getInstance(config).methods();
        var logger = LoggerFactory.getLogger("my-awesome-slack-app");
        UsersConversationsResponse result = null;

        List<ConversationType> conversationTypeList = new ArrayList<>();
        conversationTypeList.add(ConversationType.PUBLIC_CHANNEL);
        conversationTypeList.add(ConversationType.PRIVATE_CHANNEL);

        try {

            result = client.usersConversations(r -> r
                    .excludeArchived(true)
                    .limit(500)
                    .types(conversationTypeList)
                    .token(token)
                    .user(userId)
            );
        } catch (IOException | SlackApiException e) {
            logger.error("error: {}", e.getMessage(), e);
        }

        return result.getChannels();
    }

}
