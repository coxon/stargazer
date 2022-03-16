package org.mym.incubator.stargazer.spi.subscriber;

import lombok.extern.slf4j.Slf4j;

/**
 * print as message to console
 * @author coxon
 */
@Slf4j
public class ConsoleSubscribeProcessor implements SubscriberProcessor {

    @Override
    public void onMessage(byte[] message) {
        log.info("message coming {}", new String(message));
    }
}