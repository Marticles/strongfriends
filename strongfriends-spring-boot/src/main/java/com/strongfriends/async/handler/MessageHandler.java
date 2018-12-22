package com.strongfriends.async.handler;

import com.alibaba.fastjson.JSON;
import com.strongfriends.async.EventHandler;
import com.strongfriends.async.EventModel;
import com.strongfriends.async.EventType;
import com.strongfriends.model.*;
import com.strongfriends.service.MessageService;
import com.strongfriends.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.util.Arrays;
import java.util.List;

@Component
public class MessageHandler implements EventHandler {

    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;


    @Override
    public void doHandle(EventModel model) {
        Message msg = JSON.parseObject(model.getExt("msg"), Message.class);
        messageService.addMessage(msg);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.MESSAGE);
    }
}
