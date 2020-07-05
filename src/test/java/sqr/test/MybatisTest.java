package sqr.test;



import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import sqr.dao.IUserMap;
import sqr.domain.User;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

/*
* mybatis的测试
* */
public class MybatisTest {
    private SqlSession sqlSession;
    private InputStream in;
    private IUserMap userMap;
    private SqlSessionFactory factory;
    @Before
    public void init()throws Exception{
        //1、读取配置文件
        in= Resources.getResourceAsStream("SqlMapConfig.xml");
        //2、创建工厂对象
        factory = new SqlSessionFactoryBuilder().build(in);
        //3、创建sqlSession对象
        sqlSession = factory.openSession();
        //4、创建代理对象
        userMap = sqlSession.getMapper(IUserMap.class);
    }
    @After
    public void destory()throws Exception{
        //事务提交
        sqlSession.commit();
        //6、释放资源
        sqlSession.close();
        in.close();
    }
    @Test
    public void testFirstCache(){
        //5、查询所有
        User user1 = userMap.findUserById(41);
        sqlSession.clearCache();
        User user2 = userMap.findUserById(41);
        System.out.println(user1);
        System.out.println(user2);
        System.out.println(user1==user2);
    }
    @Test
    public void testSecondCache(){
        //5、查询所有
        SqlSession session1 = factory.openSession();
        SqlSession session2 = factory.openSession();
        userMap = session1.getMapper(IUserMap.class);
        User user1 = userMap.findUserById(41);
        IUserMap userMap2 = session1.getMapper(IUserMap.class);
        User user2 = userMap2.findUserById(41);
        System.out.println(user1);
        System.out.println(user2);
        System.out.println(user1==user2);
    }
}
