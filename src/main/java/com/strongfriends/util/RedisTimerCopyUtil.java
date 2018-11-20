package com.strongfriends.util;

import com.strongfriends.dao.NewsDAO;
import com.strongfriends.model.News;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 这个Util类只同步Like
 */

@Component
public class RedisTimerCopyUtil {

    @Autowired
    NewsDAO newsDAO;

    @Autowired
    JedisAdapter jedisAdapter;

    // 每30分钟同步一次
    @Scheduled(fixedRate = 1000 * 60 * 30)
    public void timer() {
//        List<News> newsList = new ArrayList<News>(newsDAO.getAllNews());
//        for (News news : newsList) {
//            int id = news.getId();
//            int count = (int) jedisAdapter.scard("LIKE:1:" + String.valueOf(id));
//            newsDAO.updateLikeCount(id, count);
//        }


    }

}
