package com.strongfriends.controller;

import com.strongfriends.model.*;
import com.strongfriends.service.CommentService;
import com.strongfriends.service.LikeService;
import com.strongfriends.service.NewsService;
import com.strongfriends.service.UserService;
import com.strongfriends.util.StrongFriendsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class NewsController {
    private static final Logger logger = LoggerFactory.getLogger(NewsController.class);

    @Autowired
    NewsService newsService;

    @Autowired
    UserService userService;

    @Autowired
    CommentService commentService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    LikeService likeService;

    @RequestMapping(path = {"/news/{newsId}"}, method = {RequestMethod.GET})
    public String newsDetail(@PathVariable("newsId") int newsId, Model model) {
        News news = newsService.getById(newsId);
        if (news != null) {
            int localUserId = hostHolder.getUser() != null ? hostHolder.getUser().getId() : 0;
            if (localUserId != 0) {
                model.addAttribute("like", likeService.getLikeStatus(localUserId, EntityType.ENTITY_POST, news.getId()));
            } else {
                model.addAttribute("like", 0);
            }
            // 评论
            List<Comment> comments = commentService.getCommentsByEntity(news.getId(), EntityType.ENTITY_POST);
            List<ViewObject> commentVOs = new ArrayList<ViewObject>();
            int i = comments.size();
            for (Comment comment : comments) {
                                ViewObject vo = new ViewObject();
                vo.set("comment", comment);
                vo.set("user", userService.getUser(comment.getUserId()));
                vo.set("floor",i);
                vo.set("userName",userService.getUser(comment.getUserId()).getName());
                commentVOs.add(vo);
                i-=1;
            }
            model.addAttribute("comments", commentVOs);
        }
        model.addAttribute("news", news);
        model.addAttribute("owner", userService.getUser(news.getUserId()));
        return "detail";
    }

    @RequestMapping(path = {"/image"}, method = {RequestMethod.GET})
    @ResponseBody
    public void getImage(@RequestParam("name") String imageName,
                         HttpServletResponse response) {
        try {
            response.setContentType("image");
            StreamUtils.copy(new FileInputStream(new
                    File(StrongFriendsUtil.IMAGE_DIR + imageName)), response.getOutputStream());
        } catch (Exception e) {
            logger.error("读取图片错误" + imageName + e.getMessage());
        }
    }

    @RequestMapping(path = {"/uploadImage/"}, method = {RequestMethod.POST})
    @ResponseBody
    public String uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            String fileUrl = newsService.saveImage(file);
            //String fileUrl = qiniuService.saveImage(file);
            if (fileUrl == null) {
                return StrongFriendsUtil.getJSONString(1, "上传图片失败");
            }
            return StrongFriendsUtil.getJSONString(0, fileUrl);
        } catch (Exception e) {
            logger.error("上传图片失败" + e.getMessage());
            return StrongFriendsUtil.getJSONString(1, "上传失败");
        }
    }

    @RequestMapping(path = {"/user/addNews/"}, method = {RequestMethod.POST})
    @ResponseBody
    public String addNews(@RequestParam("image") String image,
                          @RequestParam("title") String title,
                          @RequestParam("link") String link) {
        try {
            News news = new News();
            news.setCreatedDate(new Date());
            news.setTitle(title);
            news.setImage(image);
            news.setLink(link);
            if (hostHolder.getUser() != null) {
                news.setUserId(hostHolder.getUser().getId());
            } else {
                // 匿名用户id
                news.setUserId(99999999);
            }
            newsService.addNews(news);
            return StrongFriendsUtil.getJSONString(0);
        } catch (Exception e) {
            logger.error("添加新帖失败" + e.getMessage());
            return StrongFriendsUtil.getJSONString(1, "发布失败");
        }
    }

    @RequestMapping(path = {"/addComment"}, method = {RequestMethod.POST})
    public String addComment(@RequestParam("newsId") int newsId,
                             @RequestParam("content") String content) {
        try {
            Comment comment = new Comment();
            comment.setUserId(hostHolder.getUser().getId());
            comment.setContent(content);
            comment.setEntityType(EntityType.ENTITY_POST);
            comment.setEntityId(newsId);
            comment.setCreatedDate(new Date());
            comment.setStatus(0);
            commentService.addComment(comment);


            int count = commentService.getCommentCount(comment.getEntityId(), comment.getEntityType());
            newsService.updateCommentCount(comment.getEntityId(), count);



        } catch (Exception e) {
            logger.error("提交评论错误" + e.getMessage());
        }
        return "redirect:/news/" + String.valueOf(newsId);
    }
}
