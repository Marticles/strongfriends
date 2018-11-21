package com.strongfriends.service;

import com.alibaba.fastjson.JSON;
import com.strongfriends.dao.CommentDAO;
import com.strongfriends.model.Comment;
import com.strongfriends.util.JedisAdapter;
import com.strongfriends.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class CommentService {
    @Autowired
    CommentDAO commentDAO;

    @Autowired
    JedisAdapter jedisAdapter;

    public int getUserCommentCount(int userId) {
        return commentDAO.getUserCommentCount(userId);
    }

    public void delcomment(int commentId){
        commentDAO.delComment(commentId);
    }

    public List<Comment> getCommentsByEntity(int entityId, int entityType) {
        return commentDAO.selectByEntity(entityId, entityType);
    }

    public int addComment(Comment comment) {
        try{
            int result = commentDAO.addComment(comment);
            return result;
        }catch (Exception e){
            return 0;
        }

    }

    public int getCommentCount(int entityId, int entityType) {
        return commentDAO.getCommentCount(entityId, entityType);
    }


    public void addCommentToRedis(Comment comment){
        String CommentKey = RedisKeyUtil.getCommentKey(comment.getEntityId(), comment.getEntityType());
        String commentString = JSON.toJSONString(comment, true);
        jedisAdapter.sadd(CommentKey, commentString);

    }


    public Set<Comment> getCommentsByEntityFromRedis(int newsId) {

        return jedisAdapter.smembers("COMMENT:1:"+ String.valueOf(newsId));
    }

    public void delCommentFromRedis(String key){
        jedisAdapter.sdel(key);
    }

}

