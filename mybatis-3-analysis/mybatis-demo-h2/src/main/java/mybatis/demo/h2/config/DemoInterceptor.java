package mybatis.demo.h2.config;

import com.alibaba.fastjson2.JSON;
import mybatis.demo.h2.DemoMapper;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.lang.reflect.Method;
import java.sql.Statement;
import java.util.Properties;

/**
 * @date 2022/9/4
 */

@Intercepts({
        @Signature(
                type = Executor.class,
                method = "query",
                args = {
                        MappedStatement.class,
                        Object.class,
                        RowBounds.class,
                        ResultHandler.class
                }
        )
})
public class DemoInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {

// todo zsx

        System.out.println("invocation.getArgs() : ");
        Object[] args = invocation.getArgs();
        System.out.println(args);
        for (Object arg : args) {
            System.out.println(arg);
        }

        System.out.println();

        System.out.println("invocation.getMethod() : ");
        Method method = invocation.getMethod();
        System.out.println(method.getName());

        System.out.println();

        System.out.println("invocation.getTarget() : ");
        Object target = invocation.getTarget();
        System.out.println(target);
        System.out.println();


        System.out.println("原始sql ： " + getOriginalSql(invocation));

        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        Object parameter = invocation.getArgs()[1];
        BoundSql boundSql = mappedStatement.getBoundSql(parameter);

        BoundSql newBoundSql = new BoundSql(mappedStatement.getConfiguration(), getOriginalSql(invocation) + " where id = 2", boundSql.getParameterMappings(), boundSql.getParameterObject());

        System.out.println("改造sql ： " + newBoundSql.getSql());

        MappedStatement newMappedStatement = copyFromMappedStatement(mappedStatement, new BoundSqlSqlSource(newBoundSql));

        invocation.getArgs()[0] = newMappedStatement;

        // 继续执行
        Object proceed = invocation.proceed();

        return proceed;
    }

    /**
     * 获取原始sql
     *
     * @param invocation
     * @return
     */
    private String getOriginalSql(Invocation invocation) {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        Object parameter = invocation.getArgs()[1];
        BoundSql boundSql = mappedStatement.getBoundSql(parameter);
        return boundSql.getSql();
    }

    /**
     * 复制原始MappedStatement
     *
     * @param ms
     * @param newSqlSource
     * @return
     */
    private MappedStatement copyFromMappedStatement(MappedStatement ms, SqlSource newSqlSource) {
        MappedStatement.Builder builder = new MappedStatement.Builder(ms.getConfiguration(), ms.getId(), newSqlSource,
                ms.getSqlCommandType());
        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        if (ms.getKeyProperties() != null) {
            for (String keyProperty : ms.getKeyProperties()) {
                builder.keyProperty(keyProperty);
            }
        }
        builder.timeout(ms.getTimeout());
        builder.parameterMap(ms.getParameterMap());
        builder.resultMaps(ms.getResultMaps());
        builder.cache(ms.getCache());
        builder.useCache(ms.isUseCache());
        return builder.build();
    }

    public static class BoundSqlSqlSource implements SqlSource {
        BoundSql boundSql;

        public BoundSqlSqlSource(BoundSql boundSql) {
            this.boundSql = boundSql;
        }

        public BoundSql getBoundSql(Object parameterObject) {
            return boundSql;
        }
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
