package com.strongfriends.dao;

import com.strongfriends.model.News;
import com.strongfriends.model.Rank;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RankDAO {
    String TABLE_NAME = " rank ";
    String INSERT_FIELDS = " id, name, gender, weight, squat, bench, deadlift, total, wilks ";

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{id},#{name},#{gender},#{weight},#{squat},#{bench},#{deadlift},#{total,},#{wilks})"})
    int addRank(Rank rank);

    @Update({"update ", TABLE_NAME, " set name = #{name},gender = #{gender},weight = #{weight},squat=#{squat},bench=#{bench},deadlift=#{deadlift},total=#{total},wilks = #{wilks} where id=#{id}"})
    int updateRank(Rank rank);

    @Select({"select *  from ", TABLE_NAME, " where id=#{id}"})
    Rank getById(int id);

    @Select({"select * from ", TABLE_NAME, " order by wilks desc"})
    List<Rank> getAllRank();

}
