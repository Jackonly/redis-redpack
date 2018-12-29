package com.suzhe.qhb.test;

import com.suzhe.qhb.IdWorker;
import com.suzhe.qhb.JedisUtils;
import com.suzhe.qhb.RedpackService;
import org.junit.Test;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * @author suzhe
 * @date 2018/12/29
 */
public class TestRedpackService {

    @Test
    public void genRedpack(){
        JedisUtils jedisUtils = new JedisUtils("118.89.196.99", 6379, "123456");
        RedpackService redpackService = new RedpackService(jedisUtils);
        redpackService.genRedpack(111111,5);
    }

    @Test
    public void snatchRedpack() throws InterruptedException {
        JedisUtils jedisUtils = new JedisUtils("118.89.196.99", 6379, "123456");
        RedpackService redpackService = new RedpackService(jedisUtils);
        IdWorker idWorker = new IdWorker();
        int N = 100;
        CyclicBarrier barrier = new CyclicBarrier(N);

        for (int i = 0;i<N;i++){
            new Thread(()->{
                long userId = idWorker.nextId();
                try {
                    System.out.println("用户"+userId+"准备抢红包");
                    barrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
                String result = redpackService.snatchRedpack(userId, 111111);
                if ("0".equals(result)){
                    System.out.println("用户" + userId + "未抢到红包，原因：红包已领完");
                }else if ("1".equals(result)){
                    System.out.println("用户" + userId + "未抢到红包，原因：红包已领过");
                }else{
                    System.out.println("用户" + userId + "抢到红包：" + result);
                }
            },"thread"+i).start();
        }
        Thread.sleep(Integer.MAX_VALUE);
    }

}
