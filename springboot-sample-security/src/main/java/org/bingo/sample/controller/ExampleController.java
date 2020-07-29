package org.bingo.sample.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class ExampleController {

    @GetMapping({"/index", "/"})
    public String index() {
        return "index";
    }

    @GetMapping("/url1")
    public String url1() {
        return "url1";
    }

    @GetMapping("/url2")
    @PreAuthorize("isAuthenticated()")
    public String url2() {
        return "url2";
    }

    @GetMapping("/admin/url1")
    public String adminA1() {
        return "/admin/url1";
    }

    @GetMapping("/admin/url2")
    /**EL表达式控制权限*/
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String adminA2() {
        return "/admin/url2";
    }

    @GetMapping("/user/url1")
    public String userA1() {
        return "/user/url1";
    }

    @GetMapping("/user/url2")
    public String userA2() {
        return "/user/url2";
    }

    /**使用@AuthenticationPrincipal注解获取用户详细信息 - 需认证*/
    @GetMapping("/getPrincipal")
    public Object getPrincipal(@AuthenticationPrincipal Principal principal){
        return principal;
    }
    @GetMapping("/getUserDetails")
    public Object getUserDetails(@AuthenticationPrincipal UserDetails userDetails) {
        return userDetails;
    }
}
