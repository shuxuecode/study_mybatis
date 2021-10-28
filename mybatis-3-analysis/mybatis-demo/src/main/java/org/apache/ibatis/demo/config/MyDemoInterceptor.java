package org.apache.ibatis.demo.config;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;

import java.sql.Connection;
import java.util.Properties;

/**
 * @date 2021/10/28
 */

@Intercepts({@Signature(
        type = StatementHandler.class,
        method = "prepare",
        args = {Connection.class, Integer.class}
)})
public class MyDemoInterceptor implements Interceptor {

    /**
     * 每次执行到被拦截的方法后，都会进行此处进行逻辑增强处理
     *
     * @param invocation
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        System.out.println("对StatementHandler.prepare方法进行增强");
        // 执行原方法
        return invocation.proceed();
    }

    /**
     * 主要是为了把这个拦截器⽣成⼀个代理放到拦截器链中，包装⽬标对象 为⽬标对象创建代理对象；
     *
     * @param target
     * @return
     */
    @Override
    public Object plugin(Object target) {
        return Interceptor.super.plugin(target);
    }

    /**
     * 插件初始化的时候调⽤且只调⽤⼀次，插件配置的属性从plugin标签对应自定义插件设置的属性参数来获得。
     *
     * @param properties
     */
    @Override
    public void setProperties(Properties properties) {
        Interceptor.super.setProperties(properties);
    }
}
