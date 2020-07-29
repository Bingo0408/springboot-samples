package org.bingo.sample.service;

import org.bingo.sample.entity.User1;
import org.bingo.sample.entity.User2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ParentService {

    User1 u1 = new User1("user_1");
    User2 u2 = new User2("user_2");

    @Autowired
    private User1Service user1Service;

    @Autowired
    private User2Service user2Service;

    /** 方法命名规则
     *  外层(父)方法事务传播类型_内部方法事务传播类型 */

    /** Propagation.REQUIRED: 当前没有事务就新建，已经存在则加入该事务
     *  ①等价于单独调用user1Service(), 由于上下文不存在事务, 会单独开启一个新事务
     *  ②中执行到外层方法时, 开启新事务。当执行到user1Service()时, 加入该事务 */
    public void NONE_REQUIRED(){
        user1Service.TX_REQUIRED(u1, false);
    }
    @Transactional(propagation = Propagation.REQUIRED)
    public void REQUIRED_REQUIRED(){
        user1Service.TX_REQUIRED(u1, false);
    }

    /** Propagation.SUPPORTS: 当前有事务则加入该事务，没有则以非事务方式执行
     *  ①等价于单独调用user1Service(), 以非事务运行
     *  ②中执行到外层方法时, 开启新事务。当执行到user1Service()时, 加入该事务 */
    public void NONE_SUPPORTS(){
        user1Service.TX_SUPPORTS(u1, false);
    }
    @Transactional(propagation = Propagation.REQUIRED)
    public void REQUIRED_SUPPORTS(){
        user1Service.TX_SUPPORTS(u1, false);
    }

    /** Propagation.MANDATORY: 使用当前事务，没有则抛出异常
     *  ①等价于单独调用user1Service(), 由于上下文不存在事务, 因此会直接抛出异常
     *  ②中定义了外层方法的事务, 事务正常执行
     *  原因在于user1Service()定义为Propagation.MANDATORY, 要求当前必须存在事务, 否则抛出异常 */
    public void NONE_MANDATORY(){
        user1Service.TX_MANDATORY(u1, false);
    }
    @Transactional(propagation = Propagation.REQUIRED)
    public void REQUIRED_MANDATORY(){
        user1Service.TX_MANDATORY(u1, false);
    }

    /** Propagation.REQUIRES_NEW: 新建事务，如果当前有事务则挂起该事务。需要使用JtaTransactionManager作为事务管理器
     *  外层事务与内层事务是两个互相独立的事务。考虑如下情况,
     *  doSomethingA() - 成功
     *  user1Service() - 成功
     *  doSomethingB() - 失败
     *  那么执行结果为: user1Service()事务正常提交, 而doSomethingA()和doSomethingB()事务都会被回滚 */
    @Transactional(propagation = Propagation.REQUIRED)
    public void REQUIRED_REQUIRESNEW(){
        System.out.println("该方法是示意方法, 无法执行");
        // doSomethingA()
        user1Service.TX_REQUIRES_NEW(u1, false);
        // doSomethingB()
    }

    /** Propagation.NOT_SUPPORTED: 无论上下文是否有事务, 都以非事务执行
     *  ①与②都会以非事务执行 */
    public void NONE_NOTSUPPORTED(){
        user1Service.TX_NOT_SUPPORTED(u1, true);
    }
    @Transactional(propagation = Propagation.REQUIRED)
    public void REQUIRED_NOTSUPPORTED(){
        user1Service.TX_NOT_SUPPORTED(u1, true);
    }

    /** Propagation.NEVER: 总是以非事务执行, 如果上下文存在事务则抛出异常
     *  ①正常执行, ②抛出异常 */
    public void NONE_NEVER(){
        user1Service.TX_NEVER(u1, false);
    }
    @Transactional(propagation = Propagation.REQUIRED)
    public void REQUIRED_NEVER(){
        user1Service.TX_NEVER(u1, false);
    }

    /** Propagation.NEVER: 如果上下文存在事务，则在嵌套事务中执行。如果上下文没有活动事务则执行与REQUIRED类似的操作
     *  使用JDBC 3.0驱动时, 仅仅支持DataSourceTransactionManager作为事务管理器。
     *  需要JDBC驱动的java.sql.Savepoint类。使用PROPAGATION_NESTED,
     *  还需要把PlatformTransactionManager的nestedTransactionAllowed属性设为true。
     *  ①等价于单独调用user1Service(), 按照REQUIRE属性执行
     *  ②即是一个嵌套事务, 内层事务依赖于外层事务。外层事务失败时回滚内层事务所有动作。而内层事务的失败不会引起外层事务的回滚。 */
    public void NONE_NESTED(){
        System.out.println("该方法是示意方法, 无法执行");
        // doSomethingA()
        user1Service.TX_NESTED(u1, false);
        // doSomethingB()
    }
    @Transactional(propagation = Propagation.REQUIRED)
    public void REQUIRED_NESTED(){
        System.out.println("该方法是示意方法, 无法执行");
        // doSomethingA()
        user1Service.TX_NESTED(u1, false);
        // doSomethingB()
    }
    /** 总结: PROPAGATION_REQUIRES_NEW与PROPAGATION_NESTED的区别
     *  两者都类似于嵌套事务, 前者中内层事务与外层事务更像两个独立事务。内层事务提交后, 外层事务不能对其回滚。
     *  后者中外层事务的回滚会引起内层事务的回滚, 但内层事务不会引起外层事务回滚, 是真正的嵌套事务。
     *  且前者完全会新起一个事务, 而后者则是真正的嵌套事务, 作为外层事务的子事务。*/
}
