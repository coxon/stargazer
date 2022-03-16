package org.mym.incubator.stargazer.redis;

import lombok.extern.slf4j.Slf4j;
import org.mym.incubator.stargazer.common.constants.Connector;
import org.mym.incubator.stargazer.common.domain.CreateRequest;
import org.mym.incubator.stargazer.common.exception.CreateConnectorFailedException;
import org.mym.incubator.stargazer.common.exception.PublishFailedException;
import org.mym.incubator.stargazer.common.exception.SubscribeFailedException;
import org.mym.incubator.stargazer.common.exception.UnauthorizedConnectorException;
import org.mym.incubator.stargazer.spi.StargazerConnector;
import org.mym.incubator.stargazer.spi.subscriber.SubscriberProcessor;

import java.util.concurrent.TimeUnit;

/**
 * @author coxon
 */
@Slf4j
public class RedisConnector implements StargazerConnector {

    @Override
    public Connector getConnector() {
        return Connector.REDIS;
    }

    @Override
    public void initialize(CreateRequest request) throws CreateConnectorFailedException, UnauthorizedConnectorException {
        log.info("start redis connect");
    }

    @Override
    public void publish(String topic, byte[] payload) throws PublishFailedException, UnauthorizedConnectorException {

    }

    @Override
    public void subscribe(String topic, SubscriberProcessor listener) throws SubscribeFailedException, UnauthorizedConnectorException {

    }

    @Override
    public boolean require(String topic, long time, TimeUnit timeUnit) {
        return false;
    }

    @Override
    public void release(String topic) {

    }

    @Override
    public void close() {

    }
}
