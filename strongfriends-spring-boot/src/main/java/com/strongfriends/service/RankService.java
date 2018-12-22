package com.strongfriends.service;


import com.alibaba.fastjson.JSONObject;
import com.strongfriends.dao.RankDAO;
import com.strongfriends.model.Rank;
import com.strongfriends.util.JedisAdapter;
import org.apache.commons.lang3.builder.ToStringExclude;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class RankService {

    @Autowired
    private RankDAO rankDao;

    @Autowired
    private JedisAdapter jedisAdapter;

    public int addRank(Rank rank) {

        return rankDao.addRank(rank);
    }

    public int updateRank(Rank rank) {

        return rankDao.updateRank(rank);
    }

    public Rank getById(int id) {
        return rankDao.getById(id);
    }

    public List<Rank> getAllRank() {
        return rankDao.getAllRank();
    }

    public void addRankByRedis(Rank rank){
        jedisAdapter.zadd("RANK",rank.getWilks(),JSONObject.toJSONString(rank));
    }

    public List<Rank> getAllRankByRedis(){
        Set<String> jedisRanks = jedisAdapter.zrevrange("RANK",0,10);
        List<Rank> ranks = new ArrayList<Rank>();
        for(String jsonRank : jedisRanks){
            Rank rank = JSONObject.parseObject(jsonRank,Rank.class);
            ranks.add(rank);
        }
        return ranks;
    }

    public void delRankByRedis(Rank rank){
        jedisAdapter.zremove("RANK",JSONObject.toJSONString(rank));
    }

}
