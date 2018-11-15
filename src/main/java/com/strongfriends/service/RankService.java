package com.strongfriends.service;


import com.strongfriends.dao.RankDAO;
import com.strongfriends.model.Rank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RankService {

    @Autowired
    private RankDAO rankDao;

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


}
