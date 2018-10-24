package com.strongfriends;

import com.strongfriends.dao.CommentDAO;
import com.strongfriends.dao.LoginTicketDAO;
import com.strongfriends.dao.NewsDAO;
import com.strongfriends.dao.UserDAO;
import com.strongfriends.model.*;
import org.junit.Test;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = StrongfriendsApplication.class)
public class InitDatabaseTests {

    @Autowired
    UserDAO userDAO;

    @Autowired
    NewsDAO newsDAO;

    @Autowired
    LoginTicketDAO loginTicketDAO;

    @Autowired
    CommentDAO commentDAO;

    @Test
    public void initData() {
        for (int i = 0; i < 3; ++i) {
//            User user = new User();
//            user.setHeadUrl(String.format("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3198678185,878755003&fm=26&gp=0.jpg"));
//            user.setName(String.format("USER%d", i));
//            user.setPassword("");
//            user.setSalt("");
//            userDAO.addUser(user);
//
//            News news = new News();
//            news.setCommentCount(i);
//            Date date = new Date();
//            date.setTime(date.getTime() + 1000 * 3600 * 5 * i);
//            news.setCreatedDate(date);
//            news.setImage(String.format("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3198678185,878755003&fm=26&gp=0.jpg"));
//            news.setLikeCount(i + 1);
//            news.setUserId(i + 1);
//            news.setTitle(String.format("TITLE%d", i));
//            news.setLink(String.format("http://www.strongfriends.com/%d.html", i));
//            newsDAO.addNews(news);
//
//            user.setPassword("012345");
//            userDAO.updatePassword(user);
//            Date date = new Date();
//            LoginTicket ticket = new LoginTicket();
//            ticket.setStatus(0);
//            ticket.setUserId(i+1);
//            ticket.setExpired(date);
//            ticket.setTicket(String.format("TICKET%d",i+1));
//            loginTicketDAO.addTicket(ticket);
//            loginTicketDAO.updateStatus(ticket.getTicket(), 2);
//            for(int j = 0; j < 3; ++j) {
//                Comment comment = new Comment();
//                comment.setUserId(i+1);
//                comment.setCreatedDate(new Date());
//                comment.setStatus(0);
//                comment.setContent("TEST评论" + String.valueOf(j));
//                comment.setEntityId(news.getId());
//                comment.setEntityType(EntityType.ENTITY_POST);
//                commentDAO.addComment(comment);
//            }
//
        }

//        Assert.assertEquals("012345", userDAO.selectById(1).getPassword());
//        userDAO.deleteById(1);
//        Assert.assertNull(userDAO.selectById(1));
//        Assert.assertEquals(1,loginTicketDAO.selectByTicket("TICKET1").getUserId());
//        Assert.assertEquals(2,loginTicketDAO.selectByTicket("TICKET1").getStatus());
        Assert.assertNotNull(commentDAO.selectByEntity(1, EntityType.ENTITY_POST).get(0));
    }
}
