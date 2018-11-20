package com.strongfriends.async.handler;

import com.alibaba.fastjson.JSON;
import com.strongfriends.async.EventHandler;
import com.strongfriends.async.EventModel;
import com.strongfriends.async.EventType;
import com.strongfriends.model.*;
import com.strongfriends.service.LikeService;
import com.strongfriends.service.MessageService;
import com.strongfriends.service.NewsService;
import com.strongfriends.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DisLikeHandler implements EventHandler {

    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @Autowired
    NewsService newsService;

    @Autowired
    LikeService likeService;

    @Override
    public void doHandle(EventModel model) {
        Like like = JSON.parseObject(model.getExt("dislike"), Like.class);
        long likeCount = likeService.disLike(like.getUserId(), like.getEntityType(), like.getEntityId());
        newsService.updateLikeCount(like.getEntityId(), (int) likeCount);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.DISLIKE);
    }
}
