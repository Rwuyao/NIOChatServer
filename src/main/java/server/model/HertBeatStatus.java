package server.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import server.util.propertiesUtils;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
@Builder
public class HertBeatStatus {

    private  int MAX_UN_REC_PING_TIMES ;// 没有收到服务端心跳次数的阀值
    private int Threshold_SECONDS;
    private volatile LocalDateTime lastActionTime=LocalDateTime.now(); //上一次接收或者发送聊天消息或者接收到心跳包时更新
    private int noRec=1;

    public HertBeatStatus(){
        loadProperties();
    }

    public void loadProperties(){
        this.MAX_UN_REC_PING_TIMES= propertiesUtils.getInt("config/hertbeator","MAX_UN_REC_PING_TIMES");
        this.Threshold_SECONDS = propertiesUtils.getInt("config/hertbeator","Threshold_SECONDS");
    }

    public void increaseNoRec(){
        noRec++;
    }

    public void refreshLastActionTime(){
        lastActionTime=LocalDateTime.now();
    }
}
