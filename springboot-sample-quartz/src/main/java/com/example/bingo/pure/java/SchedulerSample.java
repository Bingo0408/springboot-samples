package com.example.bingo.pure.java;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

/** 1.使用StdSchedulerFactory定义调度器Scheduler,
 *    默认读取quartz.properties, 位于quartz-2.3.1.jar - StdSchedulerFactory中定义了所有Key
 *  2.实例化任务JobDetail
 *  3.触发器Trigger。- CronTrigger支持"星期几", 每个月最后几天, 每个月第几个周几（父亲节）
 *  4.将Job和Trigger关联到Scheduler上
 *  5.启动Scheduler
 *  P.S. usingJobData()为JobDetail和Trigger设置JobDataMap
 *       Quartz的Cron表达式比较特殊, 可以使用专门的生成器生成 */
public class SchedulerSample {
    public static void main(String[] args) throws SchedulerException {
        // 1.生成调度器, 除此之外, DirectSchedulerFactory使用场景很少
        // StdSchedulerFactory默认读取quartz.properties文件, StdSchedulerFactory中定义了所有Key
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        // 2.实例化任务JobDetail（JobName支持分组）
        JobDetail jobDetail = JobBuilder.newJob(MyBusinessStatelessJob.class)
                                        .withIdentity("JobName", "JobGroup")
                                        // 设置JobDataMap
                                        .usingJobData("JobDetailData", "JobDetailDataMap")
                                        .build();
        // 3.触发器Trigger
        Trigger trigger = TriggerBuilder.newTrigger()
                                        .withIdentity("TriggerName", "JobGroup")
                                        // 定义Scheduler, 重复执行3次（从0开始算起）
                                        // 除了例子中的SimpleTrigger, 还可以使用CronScheduleBuilder（支持"星期几"设定）
                                        .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(2).withRepeatCount(3))
                                        // 设置JobDataMap
                                        .usingJobData("TriggerData", "TriggerDataMap")
                                        .startNow()
                                        .build();
        // 4.将Job和Trigger关联到Scheduler上
        scheduler.scheduleJob(jobDetail, trigger);
        // 5.启动Scheduler, 其它方法有:挂起standby, 关闭shutdown(true) - true表示等待所有任务执行完毕关闭
        scheduler.start();
    }
}