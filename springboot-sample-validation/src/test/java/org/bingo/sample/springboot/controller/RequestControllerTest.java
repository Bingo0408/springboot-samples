package org.bingo.sample.springboot.controller;

import com.google.gson.Gson;
import org.bingo.sample.springboot.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

/**
 * @Author: Bin GU
 * @Date: 2019/2/13 15:32
 * @Version Initial Version
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RequestControllerTest {

    @Autowired
    private MockMvc mvc;

    Gson gson = new Gson();

    @Test
    public void checkInsertGroup() throws Exception {

        User user1 = new User();
        user1.setId(1l);
        user1.setName("");  // InsertGroup NotEmpty Validation

        User user2 = new User();
        user2.setId(null);  // UpdateGroup NotEmpty Validation
        user2.setName("Bingo");

        // 抛出名字不能为空
        mvc.perform(MockMvcRequestBuilders.post("/validation/check_insert_group")
           .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(user1)));
        // 正常
        mvc.perform(MockMvcRequestBuilders.post("/validation/check_insert_group")
                .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(user2)));
        // 正常
        mvc.perform(MockMvcRequestBuilders.post("/validation/check_update_group")
                .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(user1)));
        // 抛出ID不能为空
        mvc.perform(MockMvcRequestBuilders.post("/validation/check_update_group")
                .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(user2)));
    }

    @Test
    public void checkUpdateGroup() {
    }
}