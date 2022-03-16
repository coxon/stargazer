package org.mym.incubator.stargazer.rocketmq;

import org.mym.incubator.stargazer.common.constants.Connector;
import org.mym.incubator.stargazer.common.domain.CreateRequest;
import org.mym.incubator.stargazer.common.exception.CreateConnectorFailedException;
import org.mym.incubator.stargazer.common.exception.PublishFailedException;
import org.mym.incubator.stargazer.common.exception.SubscribeFailedException;
import org.mym.incubator.stargazer.common.exception.UnauthorizedConnectorException;
import org.mym.incubator.stargazer.spi.StargazerConnector;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author coxon
 */
@Slf4j
public class RocketMqConnector implements StargazerConnector {

    @Override
    public Connector getConnector() {
        return Connector.ROCKET_MQ;
    }

    @Override
    public void initialize(CreateRequest request) throws CreateConnectorFailedException, UnauthorizedConnectorException {
        log.info("start rmq connect");
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
