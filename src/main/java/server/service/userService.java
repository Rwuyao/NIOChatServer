package server.service;

import org.apache.ibatis.session.SqlSession;
import server.dao.UserMapper;
import server.model.User;
import server.model.UserExample;
import server.util.MybatisUtils;

import java.util.List;

public class userService {

    public static boolean isExitsUserName(String username) {
        try(SqlSession session= MybatisUtils.getCommonMapper()) {
            UserMapper UserMapper = session.getMapper(server.dao.UserMapper.class);
            UserExample example = new UserExample();
            example.createCriteria().andUsernameEqualTo(username);
            long count = UserMapper.countByExample(example);
            if (count > 0) {
                return true;
            } else {
                return false;
            }
        }
    }

    public static User getUserByUsername(String username){
        try(SqlSession session= MybatisUtils.getCommonMapper()) {
            UserMapper UserMapper = session.getMapper(server.dao.UserMapper.class);
            UserExample example = new UserExample();
            example.createCriteria().andUsernameEqualTo(username);
           List<User> userlist= UserMapper.selectByExample(example);
            if(userlist!=null&& userlist.size()>0 ){
               return userlist.get(0);
            }else{
                return null;
            }
        }
    }

    public static void saveUser(User user) {
        try(SqlSession session= MybatisUtils.getCommonMapper()) {
            UserMapper UserMapper = session.getMapper(server.dao.UserMapper.class);
            UserMapper.insert(user);
            session.commit();
        }
    }
}
