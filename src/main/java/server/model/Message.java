package server.model;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Message {
    private String from;
    private String to;
    private String msg;
    @Builder.Default
    private String iconUrl="client/resource/icon.jpeg";
    private MessageType type;
    private LocalDateTime time;
    private Result result;
    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
