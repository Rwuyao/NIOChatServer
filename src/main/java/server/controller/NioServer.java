package server.controller;

import server.util.propertiesUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Random;

public class NioServer {

    private static final int cpuNum=2*Runtime.getRuntime().availableProcessors();
    private  int PORT ;
    private  int TIMEOUT ;
    private  Random random = new Random();
    private  int ThreadNum;
    private Reactor[] reactors ;
    private Selector selector ;
    private ServerSocketChannel ssc ;

    public static void main(String[] args)
    {
        NioServer server = new NioServer();
        server.loadProperties();
        if(server.init()){
            System.out.println("server初始化完毕");
            server.selector();
        };
    }

    public void loadProperties(){
        this.PORT= propertiesUtils.getInt("config/reactor","PORT");
        this.TIMEOUT = propertiesUtils.getInt("config/reactor","TIMEOUT");
        this.ThreadNum=cpuNum+propertiesUtils.getInt("config/reactor","ThreadNum");
    }

    /**
     *  初始化失败就结束返回
     * @return
     */
    public  boolean init()  {
        try {
            reactors= ReactorFactory.build(ThreadNum);
        } catch (IOException e) {
            //创建Reactor失败，抛出异常，结束程序
            System.out.println("创建Reactor失败，抛出异常，结束程序");
            //e.printStackTrace();
            return false;
        }

        //将ServerSocketChannel注册到Selector，关注ACCEPT事件
        try {
            selector = Selector.open();
            ssc= ServerSocketChannel.open();
            ssc.socket().bind(new InetSocketAddress(PORT));
            ssc.configureBlocking(false);
            ssc.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            //初始化selector失败
            System.out.println("初始化selector失败");
            //e.printStackTrace();
            return false;
        }
        return true;
    }

    public  void selector() {

        System.out.println("server启动");

        while(true) {
            try{
                if (selector.select(TIMEOUT) == 0) {
                    continue;
                }

                Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
                while (iter.hasNext()) {
                    SelectionKey key = iter.next();
                    if(key.isValid()){
                        if (key.isAcceptable()) {
                            handleAccept(key);
                        }
                    }else{
                        key.cancel();
                    }
                    iter.remove();
                }
            }catch(Exception e){
                //捕获所有异常，以免selector.select(TIMEOUT)中断
                e.printStackTrace();
            }
        }



    }

    public  void handleAccept(SelectionKey key) {
        //随机选择一个Reactor，然后注册
       int n= random.nextInt(cpuNum);
       reactors[n].handleAccept(key);
    }



}
