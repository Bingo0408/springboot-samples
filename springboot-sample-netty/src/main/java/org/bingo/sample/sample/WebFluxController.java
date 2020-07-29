package org.bingo.sample.sample;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/** 0.sample尝试
 *  http://localhost:8080/webFlux/users/list1
 *  http://localhost:8080/webFlux/users/list2
 *  http://localhost:8080/webFlux/users/user
 *
 *  1.与SpringMVC不同的是，webFlux封装了返回对象的数据结构
 *  1.1. 如果返回的是collection形式，使用Flux<T>的形式
 *  1.2. 如果返回的是单一对象的形式，使用Mono<T>
 *
 *  2.对比list1和list2的api，list2的返回方式与SpringMVC完全一样，而list1以流式处理的方式返回结果
 *  2.1. list1延长了通道的时长，以stream的方式返回json
 */
@RestController
@RequestMapping("/webFlux/users")
public class WebFluxController {

    @GetMapping(value = "/list1", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<User> listUsers1(){
        List<User> users = new ArrayList<User>();
        for (int i = 0; i < 100; i++) {
            users.add(new User("bingo"+i,28));
        }
        return Flux.fromIterable(users).delayElements(Duration.ofSeconds(1));
    }

    @GetMapping(value = "/list2")
    public Flux<User> listUsers2(){
        List<User> users = new ArrayList<User>();
        for (int i = 0; i < 100; i++) {
            users.add(new User("bingo"+i,28));
        }
        return Flux.fromIterable(users);
    }

    @GetMapping("/user")
    public Mono<User> getUser(){
        return Mono.just(new User("bingo",28));
    }
}