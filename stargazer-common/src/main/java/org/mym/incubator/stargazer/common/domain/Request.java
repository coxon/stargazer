package org.mym.incubator.stargazer.common.domain;

import org.mym.incubator.stargazer.common.exception.StartConnectorException;

/**
 * @author coxon
 */
public interface Request {

    /**
     * base valid
     *
     * @param topic topic
     * @param hosts hosts
     */
    default void validate(String topic, String...hosts) {
        if (null == topic) {
            throw StartConnectorException.builder().message("connector topic must be non-null").build();
        }

        if (null == hosts) {
            throw StartConnectorException.builder().message("connector hosts must be non-null").build();
        }
    }
}
