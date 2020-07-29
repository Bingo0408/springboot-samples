package org.bingo.sample.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.bingo.sample.entity.User1;

import java.util.List;

public interface User1Mapper {
    @Insert("INSERT INTO user_1(name) VALUES(#{name})")
    int insert(User1 user1);

    @Select("SELECT id, name FROM user_1")
    List<User1> selectAll();

    @Delete("DELETE FROM user_1")
    void deleteAll();
}
