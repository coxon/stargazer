package org.mym.incubator.stargazer.spi;

import org.mym.incubator.stargazer.common.constants.Connector;
import org.mym.incubator.stargazer.common.exception.PublishFailedException;
import org.mym.incubator.stargazer.common.exception.SubscribeFailedException;
import org.mym.incubator.stargazer.common.exception.UnauthorizedConnectorException;
import org.mym.incubator.stargazer.spi.subscriber.SubscriberProcessor;

import java.util.concurrent.TimeUnit;

/**
 * SPI Handler
 *
 * @author coxon
 */
public interface StargazerConnector extends ConnectorLifecycle {

    /**
     * get current connector
     *
     * @return {@link Connector}
     */
    Connector getConnector();

    /**
     * publish
     *
     * @param topic   topic
     * @param payload payload body
     * @throws UnauthorizedConnectorException throw if connect authorization failed.
     * @throws PublishFailedException         throw if publish failed.
     */
    void publish(String topic, byte[] payload) throws PublishFailedException, UnauthorizedConnectorException;

    /**
     * subscribe
     *
     * @param topic    topic
     * @param listener listener
     * @throws UnauthorizedConnectorException throw if connect authorization failed.
     * @throws SubscribeFailedException       throw if subscribe failed.
     */
    void subscribe(String topic, SubscriberProcessor listener) throws SubscribeFailedException, UnauthorizedConnectorException;

    /**
     * require distributed lock for topic
     *
     * @param topic    lock topic
     * @param time     time to wait
     * @param timeUnit time unit
     * @return true or false
     */
    boolean require(String topic, long time, TimeUnit timeUnit);

    /**
     * release lock for topic
     *
     * @param topic locked topic
     */
    void release(String topic);
}
