package com.strongfriends;

import com.strongfriends.dao.CommentDAO;
import com.strongfriends.dao.LoginTicketDAO;
import com.strongfriends.dao.NewsDAO;
import com.strongfriends.dao.UserDAO;
import com.strongfriends.model.*;
import com.strongfriends.util.JedisAdapter;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.Random;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = StrongfriendsApplication.class)
public class JedisTests {
    @Autowired
    JedisAdapter jedisAdapter;

    @Test
    public void testObject() {
        User user = new User();
        user.setHeadUrl("http://test.com/test.jpg");
        user.setName("user1");
        user.setPassword("pwd");;
        user.setSalt("salt");

        jedisAdapter.setObject("test", user);

        User u = jedisAdapter.getObject("test", User.class);

        System.out.println(ToStringBuilder.reflectionToString(u));

    }

}

