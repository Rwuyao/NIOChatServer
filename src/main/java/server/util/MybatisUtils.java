package server.util;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

public class MybatisUtils {

    private static SqlSessionFactory sqlSessionFactory ;
   //初始化SqlSessionFactory
   static {
       String resource = "config/mybatis-config.xml";
       try (InputStream inputStream = Resources.getResourceAsStream(resource)) {
           sqlSessionFactory =new SqlSessionFactoryBuilder().build(inputStream);
       } catch (IOException e) {
           e.printStackTrace();
       }
   }

   public static SqlSession getCommonMapper(){
       SqlSession openSession = sqlSessionFactory.openSession();
       return openSession;
   }
}
