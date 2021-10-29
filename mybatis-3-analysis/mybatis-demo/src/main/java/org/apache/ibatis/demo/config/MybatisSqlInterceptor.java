package org.apache.ibatis.demo.config;

import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.regex.Matcher;

/**
 * @date 2021/10/29
 * 参考文章：https://www.jianshu.com/p/c0bcb9beb51a
 */
@Intercepts({
        @Signature(
                type = Executor.class,
                method = "update",
                args = {MappedStatement.class, Object.class}
        ),
        @Signature(
                type = Executor.class,
                method = "query",
                args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}
        )
})
public class MybatisSqlInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        try {
            MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];

            Object parameter = null;

            if (invocation.getArgs().length > 1) {
                parameter = invocation.getArgs()[1];
            }

            BoundSql boundSql = mappedStatement.getBoundSql(parameter);
            Configuration configuration = mappedStatement.getConfiguration();

            String sqlId = mappedStatement.getId();
            String sql = getSql(configuration, boundSql);

            System.out.println("sqlId : " + sqlId);
            System.out.println(sql);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return invocation.proceed();
    }

    public static String getSql(Configuration configuration, BoundSql boundSql) {
        // 获取参数
        Object parameterObject = boundSql.getParameterObject();

        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();

        //sql语句中多个空格都用一个空格代替
        String sql = boundSql.getSql().replaceAll("[\\s]+", " ");

        if (parameterObject == null || CollectionUtils.isEmpty(parameterMappings)) {
            return sql;
        }

        //获取类型处理器注册器，类型处理器的功能是进行java类型和数据库类型的转换
        TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
        //如果根据parameterObject.getClass(）可以找到对应的类型，则替换
        if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
            sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(getParameterValue(parameterObject)));
        } else {
            // MetaObject主要是封装了originalObject对象，提供了get和set的方法用于获取和设置originalObject的属性值,
            // 主要支持对JavaBean、Collection、Map三种类型对象的操作
            MetaObject metaObject = configuration.newMetaObject(parameterObject);
            for (ParameterMapping parameterMapping : parameterMappings) {
                String property = parameterMapping.getProperty();
                if (metaObject.hasGetter(property)) {
                    Object object = metaObject.getValue(property);
                    sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(getParameterValue(object)));
                } else if (boundSql.hasAdditionalParameter(property)) {
                    // 支持动态sql
                    Object object = boundSql.getAdditionalParameter(property);
                    sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(getParameterValue(object)));
                } else {
                    // 打印出缺失，提醒该参数缺失并防止错位
                    sql = sql.replaceFirst("\\?", "缺失");
                }
            }
        }
        return sql;
    }

    // 如果参数是String，则添加单引号， 如果是日期，则转换为时间格式器并加单引号； 对参数是null和不是null的情况作了处理
    private static String getParameterValue(Object object) {
        String value = null;
        if (object instanceof String) {
            value = "'" + object.toString() + "'";
        } else if (object instanceof Date) {
            DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
            value = "'" + dateFormat.format(new Date()) + "'";
        } else {
            if (object != null) {
                value = object.toString();
            } else {
                value = "";
            }
        }
        return value;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
