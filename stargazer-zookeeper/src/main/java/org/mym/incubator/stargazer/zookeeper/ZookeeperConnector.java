package org.mym.incubator.stargazer.zookeeper;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.framework.recipes.locks.InterProcessSemaphoreMutex;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.data.Stat;
import org.mym.incubator.stargazer.common.constants.Connector;
import org.mym.incubator.stargazer.common.domain.CreateRequest;
import org.mym.incubator.stargazer.common.exception.CreateConnectorFailedException;
import org.mym.incubator.stargazer.common.exception.PublishFailedException;
import org.mym.incubator.stargazer.common.exception.SubscribeFailedException;
import org.mym.incubator.stargazer.common.exception.UnauthorizedConnectorException;
import org.mym.incubator.stargazer.spi.StargazerConnector;
import org.mym.incubator.stargazer.spi.subscriber.SubscriberProcessor;

import javax.annotation.PreDestroy;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author coxon
 */
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ZookeeperConnector implements StargazerConnector {

    String topic;

    CuratorFramework client;

    boolean closed = false;

    private static final String NAMESPACE_START_WITH = "/";
    private static final String DEFAULT_WORK_PATH = "/stargazer";
    private static final String DEFAULT_LOCK_PATH = "lock";

    private static final ConcurrentHashMap<String, InterProcessSemaphoreMutex> LOCKED_TOPICS = new ConcurrentHashMap<>();

    @Override
    public Connector getConnector() {
        return Connector.ZOOKEEPER;
    }

    @Override
    public void initialize(CreateRequest request) throws CreateConnectorFailedException, UnauthorizedConnectorException {
        log.info("start zk connect");
        topic = request.getTopic();
        topic = Optional.ofNullable(this.topic).orElse(DEFAULT_WORK_PATH);
        if (!topic.startsWith(NAMESPACE_START_WITH)) {
            topic = NAMESPACE_START_WITH + topic;
        }
        client = CuratorFrameworkFactory.newClient(String.join(",", request.getHosts()), new RetryNTimes(request.getRetryTimes(), request.getSleepBetweenRetries()));
        try {
            client.start();
        } catch (Exception e) {
            log.error("initial zookeeper client failed, cause : {}", e.getLocalizedMessage());
            throw new CreateConnectorFailedException("initial zookeeper client failed");
        }

    }

    @PreDestroy
    @Override
    public void close() {
        if (closed) {
            return;
        }
        if (null != client) {
            client.close();
        }
        closed = true;
        log.info("zookeeper connector closed");
    }

    @Override
    public void publish(String topic, byte[] payload) throws PublishFailedException, UnauthorizedConnectorException {
        String node = checkThenCreateNode(topic);
        try {
            client.setData().forPath(node, payload);
        } catch (Exception e) {
            log.error("pub failed, ex: {}", e.getLocalizedMessage());
        }
    }

    @Override
    public void subscribe(String topic, SubscriberProcessor processor) throws SubscribeFailedException, UnauthorizedConnectorException {
        String node = checkThenCreateNode(topic);

        PathChildrenCache cache = new PathChildrenCache(client, node, true);

        PathChildrenCacheListener listener = (client, event) -> {
            if (PathChildrenCacheEvent.Type.CHILD_ADDED == event.getType() || PathChildrenCacheEvent.Type.CHILD_UPDATED == event.getType()) {
                processor.onMessage(event.getData().getData());
            } else {
                log.warn("catch event {}, ignore it", event.getType());
            }
        };

        cache.getListenable().addListener(listener);
        try {
            cache.start(PathChildrenCache.StartMode.BUILD_INITIAL_CACHE);
        } catch (Exception e) {
            log.error("sub failed, ex: {}", e.getLocalizedMessage());
        }
    }

    @Override
    public boolean require(String node, long time, TimeUnit timeUnit) {
        node = Optional.ofNullable(node).orElse(DEFAULT_LOCK_PATH);
        String path = String.join("/", topic, node);
        InterProcessSemaphoreMutex mutex = LOCKED_TOPICS.get(node);
        if (null == mutex) {
            mutex = new InterProcessSemaphoreMutex(client, path);
        }
        try {
            boolean acquired = mutex.acquire(time, timeUnit);
            if (acquired) {
                LOCKED_TOPICS.put(node, mutex);
            }
            return acquired;
        } catch (Exception e) {
            log.error("require lock failed, ex: {}", e.getLocalizedMessage());
        }
        return false;
    }

    @Override
    public void release(String node) {
        node = Optional.ofNullable(node).orElse(DEFAULT_LOCK_PATH);
        Optional.ofNullable(LOCKED_TOPICS.get(node)).ifPresent(m -> {
            try {
                if (m.isAcquiredInThisProcess()) {
                    m.release();
                    log.debug("lock released");
                }
            } catch (Exception e) {
                log.error("release lock failed, ex: {}", e.getLocalizedMessage());
            }
        });
    }

    private String checkThenCreateNode(String node) {
        String path = String.join("/", topic, node);
        try {
            Stat stat = client.checkExists().forPath(path);
            if (Objects.isNull(stat)) {
                return client.create().creatingParentsIfNeeded().forPath(path);
            }
        } catch (Exception e) {
            log.error("pub/sub error, create node exception {}", e.getLocalizedMessage());
            throw new RuntimeException("create pub/sub node failed");
        }
        return path;
    }
}
