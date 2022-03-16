package org.mym.incubator.stargazer.spring.starter;

import org.mym.incubator.stargazer.common.constants.Connector;
import org.mym.incubator.stargazer.common.domain.CreateRequest;
import org.mym.incubator.stargazer.common.exception.CreateConnectorFailedException;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.Assert;

import java.util.Optional;
import java.util.function.Function;

/**
 * @author coxon
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@ConfigurationProperties(prefix = StargazerProperties.STARGAZER_PREFIX)
public class StargazerProperties {

    public static final String STARGAZER_PREFIX = "ai.stargazer";

    private static final String SPLIT_SYMBOL = ",";

    private static final String SCHEMA_SEPARATOR = "://";

    private static final long TIMEOUT = 3000L;

    /**
     * schema://host1:port,host2:port
     * multi addresses connective with ','
     *
     * @see Connector
     */
    String url;

    /**
     * optional
     */
    String password;

    /**
     * send timeout, millisecond
     */
    long timeout = TIMEOUT;

    /**
     * retry times
     */
    int retryTimes;

    /**
     * sleep between retries, millisecond
     */
    long sleepBetweenRetries = TIMEOUT;

    /**
     * required
     * sub/pub topic
     */
    Topic topic;

    /**
     * be able to create topic if not exists
     * default true
     */
    boolean createTopicIfNotExists = true;

    Function<String, String[]> buildHost = url -> {
        Assert.hasText(url, "connector url must be non-null");
        return Optional.of(url.substring(url.indexOf(SCHEMA_SEPARATOR) + (SCHEMA_SEPARATOR).length()))
                .map(m -> m.split(SPLIT_SYMBOL))
                .orElseThrow(() -> CreateConnectorFailedException.builder().message("illegal connector url").build());
    };

    /**
     * moving to stargazer-security
     */
    @Deprecated
    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Acl {
        String[] whiteList;

        String[] blackList;
    }

    CreateRequest buildCreateRequest() {
        Assert.notNull(this.topic, "");
        return CreateRequest.builder()
                .topic(topic.getName())
                .publishGroup(topic.getPublishGroup())
                .subscribeGroup(topic.getSubscribeGroup())
                .hosts(buildHost.apply(url))
                .build();
    }

    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Topic {

        String name;

        String publishGroup;

        String subscribeGroup;
    }
}