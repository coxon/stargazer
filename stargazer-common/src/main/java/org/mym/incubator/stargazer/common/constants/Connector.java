package org.mym.incubator.stargazer.common.constants;

import org.mym.incubator.stargazer.common.exception.CreateConnectorFailedException;
import org.mym.incubator.stargazer.common.exception.StartConnectorException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.stream.Stream;

/**
 * @author coxon
 */
@Slf4j
@Getter
@AllArgsConstructor
public enum Connector {

    /**
     * zookeeper
     * schema format zookeeper://
     * See <a href="https://zookeeper.apache.org/">Apache Zookeeper</a>
     */
    ZOOKEEPER("zookeeper://") {
        @Override
        public String classPath() {
            return "org.mym.incubator.stargazer.zookeeper.ZookeeperConnector";
        }
    },

    /**
     * redis
     * schema format redis://
     * See <a href="https://redis.io/">Redis</a>
     */
    REDIS("redis://") {
        @Override
        public String classPath() {
            return "org.mym.incubator.stargazer.redis.RedisConnector";
        }
    },

    /**
     * rocket mq
     * schema format rmq://
     * See <a href="https://rocketmq.apache.org/">Apache RocketMq</a>
     */
    ROCKET_MQ("rocketmq://") {
        @Override
        public String classPath() {
            return "org.mym.incubator.stargazer.rocketmq.RocketMqConnector";
        }
    };

    String schema;

    /**
     * obtain connector by give url
     * @param url
     * @return Connector
     */
    public static Connector fetchConnector(String url) {
        if (null == url) {
            throw new StartConnectorException("connector url must be non-null");
        }
        return Stream.of(Connector.values())
                .filter(m -> url.startsWith(m.getSchema()))
                .findFirst()
                .orElseThrow(() -> new CreateConnectorFailedException(String.format("unsupported schema {%s}", url)));
    }

    public abstract String classPath();
}
