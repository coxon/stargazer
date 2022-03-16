package org.mym.incubator.stargazer.spi;

import org.mym.incubator.stargazer.common.domain.CreateRequest;
import org.mym.incubator.stargazer.common.exception.CreateConnectorFailedException;
import org.mym.incubator.stargazer.common.exception.UnauthorizedConnectorException;

/**
 * @author coxon
 */
public interface ConnectorLifecycle {

    /**
     * initialize
     *
     * @param request create connector parameters
     * @throws CreateConnectorFailedException throw if create connect failed
     * @throws UnauthorizedConnectorException throw if connect authorization failed.
     */
    void initialize(CreateRequest request) throws CreateConnectorFailedException, UnauthorizedConnectorException;


    /**
     * close connector
     */
    void close();
}
