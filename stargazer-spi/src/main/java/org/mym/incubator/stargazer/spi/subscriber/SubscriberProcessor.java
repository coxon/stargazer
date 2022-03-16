package org.mym.incubator.stargazer.spi.subscriber;

/**
 * @author coxon
 */
@FunctionalInterface
public interface SubscriberProcessor {

    /**
     * process message
     *
     * @param message message body
     */
    void onMessage(byte[] message);
}