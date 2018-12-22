package com.strongfriends.service;


import com.strongfriends.dao.TrackerDAO;
import com.strongfriends.model.Tracker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrackerService {

    @Autowired
    TrackerDAO trackerDAO;

    public List<Tracker> getById(int id){
        return trackerDAO.getById(id);
    }

    public void addTracker (Tracker tracker){
        trackerDAO.addTracker(tracker);
    }

}
