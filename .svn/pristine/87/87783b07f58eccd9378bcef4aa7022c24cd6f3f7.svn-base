package newwater.com.newwater.manager;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadManager {

    private static ThreadManager instance = new ThreadManager();
    private ThreadPoolProxy longPoolProxy;
    private ThreadPoolProxy shortPoolProxy;


    public static ThreadManager getInstance(){
        return instance;
    }

    private ThreadManager(){

    }

    /**
     * 长线程池
     * @return
     */
    public synchronized ThreadPoolProxy createLongPool(){
        if (longPoolProxy == null) {
            //(int corePoolSize 线程池大小, int maximumPoolSize 最大值, long keepAliveTime 存活时间)
            longPoolProxy = new ThreadPoolProxy(5, 5, 5000);
        }
        return longPoolProxy;
    }
    /**
     * 短线程池
     * @return
     */
    public synchronized ThreadPoolProxy createShortPool(){
        if (shortPoolProxy == null) {
            shortPoolProxy = new ThreadPoolProxy(3, 3, 5000);
        }
        return shortPoolProxy;
    }

    public class ThreadPoolProxy{
        private ThreadPoolExecutor pool;
        private int corePoolSize; //线程数
        private int maximumPoolSize; //线程满了后额外开的线程窗口
        private long keepAliveTime;//没有线程执行时存活时间


        private ThreadPoolProxy(int corePoolSize,int maximumPoolSize, long keepAliveTime){
            this.corePoolSize = corePoolSize;
            this.maximumPoolSize = maximumPoolSize;
            this.keepAliveTime = keepAliveTime;
        }

        /**
         * 执行线程
         * @param runnable
         */
        public void execute(Runnable runnable){
            if (pool == null) {
                //最多可有多少个线程排队
                BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>(10);
                pool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.MILLISECONDS, workQueue);
            }
            pool.execute(runnable);
        }
        /**
         * 取消线程
         * @param runnable
         */
        public void cancel(Runnable runnable){
            if (pool != null) {
                pool.remove(runnable);
            }
        }
    }
}
