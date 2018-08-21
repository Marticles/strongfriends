package com.strongfriends.controller;

import com.strongfriends.aspect.LogAspect;
import com.strongfriends.model.User;
import com.strongfriends.service.StrongfriendsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class IndexController {

    @Autowired
    private StrongfriendsService  strongfriends;

    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

    @RequestMapping(path = {"/"})
    @ResponseBody
    public String index(HttpSession session) {
        logger.info("Visit index");

        return "Demo," + session.getAttribute("msg")+"<br>Say:"+strongfriends.say();

    }

    @RequestMapping(value = {"/profile/{groupId}/{userId}"})
    @ResponseBody
    public String profile(@PathVariable("groupId") String groupId,
                          @PathVariable("userId") int userId,
                          @RequestParam(value = "type", defaultValue = "1") int type,
                          @RequestParam(value = "key", defaultValue = "xxx") String key) {
        return String.format("{%s},{%d},{%d},{%s}", groupId, userId, type, key);
    }

    @RequestMapping(value = {"/vm"})
    public String news(Model model) {
        model.addAttribute("value1", "sxxxxxx2");
        List<String> colors = Arrays.asList(new String[]{"RED", "BLUE"});

        Map<String, String> map = new HashMap<String, String>();
        for (int i = 0; i < 4; i++) {
            map.put(String.valueOf(i), String.valueOf(i * i));
        }

        model.addAttribute("colors", colors);
        model.addAttribute("map", map);
        model.addAttribute("user", new User("tom"));

        return "news";
    }

    @RequestMapping(value = {"/request"})
    @ResponseBody
    public String request(HttpServletRequest request,
                          HttpServletResponse response,
                          HttpSession session) {
        StringBuilder sb = new StringBuilder();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            sb.append(name + ":" + request.getHeader(name) + "<br>");
        }


        return sb.toString();
    }

    @RequestMapping(value = {"/response"})
    @ResponseBody
    public String response(@CookieValue(value = "strongfriendsid", defaultValue = "aaS8") String strongfriendsId
            , @RequestParam(value = "key", defaultValue = "key") String key
            , @RequestParam(value = "value", defaultValue = "value") String value
            , HttpServletResponse response) {

        response.addCookie(new Cookie(key, value));
        response.addHeader(key, value);
        return "strongfriedns Cookie:" + strongfriendsId;

    }

    @RequestMapping("/redirect/{code}")
    public String redirect(@PathVariable("code") int code, HttpSession session) {
        /*RedirectView red = new RedirectView("/",true);

        if (code==301){
            red.setStatusCode(HttpStatus.MOVED_PERMANENTLY);

        }
        return red;*/
        session.setAttribute("msg", "Jump from redirect");
        return "redirect:/";
    }

    @RequestMapping("/admin")
    @ResponseBody
    public String main(@RequestParam(value="key",required=false) String key) throws IllegalAccessException {
        if("admin".equals(key)){
            return "hello,admin";
        }
        throw new IllegalArgumentException("Error!");
    }

    @ExceptionHandler
    @ResponseBody
    public String error(Exception e){
        return "error:" + e.getMessage();
    }
}

