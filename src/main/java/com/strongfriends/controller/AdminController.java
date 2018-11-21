package com.strongfriends.controller;

import com.strongfriends.model.*;
import com.strongfriends.service.CommentService;
import com.strongfriends.service.NewsService;
import com.strongfriends.service.UserService;
import com.strongfriends.util.StrongFriendsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AdminController {

    @Autowired
    NewsService newsService;

    @Autowired
    CommentService commentService;

    @Autowired
    UserService userService;

    @RequestMapping(path = {"/admin"}, method = {RequestMethod.GET})
    public String admin(Model model) {
        List<News> allNews = newsService.getAllNews();
        List<ViewObject> newsVOs = new ArrayList<ViewObject>();
        for (News news : allNews) {
            ViewObject vo = new ViewObject();
            vo.set("id", news.getId());
            vo.set("title", news.getTitle());
            vo.set("like_count", news.getLikeCount());
            vo.set("comments_count", news.getCommentCount());
            vo.set("create_date", news.getCreatedDate());
            vo.set("user_id", news.getUserId());
            User user = userService.getUser(news.getUserId());
            vo.set("user_name", user.getName());
            vo.set("domain",StrongFriendsUtil.STRONGFRIENDS_DOMAIN);
            newsVOs.add(vo);
        }
        model.addAttribute("news", newsVOs);

        List<User> allUsers = userService.getAllUsers();
        List<ViewObject> usersVOs = new ArrayList<ViewObject>();
        for (User user : allUsers) {
            ViewObject userVO = new ViewObject();
            userVO.set("id", user.getId());
            userVO.set("name", user.getName());
            userVO.set("post_count", newsService.getUserNewsCount(user.getId()));
            userVO.set("comment_count", commentService.getUserCommentCount(user.getId()));
            usersVOs.add(userVO);
        }
        model.addAttribute("users", usersVOs);


        return "admin";
    }

    @RequestMapping(path = {"/admin/news/{newsId}"}, method = {RequestMethod.GET})
    public String adminDetail(@PathVariable("newsId") int newsId,Model model) {
        News news = newsService.getById(newsId);
        ViewObject newsVO = new ViewObject();
        newsVO.set("id", news.getId());
        newsVO.set("title", news.getTitle());
        newsVO.set("like_count", news.getLikeCount());
        newsVO.set("comments_count", news.getCommentCount());
        newsVO.set("create_date", news.getCreatedDate());
        newsVO.set("user_id", news.getUserId());
        User user = userService.getUser(news.getUserId());
        newsVO.set("user_name", user.getName());
        newsVO.set("domain",StrongFriendsUtil.STRONGFRIENDS_DOMAIN);
        model.addAttribute("new", newsVO);

        List<Comment> comments = commentService.getCommentsByEntity(newsId,EntityType.ENTITY_COMMENT);
        List<ViewObject> commentsVOs = new ArrayList<ViewObject>();
        for (Comment comment : comments) {
            ViewObject vo = new ViewObject();
            vo.set("id", comment.getId());
            vo.set("content", comment.getContent());
            vo.set("user_id", comment.getUserId());
            User commentUser = userService.getUser(comment.getUserId());
            vo.set("user_name", commentUser.getName());

            vo.set("create_date", comment.getCreatedDate());
            commentsVOs.add(vo);
        }
        model.addAttribute("comments", commentsVOs);


        return "adminDetail";
    }

    @RequestMapping(path = {"/admin/delnews"}, method = {RequestMethod.POST})
    @ResponseBody
    public String delNews(HttpServletRequest request){
        int delNewsId = Integer.parseInt(request.getParameter("news_id"));
        newsService.delNews(delNewsId);
        return StrongFriendsUtil.getJSONString(1, "帖子ID："+delNewsId+"已删除");
    }

    @RequestMapping(path = {"/admin/delcomment"}, method = {RequestMethod.POST})
    @ResponseBody
    public String delComment(HttpServletRequest request){
        int delCommentId = Integer.parseInt(request.getParameter("comment_id"));
        commentService.delcomment(delCommentId);
        return StrongFriendsUtil.getJSONString(1, "评论ID："+delCommentId+"已删除");
    }

    @RequestMapping(path = {"/admin/deluser"}, method = {RequestMethod.POST})
    @ResponseBody
    public String delUser(HttpServletRequest request){
        int delUserId = Integer.parseInt(request.getParameter("user_id"));
        userService.delUserId(delUserId);
        return StrongFriendsUtil.getJSONString(1, "用户ID："+delUserId+"已删除");
    }



}
