import com.alibaba.fastjson2.JSON;
import com.google.common.collect.Lists;
import org.apache.ibatis.demo.Demo;
import org.apache.ibatis.demo.DemoMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.*;
import org.junit.platform.commons.logging.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 */
public class UnitTest2 {

    SqlSession sqlSession = null;

    @BeforeEach
    public void before() throws IOException {
        InputStream resourceAsStream = Resources.getResourceAsStream("MybatisConfig.xml");

        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);

        sqlSession = sqlSessionFactory.openSession();
    }

    @AfterEach
    public void after() {
        sqlSession.close();
    }

    @Test
    public void t3() throws IOException {
        DemoMapper mapper = sqlSession.getMapper(DemoMapper.class);

        HashMap<String, Object> params = new HashMap<>();
        params.put("username", "a");
        params.put("ids", Lists.newArrayList(5, 6, 7));

        List<Demo> list = mapper.selectByParam(params);

        System.out.println(JSON.toJSONString(list));
    }

    @Test
    public void t2() throws IOException {

        DemoMapper mapper = sqlSession.getMapper(DemoMapper.class);

        List<Demo> list = mapper.findAll();

        System.out.println(JSON.toJSONString(list));

    }

    @Test
    public void t1() throws IOException {

        RowBounds rowBounds = new RowBounds(1, 2);

        List<Demo> list = sqlSession.selectList("org.apache.ibatis.demo.DemoMapper.findAll", null, rowBounds);

        System.out.println(JSON.toJSONString(list));
    }

    @Test
    public void t() throws IOException {
        List<Demo> list = sqlSession.selectList("org.apache.ibatis.demo.DemoMapper.findByUsername", "a");

        System.out.println(JSON.toJSONString(list));
    }


}
