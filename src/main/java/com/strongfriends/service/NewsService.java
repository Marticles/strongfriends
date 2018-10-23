package com.strongfriends.service;

import com.strongfriends.dao.NewsDAO;
import com.strongfriends.model.News;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsService {
    @Autowired
    private NewsDAO newsDAO;

    public List<News> getLatestNews(int userId, int offset,int limit){
        return newsDAO.selectByUserIdAndOffset(userId,offset,limit);
    }

}
