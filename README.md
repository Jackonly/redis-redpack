# redis-redpack

#### 介绍
基于redis加lua脚本实现高并发下的抢红包设计

测试入口：genRedpack()生成红包，snatchRedpack()抢红包

```java
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
                try {
                    System.out.println(Thread.currentThread().getName()+"准备抢红包");
                    barrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
                String result = redpackService.snatchRedpack(idWorker.nextId(), 111111);
                if ("0".equals(result)){
                    System.out.println(Thread.currentThread().getName() + "未抢到红包，原因：红包已领完");
                }else if ("1".equals(result)){
                    System.out.println(Thread.currentThread().getName() + "未抢到红包，原因：红包已领过");
                }else{
                    System.out.println(Thread.currentThread().getName() + "抢到红包：" + result);
                }
            },"thread"+i).start();
        }
        Thread.sleep(Integer.MAX_VALUE);
    }

}
```



详细设计说明，可访问我的博客：https://my.oschina.net/suzheworld/blog/2995288
