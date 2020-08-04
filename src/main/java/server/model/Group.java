package server.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import server.service.userGroupService;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Group {
    private Integer id;

    private String groupname;

    private String createuser;

    private Date createtime;

    private String iconurl;

    @JSONField(serialize=false)
    private volatile List<String> usreList;

    public Group(String groupname){
        this.groupname=groupname;
    }

    public List<String> getUsrelist() {
        //延迟加载
        if (usreList == null) {
            synchronized(User.class){
                if(usreList == null){
                    usreList = userGroupService.getUserByGroupname(groupname);
                }
            }
        }
        return usreList;
    }

    public void setUsreList(List<String> usrelist) {
        this.usreList = usrelist;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Group group = (Group) o;
        return Objects.equals(groupname, group.groupname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupname);
    }
}