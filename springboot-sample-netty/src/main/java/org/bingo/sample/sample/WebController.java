package org.bingo.sample.sample;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Bin GU
 * @Date: 2019/5/19 18:56
 * @Version Initial Version
 */
@RestController
public class WebController {

    /** 仍然可以使用原SpringMVC的功能 */
    @GetMapping("/webController")
    public String webController(){
        return "webController";
    }
}
