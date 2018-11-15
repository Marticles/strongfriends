package com.strongfriends.controller;

import com.strongfriends.async.EventModel;
import com.strongfriends.async.EventProducer;
import com.strongfriends.async.EventType;
import com.strongfriends.model.EntityType;
import com.strongfriends.model.HostHolder;
import com.strongfriends.model.News;
import com.strongfriends.service.LikeService;
import com.strongfriends.service.NewsService;
import com.strongfriends.util.StrongFriendsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LikeController {
    @Autowired
    LikeService likeService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    NewsService newsService;

    @Autowired
    EventProducer eventProducer;

    @RequestMapping(path = {"/like"}, method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String like(@RequestParam("newsId") int newsId) {
        News news = newsService.getById(newsId);
        if (hostHolder.getUser() == null) {
            return StrongFriendsUtil.getJSONString(1);
        }
        int userId = hostHolder.getUser().getId();
        long likeCount = likeService.like(userId, EntityType.ENTITY_POST, newsId);
        newsService.updateLikeCount(newsId, (int) likeCount);

        eventProducer.fireEvent(new EventModel(EventType.LIKE)
                .setEntityOwnerId(news.getUserId())
                .setActorId(hostHolder.getUser().getId()).setEntityId(newsId));

        return StrongFriendsUtil.getJSONString(0, String.valueOf(likeCount));
    }

    @RequestMapping(path = {"/dislike"}, method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String disLike(@RequestParam("newsId") int newsId) {
        if (hostHolder.getUser() == null) {
            return StrongFriendsUtil.getJSONString(1);
        }
        int userId = hostHolder.getUser().getId();
        long likeCount = likeService.disLike(userId, EntityType.ENTITY_POST, newsId);
        newsService.updateLikeCount(newsId, (int) likeCount);
        return StrongFriendsUtil.getJSONString(0, String.valueOf(likeCount));
    }
}