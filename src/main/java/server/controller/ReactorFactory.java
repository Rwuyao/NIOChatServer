package server.controller;

import server.model.HertBeator;
import server.model.ServerSession;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReactorFactory {
    private static final ServerSession session= new ServerSession();

    /**
     *
     * @return
     * @throws IOException 创建Reactor失败，抛出异常，结束程序
     */
    public static Reactor[] build(int ThreadNum) throws IOException {
        //初始化HerBeator
        HertBeator hertBeator= new HertBeator(session);
        (new Thread(hertBeator)).start();
        //初始化Reactor
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(ThreadNum);

        Reactor[] reactors =new Reactor[ThreadNum];
        for(int i=0;i<ThreadNum;i++){
            reactors[i]=new Reactor(session);
            fixedThreadPool.execute(reactors[i]);
        }
        return reactors;
    }

}
