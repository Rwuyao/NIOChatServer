package server.controller;

import com.alibaba.fastjson.JSONObject;
import server.model.*;
import server.util.msgUtils;
import server.util.propertiesUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.List;

public class Reactor implements  Runnable{

    private volatile  boolean isShutdown=false;
    private int TIMEOUT ;
    private int BufferSize;
    private ServerSession session;
    private ByteBuffer buffer;
    private Selector selector;

    public Reactor( ){
    }

    /**
     *
     * @param session
     * @throws IOException 创建Reactor失败，抛出异常，结束程序
     */
    public Reactor( ServerSession session) throws IOException {
        loadProperties();
        selector= Selector.open();
        buffer= ByteBuffer.allocate(BufferSize);
        this.session=session;
    }

    public void loadProperties(){
        this.TIMEOUT = propertiesUtils.getInt("config/reactor","TIMEOUT");
        this.BufferSize= propertiesUtils.getInt("config/reactor","BufferSize");
    }

    public void stop(){
        isShutdown=true;
    }

    @Override
    public void run() {
        selector();
    }

    public void selector() {
        //无限循环处理事件
        while (!isShutdown && !Thread.interrupted()) {
            try {
                if (selector.select(TIMEOUT) == 0) {
                    continue;
                }
                Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
                while (iter.hasNext()) {
                    SelectionKey key = iter.next();
                    if (key.isValid()) {
                        if (key.isAcceptable()) {
                            handleAccept(key);
                        } else if (key.isReadable()) {
                            handleRead(key);
                        } else if (key.isWritable()) {
                            handleWrite(key);
                        }
                    } else {
                        //键无效了，就取消
                        key.cancel();
                    }
                    iter.remove();
                }
            } catch (Exception e) {
                //这个catch 保证selector.select(TIMEOUT)循环不会中断 可能有SelectionKey取消等等异常发生
                e.printStackTrace();
            }

        }
    }

    /**
     *
     * @param key
     */
    public void handleAccept(SelectionKey key)  {
        ServerSocketChannel ssChannel = (ServerSocketChannel)key.channel();
        try  {
            SocketChannel sc = ssChannel.accept();
            sc.configureBlocking(false);
            sc.register(selector, SelectionKey.OP_READ);
        } catch (IOException e) {
            System.out.println("接收连接失败");
            key.cancel();
            //e.printStackTrace();
        }
    }

    /**
     * 读完之后，回复消息给客户端
     * @param key
     */

    public void handleRead(SelectionKey key)  {
        SocketChannel sc = (SocketChannel)key.channel();
        //将数据写入到缓存区域
        ByteArrayOutputStream bArray = new ByteArrayOutputStream();

        long bytesRead=0;
       try{
           //开始循环读取数据
           bytesRead = sc.read(buffer);
            while(bytesRead>0){
                //调整
                buffer.flip();
                //获取发送过来的byte数组
                byte[] bytes = new byte[buffer.remaining()];
                buffer.get(bytes);
                bArray.writeBytes(bytes);
                //清空
                buffer.clear();
                bytesRead = sc.read(buffer);
            }
       }catch(SocketException e){
            System.out.println("连接中断");
            key.cancel();
            return ;
       } catch (IOException e) {
            e.printStackTrace();
           return ;
       }
        //将字符串转换为Message
        Message msg=null;
        try{
            msg= JSONObject.parseObject(bArray.toString(), Message.class);
        }catch (Exception e){
            System.out.println("粘包或者json格式异常");
            e.printStackTrace();
            return;
        }
        //处理Message中的请求
        try {
            handleMsg(msg,sc);
        } catch (IOException e) {
            System.out.println(msg.getFrom()+"消息处理失败");
        }
        //返回-1代表中断连接
        if(bytesRead == -1){
            try {
                sc.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleMsg(Message msg,SocketChannel sc) throws IOException {
        //判断消息请求类型，再进行处理
        switch (msg.getType()){
            case Login:{
                String username= msg.getFrom();
                User u=new User(username);
                //检查用户是否已经登录
                if(!session.isExistUserSession(u)){
                    System.out.println(username+"登录成功");
                    //回复消息
                    msg.setResult(Result.SUCCESS(session.getUserSession().values()));
                    try {
                        sc.write(msgUtils.MsgToByteBuffer(msg));
                        //将用户信息及socketChannel添加到缓存中
                        session.addSession(u,sc);
                        //提醒其余用户该用户上线
                        session.broadcastUserLoginTips(u);
                    } catch (IOException e) {
                        //无法回复，有必要断开连接
                        e.printStackTrace();
                    }

                }else{
                    //返回消息
                    msg.setResult(Result.fail("用户已登录"));
                    sc.write(msgUtils.MsgToByteBuffer(msg));
                }
                break;
            }
            case Logout:{
                String username= msg.getFrom();
                User u=new User(username);
                session.removeSession(u);
                //提醒其余用户该用户下线
                session.broadcastUserLogoutTips(u);

                //回复消息
                msg.setResult(Result.SUCCESS());
                try {
                    sc.write(msgUtils.MsgToByteBuffer(msg));
                } catch (IOException e) {
                    System.out.println("回复消息"+username+"失败");
                }finally {
                    try {
                        sc.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            }
            case SingleMsg:{
                //更新对应的心跳连接
                session.getSession(msg.getFrom()).getHertBeatStatus().refreshLastActionTime();
                //检查目的用户是否在线
                String toUser= msg.getTo();
                User u=new User(toUser);
                if(session.isExistUserSession(u)){
                    //直接把消息转发
                    session.transferSingleMsg(u,msg);
                }else{
                    //回复消息
                    msg.setResult(Result.fail("对方已下线"));
                    sc.write( msgUtils.MsgToByteBuffer(msg));
                }
                break;
            }
            case GroupMsg:{
                //获取用户
                User u=session.getSession(msg.getFrom());
                u.getHertBeatStatus().refreshLastActionTime();
                //获取群组
                List<Group> grouplist= u.getGroupList();
                Group toGroup= new Group(msg.getTo());

                if(grouplist.contains(toGroup)){
                    int index= grouplist.indexOf(toGroup);
                    session.transferGroupMsgAndExcludeSomeOne(msg,grouplist.get(index),msg.getFrom());
                }else{
                    //回复消息
                    msg.setResult(Result.fail("该群组不存在或已经解散"));
                    sc.write( msgUtils.MsgToByteBuffer(msg));
                }
                break;
            }
            case HeartBeat:{
               //更新对应的心跳连接
                User u=session.getSession(msg.getFrom());
                if(u==null){
                    //将用户信息及socketChannel添加到缓存中
                    u=new User(msg.getFrom());
                    session.addSession(u,sc);
                    //提醒其余用户该用户上线
                    session.broadcastUserLoginTips(u);
                }else{
                    u.getHertBeatStatus().refreshLastActionTime();
                    //回复心跳连接
                    sc.write(msgUtils.MsgToByteBuffer(msg));
                }
                break;
            }
        }
    }


    public void handleWrite(SelectionKey key) {
        //通常会使用key.attach()将要发送的消息附在SelectionKey上，等可写的时候再发送。我们就直接使用socketchannel.write()发送了。
    }



}
