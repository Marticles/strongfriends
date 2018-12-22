package com.strongfriends.async.handler;

import com.alibaba.fastjson.JSON;
import com.strongfriends.async.EventHandler;
import com.strongfriends.async.EventModel;
import com.strongfriends.async.EventType;
import com.strongfriends.model.*;
import com.strongfriends.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.util.Arrays;
import java.util.List;

@Component
public class NewsHandler implements EventHandler {

    @Autowired
    NewsService newsService;


    @Override
    public void doHandle(EventModel model) {
        News news = JSON.parseObject(model.getExt("news"), News.class);
        newsService.addNews(news);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.POST);
    }
}



