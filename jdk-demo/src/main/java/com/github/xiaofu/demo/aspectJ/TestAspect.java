/**
 * @ProjectName: jdk-demo
 * @Copyright: 版权所有 Copyright © 2001-2017 cqvip.com Inc. All rights reserved. 
 * @address: http://www.cqvip.com
 * @date: 2017年7月6日 上午11:06:21
 * @Description: 本内容仅限于维普公司内部使用，禁止转发.
 */
package com.github.xiaofu.demo.aspectJ;

/**
 * <p></p>
 * @author fulaihua 2017年7月6日 上午11:06:21
 * @version V1.0   
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user:fulaihua 2017年7月6日
 * @modify by reason:{方法名}:{原因}
 */
import org.aspectj.lang.annotation.Aspect ;
import org.aspectj.lang.annotation.Before ;
import org.aspectj.lang.annotation.After ;
import org.aspectj.lang.JoinPoint ;
 
@Aspect
public class TestAspect {
    @Before ("execution (* TestTarget.test*(..))")
    public void beforeAdvice (JoinPoint joinPoint) {
        System.out.printf ("TestAspect.advice() called on '%s'%n", joinPoint) ;
    }
 
    @After ("execution (* TestTarget.test*(..))")
    public void afterAdvice (JoinPoint joinPoint) {
        System.out.printf ("TestAspect.advice() call ended%n") ;
    }
}
