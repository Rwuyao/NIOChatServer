package server.model;

import server.util.msgUtils;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ConcurrentHashMap;

public class ServerSession {
    private ConcurrentHashMap<String, User> userSession;//存储当前用户信息
    private ConcurrentHashMap<String, SocketChannel> channelSession;//存储当前所有通道

    public ServerSession() {
        userSession=new ConcurrentHashMap<>();//存储当前用户信息
        channelSession=new ConcurrentHashMap<>();//存储当前所有通道
    }

    //移除缓存并断开连接
    public void removeAndCloseChannelSessioin(String username){
       SocketChannel sc= channelSession.remove(username);
       if(sc!=null){
           try {
               sc.close();
           } catch (IOException e) {
               e.printStackTrace();
           }
       }
    }



    //获取用户缓存
    public User getSession(String username){
      if(userSession.containsKey(username)){
          return  userSession.get(username);
      }
      return null;
    }

    //移除缓存
    public void removeSession(User u){
        userSession.remove(u);
        channelSession.remove(u.getUsername());
    }


    //添加缓存
    public void addSession(User u, SocketChannel sc){
        userSession.put(u.getUsername(),u);
        channelSession.put(u.getUsername(),sc);
    }

    //单对单发送消息
    public void  transferSingleMsg(User u, Message msg) throws IOException {
        channelSession.get(u.getUsername()).write(msgUtils.MsgToByteBuffer(msg));
    }
    //群发消息
    public void broadcastMsg(Message msg){
        channelSession.forEach((String s,SocketChannel c) -> {
            try {
                c.write(msgUtils.MsgToByteBuffer(msg));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
    //群发消息
    public void transferGroupMsgAndExcludeSomeOne(Message msg, Group g, String ExcludeUser){
        g.getUsrelist().stream().forEach((s)->{
            if(!ExcludeUser.equals(s)){
                try {
                    if(channelSession.containsKey(s)){
                        SocketChannel sc=channelSession.get(s);
                        sc.write(msgUtils.MsgToByteBuffer(msg));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    //群发消息
    public void broadcastMsgAndExcludeSomeOne(Message msg, User u){
        //循环播报
        channelSession.forEach((String s,SocketChannel c) -> {
            try {
                //排除自己
                if(!s.equals(u.getUsername())){
                    c.write(msgUtils.MsgToByteBuffer(msg));
                }
            } catch (IOException e) {
               System.out.println(u.getUsername()+",连接中断");
               //因为连接中断，所以相应的心跳连接也会慢慢超时，到时候会再发送依次下线消息，所以不需要我们手动去清除相关信息
            }
        });
    }

    //广播用户登录的信息
    public void broadcastUserLoginTips(User u){
        //创建播报信息
        Message tipMsg= msgUtils.UserLoginTips(Result.SUCCESS(u));
        //循环播报
        broadcastMsgAndExcludeSomeOne(tipMsg,u);
    }

    //广播用户登录的信息
    public void broadcastUserLogoutTips(User u){
        //创建播报信息
        Message tipMsg= msgUtils.UserLogoutTips(Result.SUCCESS(u));
        //循环播报
        broadcastMsgAndExcludeSomeOne(tipMsg,u);
    }

    //检查userSessioin中是否存在该用户
    public boolean isExistUserSession(User u){
        if(userSession.contains(u)){
            return true;
        }else{
            return false;
        }
    }

    public ConcurrentHashMap<String, User> getUserSession() {
        return userSession;
    }

    public void setUserSession(ConcurrentHashMap<String, User> userSession) {
        this.userSession = userSession;
    }

    public ConcurrentHashMap<String, SocketChannel> getChannelSession() {
        return channelSession;
    }

    public void setChannelSession(ConcurrentHashMap<String, SocketChannel> channelSession) {
        this.channelSession = channelSession;
    }
}
