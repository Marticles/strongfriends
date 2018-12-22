package com.strongfriends.dao;

import com.strongfriends.model.Tracker;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TrackerDAO {

    String TABLE_NAME = " tracker ";

    @Select({"select *  from ", TABLE_NAME, " where user_id=#{id} order by created_date desc"})
    List<Tracker> getById(int id);

    @Insert({"insert into ", TABLE_NAME, " (user_id, origin_video_name, origin_img_name,status,created_date) values " +
            "(#{userId},#{originVideoName},#{originImgName},#{status},#{createdDate})"})
    int addTracker(Tracker tracker);


}
