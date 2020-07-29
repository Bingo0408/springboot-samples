package org.bingo.sample.springboot.model;

import lombok.Data;
import org.bingo.sample.springboot.validation.group.InsertGroup;
import org.bingo.sample.springboot.validation.group.UpdateGroup;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @Author: Bin GU
 * @Date: 2019/2/13 14:34
 * @Version Initial Version
 */
@Data
public class User {

    /** 知识点1: 指定非空验证在指定UpdateGroup时不能为空 */
    @NotNull(groups = {UpdateGroup.class}, message = "ID不能为空")
    private Long id;

    /** 知识点2: 指定非空验证在指定InsertGroup时不能为空 */
    @NotEmpty(groups = {InsertGroup.class}, message = "名字不能为空")
    private String name;

    /** 知识点3:
    @Valid   // 如果WorkingExp这个JavaBean中的校验逻辑需要在当前bean中生效, 需要使用@Valid来让嵌套校验生效
    @Size(min = 1, message = "workingExpList至少有一个工作经验")   // 该设定只对workingExpList本身长度校验生效
    private List<WorkingExp> workingExpList;
    */
}
