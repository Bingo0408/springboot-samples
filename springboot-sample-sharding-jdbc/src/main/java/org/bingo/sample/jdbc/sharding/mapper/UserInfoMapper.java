package org.bingo.sample.jdbc.sharding.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.bingo.sample.jdbc.sharding.entity.UserInfo;

import java.util.List;

public interface UserInfoMapper {

    @Select("SELECT user_id, user_name, account, password FROM user_info WHERE user_id=#{userId, jdbcType=BIGINT}")
    List<UserInfo> select(Long userId);

    @Insert("insert into user_info (user_id, user_name, account,  password)" +
            "values (#{userId,jdbcType=BIGINT}, #{userName, jdbcType=VARCHAR}, #{account, jdbcType=VARCHAR}, #{password, jdbcType=VARCHAR})")
    void insert(UserInfo userInfo);
}
