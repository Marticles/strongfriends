package com.strongfriends.controller;

import com.alibaba.fastjson.JSON;
import com.strongfriends.async.EventModel;
import com.strongfriends.async.EventProducer;
import com.strongfriends.async.EventType;
import com.strongfriends.model.HostHolder;
import com.strongfriends.model.Message;
import com.strongfriends.model.User;
import com.strongfriends.model.ViewObject;
import com.strongfriends.service.MessageService;
import com.strongfriends.service.UserService;
import com.strongfriends.util.StrongFriendsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    HostHolder hostHolder;

    @Autowired
    UserService userService;

    @Autowired
    MessageService messageService;

    @Autowired
    EventProducer eventProducer;

    @RequestMapping(path = {"/msg/detail"}, method = {RequestMethod.GET})
    public String conversationDetail(Model model, @RequestParam("conversationId") String conversationId) {
        try {
            List<ViewObject> messages = new ArrayList<>();
            List<Message> conversationList = messageService.getConversationDetail(conversationId);
            for (Message msg : conversationList) {
                ViewObject vo = new ViewObject();
                vo.set("message", msg);
                User user = userService.getUser(msg.getFromId());
                if (user == null) {
                    continue;
                }
                vo.set("headUrl", user.getHeadUrl());
                vo.set("userName", user.getName());
                messages.add(vo);
            }
            model.addAttribute("messages", messages);
            return "letterDetail";
        } catch (Exception e) {
            logger.error("获取私信列表失败" + e.getMessage());
        }
        return "letterDetail";
    }

    @RequestMapping(path = {"/msg/list"}, method = {RequestMethod.GET})
    public String conversationList(Model model) {
        try {
            int localUserId = hostHolder.getUser().getId();
            List<ViewObject> conversations = new ArrayList<>();
            List<Message> conversationList = messageService.getConversationList(localUserId, 0, 10);
            for (Message msg : conversationList) {
                ViewObject vo = new ViewObject();
                vo.set("conversation", msg);
                int targetId = msg.getFromId() == localUserId ? msg.getToId() : msg.getFromId();
                User user = userService.getUser(targetId);
                vo.set("headUrl", user.getHeadUrl());
                vo.set("userName", user.getName());
                vo.set("targetId", targetId);
                vo.set("totalCount", msg.getId());
                vo.set("unreadCount", messageService.getUnreadCount(localUserId, msg.getConversationId()));
                conversations.add(vo);
            }
            model.addAttribute("conversations", conversations);
            return "letter";
        } catch (Exception e) {
            logger.error("获取私信信列表失败" + e.getMessage());
        }
        return "letter";
    }

    @RequestMapping(path = {"/msg/addMessage"}, method = {RequestMethod.POST})
    @ResponseBody
    public String addMessage(HttpServletRequest request) {
        int fromId = hostHolder.getUser().getId();
        User toUser = userService.getUserByName(request.getParameter("to_name"));
        int toId = toUser.getId();
        String content = request.getParameter("content");

        Message msg = new Message();
        msg.setContent(content);
        msg.setCreatedDate(new Date());
        msg.setToId(toId);
        msg.setFromId(fromId);
        msg.setConversationId(fromId < toId ? String.format("%d_%d", fromId, toId) :
                String.format("%d_%d", toId, fromId));

        // messageService.addMessage(msg);
        String msgString = JSON.toJSONString(msg, true);
        eventProducer.produceEvent(new EventModel(EventType.MESSAGE)
                .setEntityOwnerId(toId)
                .setActorId(fromId)
                .setExt("msg",msgString));

        return StrongFriendsUtil.getJSONString(msg.getId());
    }
}