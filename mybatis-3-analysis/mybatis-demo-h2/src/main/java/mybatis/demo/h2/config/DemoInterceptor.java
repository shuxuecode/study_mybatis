package mybatis.demo.h2.config;

import com.alibaba.fastjson2.JSON;
import mybatis.demo.h2.DemoMapper;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;

import java.sql.Statement;
import java.util.Properties;

/**
 * @date 2022/9/4
 */

@Intercepts({
        @Signature(
                type = DemoMapper.class,
                method = "findAll",
                args = {}
        )
})
public class DemoInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {

// todo zsx
        Object proceed = invocation.proceed();

        System.out.println(JSON.toJSONString(invocation.getArgs()));
        System.out.println(JSON.toJSONString(invocation.getMethod()));
        System.out.println(JSON.toJSONString(invocation.getTarget()));

        return proceed;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        System.out.println(JSON.toJSONString(properties));
    }
}
