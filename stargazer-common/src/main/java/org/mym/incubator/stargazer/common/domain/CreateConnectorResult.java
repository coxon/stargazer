package org.mym.incubator.stargazer.common.domain;

import org.mym.incubator.stargazer.common.constants.Connector;
import org.mym.incubator.stargazer.common.constants.Status;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

/**
 * @author coxon
 */

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateConnectorResult implements Serializable  {
    private static final long serialVersionUID = -3742089944897487511L;

    long time;

    String description;

    Connector connector;

    Status status;
}
