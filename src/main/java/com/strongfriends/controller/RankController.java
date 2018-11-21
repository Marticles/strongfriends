package com.strongfriends.controller;

import com.strongfriends.model.EntityType;
import com.strongfriends.model.HostHolder;
import com.strongfriends.model.Rank;
import com.strongfriends.model.ViewObject;
import com.strongfriends.service.RankService;
import com.strongfriends.util.StrongFriendsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
public class RankController {

    int AnonymousId = 10000;

    @Autowired
    RankService rankService;

    @Autowired
    HostHolder hostHolder;

    @RequestMapping(path = {"/rank"}, method = {RequestMethod.GET})
    public String rank(Model model) {
        List<Rank> ranks = rankService.getAllRank();
        List<ViewObject> rankVOs = new ArrayList<ViewObject>();
        for (Rank rank : ranks) {
            ViewObject vo = new ViewObject();
            vo.set("id", rank.getId());
            vo.set("name",rank.getName());
            vo.set("gender",rank.getGender());
            vo.set("weight",rank.getWeight());
            vo.set("squat",rank.getSquat());
            vo.set("bench",rank.getBench());
            vo.set("deadlift",rank.getDeadlift());
            vo.set("total",rank.getTotal());
            vo.set("wilks",rank.getWilks());
            rankVOs.add(vo);
        }
        model.addAttribute("ranks", rankVOs);
        return "rank";
    }


    @RequestMapping(path = {"/rank/addRank"}, method = {RequestMethod.POST})
    @ResponseBody
    public String addMessage(HttpServletRequest request) {

        int localUserId = hostHolder.getUser() != null ? hostHolder.getUser().getId() : 0;
        String localUserName = hostHolder.getUser() != null ? hostHolder.getUser().getName() : "匿名用户";
        Rank rank = new Rank();

        if (localUserName == "匿名用户") {
            rank.setId(AnonymousId);
            AnonymousId += 1;
        }else{
            rank.setId(localUserId);

        }
        rank.setName(localUserName);
        rank.setGender(request.getParameter("gender"));
        rank.setWeight(Float.parseFloat(request.getParameter("weight")));
        rank.setSquat(Float.parseFloat(request.getParameter("squat")));
        rank.setBench(Float.parseFloat(request.getParameter("bench")));
        rank.setDeadlift(Float.parseFloat(request.getParameter("deadlift")));
        rank.setTotal(Float.parseFloat(request.getParameter("total")));
        rank.setWilks(Float.parseFloat(request.getParameter("wilks")));

        Rank tmpRank = rankService.getById(localUserId);
        // 若排行榜中不存在该用户的数据
        if (tmpRank == null) {
            // 新增
            rankService.addRank(rank);
            return StrongFriendsUtil.getJSONString(rank.getId());
        } else {
            // 存在则修改
            rankService.updateRank(rank);
            return StrongFriendsUtil.getJSONString(rank.getId());
        }

    }


}
