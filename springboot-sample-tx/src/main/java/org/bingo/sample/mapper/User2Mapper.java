package org.bingo.sample.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.bingo.sample.entity.User2;

import java.util.List;

public interface User2Mapper {
    @Insert("INSERT INTO user_2(name) VALUES(#{name})")
    int insert(User2 user2);

    @Select("SELECT id, name FROM user_2")
    List<User2> selectAll();

    @Delete("DELETE FROM user_2")
    void deleteAll();
}
