package server.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import server.service.groupService;
import server.util.DateUtil;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class User {

    private Integer id;
    private String username;//账号
    private String password;
    private String nickname;
    private int sex;
    private Date createtime;
    private String iconurl= "view/icon.jpeg";
    @JSONField(serialize=false)
    private HertBeatStatus hertBeatStatus=new HertBeatStatus();
    private volatile  List<Group> groupList;

    public User(String username){
        this.username= username;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    //判断该用户的状态，检查心跳连接
    public boolean JudgingTheState(){

        //获取当前时间与上次操作时间的差值
        long seconds=  DateUtil.compareTimeWithNow( hertBeatStatus.getLastActionTime());

        //判断等待时间是否超过阀值
        if(seconds>hertBeatStatus.getThreshold_SECONDS()*hertBeatStatus.getNoRec()) {
            //超时
            hertBeatStatus.increaseNoRec();
            //超过给定次数未收到消息，认为失联，开始重连，直到连接上
            if (hertBeatStatus.getNoRec() > hertBeatStatus.getMAX_UN_REC_PING_TIMES()) {
                return true;
            }
        }else{
            hertBeatStatus.setNoRec(1);
        }

        return false;
    }

    public List<Group> getGroupList() {
        //延迟加载
        if (groupList == null) {
            synchronized(User.class){
                if(groupList == null){
                    groupList = groupService.getGroupByUsername(username);
                }
            }
        }
        return groupList;
    }

    public void setGroupList(List<Group> groupList) {
        this.groupList = groupList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return username.equals(user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }


}
