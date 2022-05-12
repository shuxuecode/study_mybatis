import com.alibaba.fastjson2.JSON;
import org.apache.ibatis.demo.Demo;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @date 2021/10/25
 */
public class UnitTest {

    public static void main(String[] args) throws IOException {

        InputStream resourceAsStream = Resources.getResourceAsStream("MybatisConfig.xml");

        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);

        SqlSession sqlSession = sqlSessionFactory.openSession();

        List<Demo> list = sqlSession.selectList("org.apache.ibatis.demo.DemoMapper.findAll");


        System.out.println(list);
        System.out.println();
        System.out.println(JSON.toJSONString(list));
        System.out.println(JSON.toJSONString(list));


        sqlSession.close();
    }


}
