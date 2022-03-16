import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mym.incubator.stargazer.spi.StargazerConnector;
import org.mym.incubator.stargazer.spi.subscriber.ConsoleSubscribeProcessor;
import org.mym.incubator.stargazer.test.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author coxon
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class ZookeeperConnectorTest {

    @Autowired
    StargazerConnector connector;

    @Test
    public void pubSub() {
        connector.subscribe("coxon", new ConsoleSubscribeProcessor());
        connector.publish("coxon/graham", "1234".getBytes());
        connector.close();
    }

    @Test
    public void lock() throws InterruptedException {
        final int threadSize = 4;
        final CountDownLatch down = new CountDownLatch(threadSize);

        ExecutorService executorService = Executors.newFixedThreadPool(threadSize);
        for (int i = 0; i < threadSize; i++) {
            executorService.submit(() -> {
                log.info(Thread.currentThread().getName() + " start!");
                long l = System.currentTimeMillis();
                boolean flag = connector.require("coxon4", 4, TimeUnit.SECONDS);
                log.info(Thread.currentThread().getName() + " " + flag);
                try {
                    down.await(2, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                connector.release("coxon4");
                down.countDown();

                log.info(Thread.currentThread().getName() + " end! cast " + (System.currentTimeMillis() - l));
            });
        }
        down.await();
        executorService.shutdown();
    }
}
