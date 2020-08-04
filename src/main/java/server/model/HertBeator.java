package server.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class HertBeator implements Runnable {

    private volatile  boolean isShutdown=false;
    private ServerSession session;

    public HertBeator(ServerSession session) {
        this.session = session;
    }


    public void stop(){
        isShutdown=true;
    }

    @Override
    public void run() {
        try {
            while(!isShutdown &&!Thread.interrupted()) {
               //循环遍历
                ConcurrentHashMap<String, User> userList= session.getUserSession();
                Iterator<Map.Entry<String, User>> it = userList.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, User> entry = it.next();
                    User u =entry.getValue();
                    if (u.JudgingTheState()) {
                        //移除userSession中的元素
                        System.out.println(u.getUsername()+"超时,"+u.getHertBeatStatus().getLastActionTime().toString()+"_"+LocalDateTime.now().toString());
                        it.remove();
                        //移除channelSession中的元素并断开连接
                        session.removeAndCloseChannelSessioin(u.getUsername());
                        //提醒其余用户该用户下线
                        session.broadcastUserLogoutTips(u);
                    }
                }
                TimeUnit.SECONDS.sleep(1);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
