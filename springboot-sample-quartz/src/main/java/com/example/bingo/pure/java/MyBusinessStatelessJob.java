package com.example.bingo.pure.java;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.time.LocalDateTime;
/** 1.实现Quartz的Job接口
 *  2.jobExecutionContext是Quartz的上下文环境
 *  P.S. 每次调度器执行Job时, 在调用execute前会创建新的Job实例，调用完成后释放
 *       JobExecutionContext可以获取Quartz运行时信息，如.getJobDetail(),
 *       .getJobDataMap() - 该Map在Scheduler中的Trigger和JobDetail设置 */
public class MyBusinessStatelessJob implements Job{
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("自定义任务: " + LocalDateTime.now());
        System.out.println(jobExecutionContext.getJobDetail().getJobDataMap().get("JobDetailData"));
        System.out.println(jobExecutionContext.getTrigger().getJobDataMap().get("TriggerData"));
        System.out.println("当前任务执行时间: " + jobExecutionContext.getFireTime());
        System.out.println("下次任务执行时间: " + jobExecutionContext.getNextFireTime());
        System.out.println("===============================");
    }
}
/** 有状态Job的实现步骤
 * 1.在JobClass上注解@PersistJobDataAfterExecution后, 多次Job调用会持久化JobDataMap中的信息 */

