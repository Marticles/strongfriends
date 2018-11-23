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

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
public class LikeHandler implements EventHandler {

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
        News news = newsService.getById(model.getEntityId());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Message message = new Message();
        // 99999为Admin账号
        message.setFromId(99999);
        User user = userService.getUser(model.getActorId());
        message.setToId(model.getEntityOwnerId());
        message.setContent("用户 " + user.getName() +
                " 于 " + String.valueOf(formatter.format(new Date())) + " 赞了你的帖子："
                + String.valueOf(news.getTitle()));
        message.setConversationId(99999 < message.getToId() ? String.format("%d_%d", 99999, message.getToId()) :
                String.format("%d_%d", message.getToId(), 99999));
        message.setCreatedDate(new Date());
        messageService.addMessage(message);
        Like like = JSON.parseObject(model.getExt("like"), Like.class);

        long[] arr = likeService.like(like.getUserId(), like.getEntityType(), like.getEntityId());
        newsService.updateLikeCount(like.getEntityId(), (int) arr[0]);
        newsService.updateDisLikeCount(like.getEntityId(), (int) arr[1]);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LIKE);
    }
}
