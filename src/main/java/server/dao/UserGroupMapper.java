package server.dao;

import org.apache.ibatis.annotations.Param;
import server.model.UserGroup;
import server.model.UserGroupExample;

import java.util.List;

public interface UserGroupMapper {
    long countByExample(UserGroupExample example);

    int deleteByExample(UserGroupExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(UserGroup record);

    int insertSelective(UserGroup record);

    List<UserGroup> selectByExample(UserGroupExample example);

    UserGroup selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") UserGroup record, @Param("example") UserGroupExample example);

    int updateByExample(@Param("record") UserGroup record, @Param("example") UserGroupExample example);

    int updateByPrimaryKeySelective(UserGroup record);

    int updateByPrimaryKey(UserGroup record);

    List<String> selectUserNameByGroupName(@Param("groupname") String groupname);
}