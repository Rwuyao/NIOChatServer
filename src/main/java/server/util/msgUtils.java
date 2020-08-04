package server.util;

import server.model.Message;
import server.model.MessageType;
import server.model.Result;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.time.LocalDateTime;

public class msgUtils {
    private static final Charset charset = Charset.forName("UTF-8");

    public  static Message UserLoginTips(Result result){
        return
                Message
                        .builder()
                        .from("server")
                        .to("all")
                        .time(LocalDateTime.now())
                        .result(result)
                        .type(MessageType.AUserLogin)
                        .build();
    }

    public  static Message UserLogoutTips(Result result){
        return
                Message
                        .builder()
                        .from("server")
                        .to("all")
                        .time(LocalDateTime.now())
                        .result(result)
                        .type(MessageType.AUserLogout)
                        .build();
    }


    public  static ByteBuffer MsgToByteBuffer(Message msg){
        return  charset.encode(msg.toString());
    }

}
