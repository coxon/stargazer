package org.mym.incubator.stargazer.common.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * @author coxon
 */
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateRequest implements Request {

    /**
     * topic for sub and pub
     */
    String topic;

    /**
     * group of publish group
     */
    String publishGroup;

    /**
     * group of subscribe
     */
    String subscribeGroup;

    /**
     * hosts
     */
    String[] hosts;

    /**
     * send timeout, millisecond
     */
    long timeout;

    /**
     * retry times
     */
    int retryTimes;

    /**
     * sleep between retries, millisecond
     */
    int sleepBetweenRetries;

    /**
     * accessible list
     * move to security
     */
    @Deprecated
    String[] whiteList;

    /**
     * inaccessible list
     * move to security
     */
    @Deprecated
    String[] blackList;
}
