package org.bingo.sample.springboot.controller;

import org.bingo.sample.springboot.model.User;
import org.bingo.sample.springboot.validation.group.InsertGroup;
import org.bingo.sample.springboot.validation.group.UpdateGroup;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Bin Gu
 * @Date: 2019/2/13 10:20
 * @Version Initial Version
 */
/** 为表单添加Validation功能, 步骤如下
 *  1.在request参数上添加@Validated/@Valid注解表明需要表单验证
 *  2.在被验证的对象属性上添加验证规则及出错信息, 如在User的ID属性上添加@NotNull(message = "ID不能为空")
 *  3.1 选择1: 为方法添加BindingResult回调, 获取验证结果以及验证出错后的信息（相当于自己进行回调处理，因此不会抛出异常了）
 *  3.2 选择2: 不使用BindingResult回调, 直接抛出异常, 使用统一异常处理的方式
 *  4.如果返回的是ModelAndView, 也可以将Model设置到方法参数中
 */
@RequestMapping("/validation")
@RestController
public class RequestController {

    /** 在InsertGroup设定下, ID可以为空, Name不能为空 */
    @PostMapping(value="/check_insert_group")
    public String checkInsertGroup(@Validated({InsertGroup.class}) @RequestBody User user) {
        return "SUCCESS";
    }

    /** 在UpdateGroup设定下, ID不能为空, Name可以为空 */
    @PostMapping(value="/check_update_group")
    public String checkUpdateGroup(@Validated({UpdateGroup.class}) @RequestBody User user, BindingResult bindingResult) {
        // 此时, 所有校验异常都存放在BindingResult中, 程序不会抛出异常, 一般不建议这样处理
        if(bindingResult.hasErrors()) {
            System.out.println(bindingResult.getFieldError().getDefaultMessage());
            return "ERROR"; // 验证出错, 直接返回
        }
        // 业务代码1
        // 业务代码2
        // ...
        return "SUCCESS";
    }
}
